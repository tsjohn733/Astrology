package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "birth_profiles")
data class BirthProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val cityName: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double,
    val isPrimary: Boolean = false,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
