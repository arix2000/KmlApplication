package com.kml.views.fragments.mainFeatures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.kml.R
import com.kml.databinding.FragmentGameSearchEngineBinding
import com.kml.models.GameFilterInfo
import com.kml.views.BaseFragment
import com.kml.views.activities.GameRecycleViewActivity

class GameSearchEngineFragment : BaseFragment() {

    companion object {
        const val EXTRA_GAME_FILTER_INFO = "com.kml.fragments.EXTRA_GAME_FILTER_INFO"
    }
    private var _binding: FragmentGameSearchEngineBinding? = null
    private val binding get() = _binding!!

    private var name: String = ""
    private var numberOfKids: String = ""
    private var kidsAge: String = ""
    private var place: String = ""
    private var typeOfGames: String = ""
    private var category: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGameSearchEngineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSearch.setOnClickListener {
            setFormInfo()
            val filterInfo = GameFilterInfo(name, numberOfKids, kidsAge, place, typeOfGames, category)
            val intent = Intent(context, GameRecycleViewActivity::class.java)
            intent.putExtra(EXTRA_GAME_FILTER_INFO, filterInfo)
            startActivity(intent)
        }
        createSpinnerInstances()
    }

    private fun setFormInfo() {
        binding.apply {
            name = binding.formEditTextName.text.toString()
            numberOfKids = formSpinnerNumberOfKids.selectedItem.toString()
            kidsAge = formSpinnerKidsAge.selectedItem.toString()
            place = formSpinnerPlace.selectedItem.toString()
            typeOfGames = formSpinnerTypeOfGames.selectedItem.toString()
            category = formSpinnerCategory.selectedItem.toString()
        }
    }

    private fun createSpinnerInstances() {
        binding.apply {
            val spinners = arrayOf(formSpinnerNumberOfKids, formSpinnerKidsAge, formSpinnerPlace, formSpinnerTypeOfGames, formSpinnerCategory)
            val stringArrayIds = intArrayOf(R.array.number_of_kids_options, R.array.kids_age_options, R.array.place_options, R.array.type_of_games_options, R.array.category_options)
            for (i in 0..4) {
                setSingleSpinnerInstance(stringArrayIds[i], spinners[i])
            }
        }
    }

    private fun setSingleSpinnerInstance(arrayResource: Int, spinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(requireContext(),
                arrayResource, R.layout.view_simple_spinner)
        adapter.setDropDownViewResource(R.layout.view_simple_dropdown)
        spinner.adapter = adapter
        spinner.setSelection(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}