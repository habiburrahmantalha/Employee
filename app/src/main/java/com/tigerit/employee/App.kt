package com.tigerit.employee

import android.app.Application
import com.tigerit.employee.db.Database
import com.tigerit.employee.db.EmployeeRepository
import com.tigerit.employee.di.components.DaggerAppComponent

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .inject(this)
        EmployeeRepository.getInstance(Database.getInstance(this).employeeDAO)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
    }
}