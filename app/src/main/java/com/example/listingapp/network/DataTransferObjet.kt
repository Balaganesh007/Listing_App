package com.example.listingapp.network

import android.telephony.TelephonyCallback
import com.example.listingapp.database.UsersModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

fun NetworkContainer.asDatabaseModel(): Array<UsersModel> {
    return results.map {
            UsersModel (
                first = it.name.first,
                last = it.name.last,
                gender = it.gender,
                email = it.email,
                phone = it.phone,
                age = it.dob.age,
                userImg = it.picture.userImg,
                latitude = it.location.coordinates.latitude,
                longitude = it.location.coordinates.longitude,
                country = it.location.country,
                city = it.location.city,
                page = info.page,
                results = info.results
            )
    }.toTypedArray()
}

@JsonClass(generateAdapter = true)
data class NetworkContainer(val results : List<UserProperty>,val info : Info)

@JsonClass(generateAdapter = true)
data class UserProperty(
    val name : Name ,

    val gender : String,
    val email : String,

    val location : Place,

    val phone : String,

    val dob : Dob,

    val picture : Image
)
data class Name(
    val first : String,
    val last : String,
)
data class Dob(
    val age : String,
)
data class Image(
    @Json(name = "medium")
    val userImg : String
)
data class Place(
    val city : String,
    val country : String,
    val coordinates : Loc
)
data class Loc(
    val latitude : String,
    val longitude : String
)
data class Info(
    val results: String,
    val page : String
)
