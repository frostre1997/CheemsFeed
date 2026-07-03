package com.frostre1997.cheemsfeed.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: SavedPost)

    @Delete
    suspend fun delete(post: SavedPost)

    @Query("SELECT * FROM saved_posts ORDER BY savedAt DESC")
    fun getAllSavedPosts(): Flow<List<SavedPost>>

    @Query("SELECT * FROM saved_posts WHERE id = :postId")
    suspend fun getPostById(postId: String): SavedPost?

    @Query("DELETE FROM saved_posts WHERE id = :postId")
    suspend fun deleteById(postId: String)

    @Query("SELECT COUNT(*) FROM saved_posts")
    fun getSavedPostCount(): Flow<Int>
}

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: UserProfile)

    @Query("SELECT * FROM user_profiles WHERE username = :username")
    suspend fun getUserProfile(username: String): UserProfile?

    @Query("SELECT * FROM user_profiles")
    fun getAllProfiles(): Flow<List<UserProfile>>

    @Delete
    suspend fun delete(profile: UserProfile)

    @Query("DELETE FROM user_profiles")
    suspend fun deleteAllProfiles()
}
