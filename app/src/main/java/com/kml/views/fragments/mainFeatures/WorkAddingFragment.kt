package com.kml.views.fragments.mainFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.kml.Constants
import com.kml.Constants.Strings.TODAY
import com.kml.R
import com.kml.data.utilities.FileFactory
import com.kml.data.utilities.Validator
import com.kml.data.utilities.Vibrator
import com.kml.databinding.FragmentAddingWorkBinding
import com.kml.extensions.clearPools
import com.kml.extensions.hideSoftKeyboard
import com.kml.extensions.showSnackBar
import com.kml.models.WorkToAdd
import com.kml.viewModelFactories.WorkTimerViewModelFactory
import com.kml.viewModels.WorkAddingViewModel
import com.kml.views.BaseFragment
import com.kml.views.dialogs.MyDatePickerDialog

class WorkAddingFragment : BaseFragment() {

    private var _binding: FragmentAddingWorkBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: WorkAddingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddingWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = WorkTimerViewModelFactory(FileFactory(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorkAddingViewModel::class.java)

        setupUi()
    }

    private fun setupUi() {
        with(binding) {
            workCreationDate.text = Constants.Strings.TODAY

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
                            ?: Constants.Numbers.TIME_HAS_NO_VALUE,
                    minutes.text.toString().toIntOrNull()
                            ?: Constants.Numbers.TIME_HAS_NO_VALUE
            )
            sendWork(work)
        }
    }

    private fun sendWork(work: WorkToAdd) {
        if (!Validator(requireActivity()).validateWork(work))
            return

        binding.worksProgressBar.visibility = View.VISIBLE
        viewModel.sendWorkToDatabase(work) {
            binding.worksProgressBar.visibility = View.GONE
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