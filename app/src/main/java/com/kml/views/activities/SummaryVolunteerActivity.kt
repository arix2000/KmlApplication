package com.kml.views.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.Constants
import com.kml.Constants.Strings.EMPTY_STRING
import com.kml.Constants.Strings.SPACE
import com.kml.Constants.Strings.TODAY
import com.kml.R
import com.kml.data.utilities.Validator
import com.kml.data.utilities.Vibrator
import com.kml.databinding.ActivitySummarySelectedBinding
import com.kml.extensions.*
import com.kml.models.Volunteer
import com.kml.models.WorkToAdd
import com.kml.viewModels.SummaryVolunteerViewModel
import com.kml.views.BaseActivity
import com.kml.views.activities.SelectVolunteersActivity.Companion.EXTRA_CHECKED_VOLUNTEERS
import com.kml.views.activities.SelectVolunteersActivity.Companion.EXTRA_IS_ALL_CHOSEN
import com.kml.views.dialogs.MyDatePickerDialog
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.*

class SummaryVolunteerActivity : BaseActivity() {

    private lateinit var viewModel: SummaryVolunteerViewModel
    lateinit var binding: ActivitySummarySelectedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_summary_selected)
        viewModel = ViewModelProvider(this).get(SummaryVolunteerViewModel::class.java)

        getShowChosenVolunteers()
        binding.apply {
            creationDate.text = TODAY
            creationDate.setOnClickListener {
                MyDatePickerDialog().run {
                    setOnResultListener { creationDate.text = it }
                    show(supportFragmentManager, "DatePicker")
                }
            }
            restoreSavedWork()
            navigationIcon.setOnClickListener { onBackPressed() }
            sendWorkAndFinish.setOnClickListener {
                viewModel.clearCache(dataStore)
                validateAndSend { finishAdding() }
            }
            if(viewModel.isAllVolunteersChosen) {
                setDisabledMaskTo(sendWorkAndContinue)
            }
            else {
                sendWorkAndContinue.setOnClickListener {
                    validateAndSend {
                        viewModel.cacheWork(dataStore, it)
                        setResult(SelectVolunteersActivity.SUMMARY_RESULT)
                        finish()
                    }
                }
            }
        }
    }

    private fun restoreSavedWork() {
        viewModel.getSavedWork().observe(this) {
            restoreViewsStateBy(it)
        }
        viewModel.fetchSavedWork(dataStore)
    }

    private fun restoreViewsStateBy(work: WorkToAdd) {
        val desc = work.description
        with(binding) {
            val dateFromDesc = desc.removeRange(desc.indexOf(SPACE), desc.length)
            val creationTimeText =
                    if (dateFromDesc == Calendar.getInstance().getTodayDate()) TODAY
                    else dateFromDesc

            workName.setText(work.name)
            workDescription.setText(desc.removeRange(0,desc.indexOf(SPACE).inc().inc()))
            creationDate.text = creationTimeText
            hours.setText(work.hours.toString())
            minutes.setText(work.minutes.toString())
        }
    }

    private fun setDisabledMaskTo(button: Button) {
        with(button) {
            backgroundTintList = ContextCompat.getColorStateList(this.context, R.color.disablingMask)
            setTextColor(ContextCompat.getColor(this.context, R.color.textColorLight))
            setOnClickListener { showSnackBar(R.string.all_volunteers_was_selected) }
        }
    }

    private fun validateAndSend(onSuccess: (WorkToAdd) -> Unit) {
        with(binding) {
            progressBar.visible()
            this@SummaryVolunteerActivity.hideSoftKeyboard(this.root)
            val validator = Validator(this@SummaryVolunteerActivity)
            val creationDateString = viewModel.decideAboutDate(creationDate.text.toString())

            val work = WorkToAdd(
                    workName.text.toSafeString(),
                    "$creationDateString  ${workDescription.text}".toSafeString(),
                    hours.text.toString().toIntOr(Constants.Numbers.TIME_HAS_NO_VALUE),
                    minutes.text.toString().toIntOr(Constants.Numbers.TIME_HAS_NO_VALUE)
            )

            if (!validator.validateWork(work)) {
                progressBar.invisible()
                return
            } else {
                sendWork(work, onSuccess)
            }
        }
    }

    private fun sendWork(work: WorkToAdd, onSuccess: (WorkToAdd) -> Unit) {
        viewModel.addWorkToDatabase(work)
                .subscribeBy(
                        onSuccess = {
                            resolveResult(it)
                            if (it) onSuccess(work)
                            binding.progressBar.invisible()
                        },
                        onError = { logError(it); binding.progressBar.invisible() }
                )
    }

    private fun resolveResult(result: Boolean) {
        if (result) {
            Vibrator(this).longVibrate()
            showToast(R.string.adding_work_confirmation)
        } else {
            showSnackBar(R.string.adding_work_error)
        }
    }

    private fun finishAdding() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun resetPools() {
        with(binding) {
            hours.setText(EMPTY_STRING)
            minutes.setText(EMPTY_STRING)
            workName.setText(EMPTY_STRING)
            workDescription.setText(EMPTY_STRING)
        }
    }

    private fun getShowChosenVolunteers() {
        val chosenVolunteers: List<Volunteer> = intent.getParcelableArrayListExtra(EXTRA_CHECKED_VOLUNTEERS)
                ?: arrayListOf()
        viewModel.isAllVolunteersChosen = intent.getBooleanExtra(EXTRA_IS_ALL_CHOSEN, false)
        viewModel.chosenVolunteers = chosenVolunteers
        binding.summaryActivityChosenVolunteers.text = viewModel.createReadableFromVolunteers()
    }
}