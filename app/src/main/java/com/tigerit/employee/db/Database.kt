package com.tigerit.employee.db

import android.content.Context

class Database(context: Context) {

    var employeeDAO = EmployeeDAO(context)

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Database(context).also { instance = it }
        }
    }
}