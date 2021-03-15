package com.kml.views.fragments.mainFeatures

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.kml.Constants.Extras.IS_FROM_FILE_EXTRA
import com.kml.Constants.Extras.WORKS_EXTRA
import com.kml.Constants.Flags.MEETINGS
import com.kml.Constants.Flags.WORKS
import com.kml.R
import com.kml.data.utilities.FileFactory
import com.kml.databinding.FragmentWorksHistoryBinding
import com.kml.extensions.setFragmentWithData
import com.kml.models.Work
import com.kml.viewModelFactories.WorksHistoryViewModelFactory
import com.kml.viewModels.WorksHistoryViewModel
import com.kml.views.BaseFragment
import com.kml.views.fragments.AllHistoryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class WorksHistoryFragment : BaseFragment() {

    lateinit var viewModel: WorksHistoryViewModel
    lateinit var fileFactory: FileFactory

    lateinit var binding: FragmentWorksHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_works_history, container, false)

        fileFactory = FileFactory(requireContext())

        val viewModelFactory = WorksHistoryViewModelFactory(fileFactory)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WorksHistoryViewModel::class.java)

        binding.myMeetingsImage.setOnClickListener {
            showProgressBar()
            launchHistory(MEETINGS)
        }

        binding.myWorks.setOnClickListener {
            showProgressBar()
            launchHistory(WORKS)
        }

        return binding.root
    }

    private fun showProgressBar() {
        binding.allHistoryProgressBar.visibility = View.VISIBLE
    }

    private fun launchHistory(type: String) {
        CoroutineScope(IO).launch {
            val works = viewModel.getData(type)
            withContext(Main) {
                openFragmentWith(works)
                binding.allHistoryProgressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun openFragmentWith(works: List<Work>) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(WORKS_EXTRA, works as ArrayList)
        bundle.putBoolean(IS_FROM_FILE_EXTRA, viewModel.isFromFile())

        setFragmentWithData(AllHistoryFragment(), bundle)
    }
}