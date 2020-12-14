package com.kml.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TimeToVolunteers(val id: Int, val hours:String, val minutes:String, var volunteers: List<Volunteer>): Parcelable