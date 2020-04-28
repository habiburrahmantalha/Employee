package com.tigerit.employee.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tigerit.employee.ui.home.Employee

const val DB_NAME = "employeeDB"
const val TABLE_NAME = "employeeTable"
const val COL_ID = "id"
const val COL_NAME = "name"
const val COL_AGE = "age"
const val COL_GENDER = "gender"
const val COL_PHOTO = "photo"

class EmployeeDAO(context: Context): SQLiteOpenHelper(context, DB_NAME, null, 1){



    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME VARCHAR(256), $COL_AGE INTEGER,$COL_GENDER VARCHAR(256), $COL_PHOTO VARCHAR(2056));"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addEmployee(employee: Employee) {
        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_NAME, employee.name)
        contentValues.put(COL_AGE, employee.age)
        contentValues.put(COL_GENDER, employee.gender)
        contentValues.put(COL_PHOTO, employee.photo)

        db.insert(TABLE_NAME, null, contentValues)
    }

    fun updateEmployee(employee: Employee) {
        val db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(COL_NAME, employee.name)
        contentValues.put(COL_AGE, employee.age)
        contentValues.put(COL_GENDER, employee.gender)
        contentValues.put(COL_PHOTO, employee.photo)
        db.update(TABLE_NAME, contentValues, COL_ID+"="+employee.id, null )
    }

    fun deleteEmployee(employee: Employee) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, COL_ID+"="+employee.id, null )
    }


    fun getEmployees(): MutableList<Employee> {
        val list: MutableList<Employee> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME;"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        if(result.count > 0)
        do{
            // var employee = Employee()
            val id = result.getString(result.getColumnIndex(COL_ID)).toInt()
            val name  = result.getString(result.getColumnIndex(COL_NAME))
            val age = result.getString(result.getColumnIndex(COL_AGE)).toInt()
            val gender  = result.getString(result.getColumnIndex(COL_GENDER))
            val photo = result.getString(result.getColumnIndex(COL_PHOTO))

            list.add(Employee(id, name, age, gender, photo))
        }while(result.moveToNext())

        result.close()
        db.close()

        return list
    }


}