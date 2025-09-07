package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.models.AlertData
import com.example.myapplication.models.AlertDataResponse
import com.example.myapplication.models.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException

class AlertDataLoader(private val context: Context) {
    
    suspend fun loadAlertData(): Result<AlertDataResponse> = withContext(Dispatchers.IO) {
        try {
            val jsonString = loadJsonFromAssets("alerts_data.json")
            val jsonObject = JSONObject(jsonString)
            
            val alerts = parseAlerts(jsonObject.getJSONArray("alerts"))
            val usersByDistrict = parseUsersByDistrict(jsonObject.getJSONObject("usersByDistrict"))
            
            Result.success(AlertDataResponse(alerts, usersByDistrict))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun loadJsonFromAssets(fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: IOException) {
            throw Exception("Could not load $fileName from assets", e)
        }
    }
    
    private fun parseAlerts(alertsArray: org.json.JSONArray): List<AlertData> {
        val alerts = mutableListOf<AlertData>()
        
        for (i in 0 until alertsArray.length()) {
            val alertJson = alertsArray.getJSONObject(i)
            
            val notes = mutableListOf<String>()
            val notesArray = alertJson.optJSONArray("notes")
            if (notesArray != null) {
                for (j in 0 until notesArray.length()) {
                    notes.add(notesArray.getString(j))
                }
            }
            
            val attachments = mutableListOf<String>()
            val attachmentsArray = alertJson.optJSONArray("attachments")
            if (attachmentsArray != null) {
                for (j in 0 until attachmentsArray.length()) {
                    attachments.add(attachmentsArray.getString(j))
                }
            }
            
            val alert = AlertData(
                id = alertJson.getString("id"),
                title = alertJson.getString("title"),
                description = alertJson.getString("description"),
                type = alertJson.getString("type"),
                priority = alertJson.getString("priority"),
                status = alertJson.getString("status"),
                district = alertJson.getString("district"),
                location = alertJson.getString("location"),
                reporter = alertJson.getString("reporter"),
                reporterContact = alertJson.getString("reporterContact"),
                createdAt = alertJson.getString("createdAt"),
                updatedAt = alertJson.getString("updatedAt"),
                assignedTo = alertJson.getString("assignedTo"),
                responseTime = alertJson.getString("responseTime"),
                notes = notes,
                attachments = attachments
            )
            
            alerts.add(alert)
        }
        
        return alerts
    }
    
    private fun parseUsersByDistrict(usersObject: JSONObject): Map<String, List<UserData>> {
        val usersByDistrict = mutableMapOf<String, List<UserData>>()
        
        val keys = usersObject.keys()
        while (keys.hasNext()) {
            val district = keys.next()
            val usersArray = usersObject.getJSONArray(district)
            val users = mutableListOf<UserData>()
            
            for (i in 0 until usersArray.length()) {
                val userJson = usersArray.getJSONObject(i)
                
                val user = UserData(
                    id = userJson.getString("id"),
                    name = userJson.getString("name"),
                    email = userJson.getString("email"),
                    phone = userJson.getString("phone"),
                    role = userJson.getString("role"),
                    status = userJson.getString("status"),
                    state = userJson.getString("state"),
                    originalRole = userJson.getString("originalRole")
                )
                
                users.add(user)
            }
            
            usersByDistrict[district] = users
        }
        
        return usersByDistrict
    }
}

