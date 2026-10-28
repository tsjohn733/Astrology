package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthProfileDao {
    @Query("SELECT * FROM birth_profiles ORDER BY isPrimary DESC, createdAt DESC")
    fun getAllProfiles(): Flow<List<BirthProfileEntity>>

    @Query("SELECT * FROM birth_profiles WHERE isPrimary = 1 LIMIT 1")
    suspend fun getPrimaryProfile(): BirthProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: BirthProfileEntity): Long

    @Update
    suspend fun updateProfile(profile: BirthProfileEntity)

    @Delete
    suspend fun deleteProfile(profile: BirthProfileEntity)

    @Query("UPDATE birth_profiles SET isPrimary = 0")
    suspend fun clearPrimaryFlags()

    @Transaction
    suspend fun setAsPrimary(id: Long) {
        clearPrimaryFlags()
        setPrimaryFlag(id)
    }

    @Query("UPDATE birth_profiles SET isPrimary = 1 WHERE id = :id")
    suspend fun setPrimaryFlag(id: Long)
}
