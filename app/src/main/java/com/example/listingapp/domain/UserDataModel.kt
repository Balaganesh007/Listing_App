package com.example.listingapp.domain

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDataModel(
    val first: String,
    val last: String,

    val gender: String,
    val email: String,
    @PrimaryKey
    val phone: String,

    val age: String,

    val userImg: String,

    val latitude : String,
    val longitude : String,
    val city : String,
    val country : String,
    val results: String,
    val page : String
    ) : Parcelable