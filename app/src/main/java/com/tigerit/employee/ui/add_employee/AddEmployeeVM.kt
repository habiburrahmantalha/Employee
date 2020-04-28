package com.tigerit.employee.ui.add_employee

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import com.tigerit.employee.base.BaseViewModel
import com.tigerit.employee.db.Database
import com.tigerit.employee.db.EmployeeRepository
import com.tigerit.employee.ui.home.Employee

class AddEmployeeVM @Inject constructor() : BaseViewModel() {


    val imagePathLiveData: MutableLiveData<String>  = MutableLiveData()
    val genderLiveData: MutableLiveData<String>  = MutableLiveData()
    val addedLiveData: MutableLiveData<Boolean> = MutableLiveData()


    fun addEmployee(name: String, age: String) {
        EmployeeRepository.getInstance().addEmployee(Employee(null, name, age.toInt(),genderLiveData.value!!, imagePathLiveData.value))
        addedLiveData.postValue(true)
    }

    fun updateEmployee(id: Int, name: String, age: String) {
        EmployeeRepository.getInstance().updateEmployee(Employee(id, name, age.toInt(),genderLiveData.value!!, imagePathLiveData.value))
        addedLiveData.postValue(true)

    }


}