package com.kml.holders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kml.R

class VolunteerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var checkBox: CheckBox = itemView.findViewById(R.id.row_user_checkbox)

    @JvmField
    var name: TextView = itemView.findViewById(R.id.row_user_name)

}