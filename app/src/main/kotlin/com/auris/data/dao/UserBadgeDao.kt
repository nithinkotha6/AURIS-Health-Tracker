package com.auris.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.auris.data.entity.UserBadgeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBadgeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(badge: UserBadgeEntity)

    @Query("SELECT * FROM user_badges ORDER BY earned_at DESC")
    fun getAll(): Flow<List<UserBadgeEntity>>
}
