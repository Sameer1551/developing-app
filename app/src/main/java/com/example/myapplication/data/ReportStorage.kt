package com.example.myapplication.data

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.models.WaterQualityReport
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ReportStorage(context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "water_quality_reports", 
        Context.MODE_PRIVATE
    )
    private val gson = Gson()
    
    fun saveReport(report: WaterQualityReport): Long {
        val reports = getAllReports().toMutableList()
        val newId = if (reports.isEmpty()) 1L else reports.maxOf { it.id } + 1
        val reportWithId = report.copy(id = newId)
        reports.add(reportWithId)
        
        val reportsJson = gson.toJson(reports)
        sharedPreferences.edit().putString("reports", reportsJson).apply()
        
        return newId
    }
    
    fun getAllReports(): List<WaterQualityReport> {
        val reportsJson = sharedPreferences.getString("reports", "[]")
        val type = object : TypeToken<List<WaterQualityReport>>() {}.type
        return try {
            gson.fromJson(reportsJson, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getReportById(reportId: Long): WaterQualityReport? {
        return getAllReports().find { it.id == reportId }
    }
    
    fun getReportsByStatus(status: String): List<WaterQualityReport> {
        return getAllReports().filter { it.status == status }
    }
    
    fun updateReport(report: WaterQualityReport) {
        val reports = getAllReports().toMutableList()
        val index = reports.indexOfFirst { it.id == report.id }
        if (index != -1) {
            reports[index] = report
            val reportsJson = gson.toJson(reports)
            sharedPreferences.edit().putString("reports", reportsJson).apply()
        }
    }
    
    fun deleteReport(report: WaterQualityReport) {
        val reports = getAllReports().toMutableList()
        reports.removeAll { it.id == report.id }
        val reportsJson = gson.toJson(reports)
        sharedPreferences.edit().putString("reports", reportsJson).apply()
    }
    
    fun getTotalReportCount(): Int {
        return getAllReports().size
    }
    
    fun getReportCountByStatus(status: String): Int {
        return getReportsByStatus(status).size
    }
}
