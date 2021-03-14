package com.kml.views.fragments.mainFeatures

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.kml.R
import com.kml.data.models.GameFilterInfo
import com.kml.views.activities.GameRecycleViewActivity

//TODO add DataBinding
class GameSearchEngineFragment : Fragment() {

    companion object {
        const val EXTRA_GAME_FILTER_INFO = "com.kml.fragments.EXTRA_GAME_FILTER_INFO"
    }

    private lateinit var rootView: View

    private lateinit var spinnerNumberOfKids: Spinner
    private lateinit var spinnerKidsAge: Spinner
    private lateinit var spinnerPlace: Spinner
    private lateinit var spinnerTypeOfGames: Spinner
    private lateinit var spinnerCategory: Spinner

    private var name: String = ""
    private var numberOfKids: String = ""
    private var kidsAge: String = ""
    private var place: String = ""
    private var typeOfGames: String = ""
    private var category: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_form_animacje, container, false)
        createSpinnerInstances()
        val button = rootView.findViewById<Button>(R.id.btn_search)
        button.setOnClickListener {
            setFormInfo()
            val filterInfo = GameFilterInfo(name, numberOfKids, kidsAge, place, typeOfGames, category)
            val intent = Intent(context, GameRecycleViewActivity::class.java)
            intent.putExtra(EXTRA_GAME_FILTER_INFO, filterInfo)
            startActivity(intent)
        }
        return rootView
    }

    private fun setFormInfo() {
        val editTextName = rootView.findViewById<EditText>(R.id.form_edit_text_name)
        name = editTextName.text.toString()
        numberOfKids = spinnerNumberOfKids.selectedItem.toString()
        kidsAge = spinnerKidsAge.selectedItem.toString()
        place = spinnerPlace.selectedItem.toString()
        typeOfGames = spinnerTypeOfGames.selectedItem.toString()
        category = spinnerCategory.selectedItem.toString()
    }

    private fun createSpinnerInstances() {
        spinnerNumberOfKids = rootView.findViewById(R.id.form_spinner_number_of_kids)
        spinnerKidsAge = rootView.findViewById(R.id.form_spinner_kids_age)
        spinnerPlace = rootView.findViewById(R.id.form_spinner_place)
        spinnerTypeOfGames = rootView.findViewById(R.id.form_spinner_type_of_games)
        spinnerCategory = rootView.findViewById(R.id.form_spinner_category)
        val spinners = arrayOf(spinnerNumberOfKids, spinnerKidsAge, spinnerPlace, spinnerTypeOfGames, spinnerCategory)
        val stringArrayIds = intArrayOf(R.array.number_of_kids_options, R.array.kids_age_options, R.array.place_options, R.array.type_of_games_options, R.array.category_options)
        for (i in 0..4) {
            setSingleSpinnerInstance(stringArrayIds[i], spinners[i])
        }
    }

    private fun setSingleSpinnerInstance(arrayResource: Int, spinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(requireContext(),
                arrayResource, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(0)
        changeSelectedTextColor(spinner)

    }

    private fun changeSelectedTextColor(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (spinner.selectedView as TextView).setTextColor(resources.getColor(R.color.textColorLight))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}