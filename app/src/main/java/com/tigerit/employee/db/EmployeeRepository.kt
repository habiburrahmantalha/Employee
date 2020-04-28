package com.tigerit.employee.db

import com.tigerit.employee.ui.home.Employee

class EmployeeRepository(private val employeeDAO: EmployeeDAO) {

    fun addEmployee(employee: Employee) = employeeDAO.addEmployee(employee)

    fun getEmployees() = employeeDAO.getEmployees()

    fun updateEmployee(employee: Employee) = employeeDAO.updateEmployee(employee)

    fun deleteEmployee(employee: Employee) = employeeDAO.deleteEmployee(employee)

    companion object {
        @Volatile
        private var instance: EmployeeRepository? = null

        fun getInstance(employeeDAO: EmployeeDAO) = instance ?: synchronized(this) {
            instance ?: EmployeeRepository(employeeDAO).also { instance = it }
        }

        fun getInstance(): EmployeeRepository {
            return instance!!
        }
    }

}