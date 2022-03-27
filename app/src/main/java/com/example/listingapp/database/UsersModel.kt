package com.example.listingapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.listingapp.domain.UserDataModel


@Entity
data class UsersModel(

    val first : String,
    val last : String,

    val gender : String,
    val email : String,
    @PrimaryKey
    val phone : String,

    val age : String,

    val userImg : String,

    val latitude : String,
    val longitude : String,
    val city : String,
    val country : String,
    val results: String,
    val page : String
)

fun List<UsersModel>.asDomainModel(): List<UserDataModel> {
    return map {
        UserDataModel(
            first = it.first,
            last = it.last,
            gender = it.gender,
            email = it.email,
            phone = it.phone,
            age = it.age,
            userImg = it.userImg,
            longitude = it.longitude,
            latitude = it.latitude,
            country = it.country,
            city = it.city,
            page = it.page,
            results = it.results
        )
    }
}
