package com.tigerit.employee.ui.home

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tigerit.employee.base.BaseViewModel
import com.tigerit.employee.db.EmployeeRepository
import java.io.*
import java.nio.channels.FileChannel
import javax.inject.Inject


class HomeVM @Inject constructor() : BaseViewModel() {

    val employeeListLiveData: MutableLiveData<MutableList<Employee>> = MutableLiveData()
    val employeeLiveData: MutableLiveData<Employee> = MutableLiveData()
    val fileResponseLiveData: MutableLiveData<String> = MutableLiveData()

    fun loadData() {
        val list = EmployeeRepository.getInstance().getEmployees()
        Log.d("VM", list.toString())
        employeeListLiveData.postValue(list);
    }

    fun onItemEdit(employee: Employee) {
        employeeLiveData.postValue(employee)
    }
    fun onItemDelete(employee: Employee) {
        EmployeeRepository.getInstance().deleteEmployee(employee)
        loadData()
    }

    fun exportDB(path: File?) {
        Log.d("VM",path.toString())
        val data: File = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath =
            "/data/com.tigerit.employee/databases/employeeDB"
        val backupDBPath: String = "employeeDB"
        val currentDB = File(data, currentDBPath)
        val backupDB = File(path, backupDBPath)
        try {
            source = FileInputStream(currentDB).getChannel()
            destination = FileOutputStream(backupDB).getChannel()
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            Log.d("VM","Exported"+path)
            fileResponseLiveData.postValue("DB exported to Download Folder")
            //Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            fileResponseLiveData.postValue("DB exported failed")
        }
    }
    fun importDB(uri: String) {
        try {
            Log.d("MV", uri)

            val data = Environment.getDataDirectory()

            val currentDBPath =
                "/data/com.tigerit.employee/databases/employeeDB"
            val backupDBPath = "employeeDB"
            val backupDB = File(data, currentDBPath)
            val currentDB = File(uri.replace(backupDBPath,""), backupDBPath)


            val src =
                FileInputStream(currentDB).channel
            val dst =
                FileOutputStream(backupDB).channel
            dst.transferFrom(src, 0, src.size())
            src.close()
            dst.close()
            Log.d("VM","imported")
            fileResponseLiveData.postValue("DB imported")
            loadData()

        } catch (e: Exception) {
            Log.d("VM","imported Failed"+e.localizedMessage)
            fileResponseLiveData.postValue("DB imported Failed")
        }
    }
}