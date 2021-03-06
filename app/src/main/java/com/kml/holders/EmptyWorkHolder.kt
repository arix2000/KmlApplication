package com.kml.holders

import android.view.View
import com.kml.extensions.invisible
import com.kml.models.dto.Work
import com.kml.models.model.User

class EmptyWorkHolder(itemView: View): WorkHolder(itemView) {
    override fun bind(work: Work, onClickListener: (Work) -> Unit, user: User) {
        itemView.invisible()
    }
}