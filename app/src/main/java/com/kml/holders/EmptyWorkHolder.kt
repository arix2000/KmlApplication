package com.kml.holders

import android.view.View
import com.kml.extensions.invisible
import com.kml.models.Work

class EmptyWorkHolder(itemView: View): WorkHolder(itemView) {
    override fun bind(work: Work, onClickListener: (Work) -> Unit) {
        itemView.invisible()
    }
}