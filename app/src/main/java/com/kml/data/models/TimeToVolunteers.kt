package com.kml.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TimeToVolunteers(var id: Int,
                       var hours: String,
                       var minutes: String,
                       var volunteers: List<Volunteer>) : Parcelable