package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class ProfilePhotoStorage(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "profile_photo", 
        Context.MODE_PRIVATE
    )
    
    fun saveProfilePhoto(bitmap: Bitmap): Boolean {
        return try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            
            sharedPreferences.edit()
                .putString("profile_photo", encodedString)
                .apply()
            
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun getProfilePhoto(): Bitmap? {
        return try {
            val encodedString = sharedPreferences.getString("profile_photo", null)
            if (encodedString != null) {
                val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    fun hasProfilePhoto(): Boolean {
        return sharedPreferences.contains("profile_photo")
    }
    
    fun clearProfilePhoto() {
        sharedPreferences.edit().remove("profile_photo").apply()
    }
}
