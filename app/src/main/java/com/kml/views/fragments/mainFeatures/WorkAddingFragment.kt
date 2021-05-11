package com.kml.views.fragments.mainFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kml.Constants.Numbers.TIME_HAS_NO_VALUE
import com.kml.Constants.Strings.TODAY
import com.kml.R
import com.kml.data.utilities.Validator
import com.kml.data.utilities.Vibrator
import com.kml.databinding.FragmentAddingWorkBinding
import com.kml.extensions.*
import com.kml.models.WorkToAdd
import com.kml.viewModels.WorkAddingViewModel
import com.kml.views.BaseFragment
import com.kml.views.dialogs.MyDatePickerDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class WorkAddingFragment : BaseFragment() {

    private var _binding: FragmentAddingWorkBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WorkAddingViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddingWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachProgressBar(binding.worksProgressBar)
        setupUi()
    }

    private fun setupUi() {
        with(binding) {
            workCreationDate.text = TODAY
            dialogTimerAddInstant.setOnClickListener {
                getSendWork()
            }

            workCreationDate.setOnClickListener {
                showCalendarDialog()
            }
        }
    }

    private fun showCalendarDialog() {
        MyDatePickerDialog().run {
            setOnResultListener {
                binding.workCreationDate.text = it
            }
            show(this@WorkAddingFragment.parentFragmentManager, "DatePicker")
        }
    }

    private fun getSendWork() {
        with(binding) {
            requireContext().hideSoftKeyboard(root)

            val creationDateString = viewModel.decideAboutDate(workCreationDate.text.toString())
            val description = " $creationDateString " + workDescription.text.toString()
            val work = WorkToAdd(workName.text.toString(),
                    description,
                    hours.text.toString().toIntOrNull()
                            ?: TIME_HAS_NO_VALUE,
                    minutes.text.toString().toIntOrNull()
                            ?: TIME_HAS_NO_VALUE
            )
            sendWork(work)
        }
    }

    private fun sendWork(work: WorkToAdd) {
        if (!Validator(requireActivity()).validateWork(work))
            return

        binding.worksProgressBar.visible()
        viewModel.sendWorkToDatabase(work) {
            binding.worksProgressBar.invisible()
            if (it) {
                showSnackBar(getString(R.string.adding_work_confirmation))
                resetPools()
                Vibrator(requireContext()).longVibrate()
            } else showSnackBar(R.string.adding_work_error)
        }
    }

    private fun resetPools() {
        with(binding) {
            clearPools(workName, workDescription, hours, minutes)
            workCreationDate.text = TODAY
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}