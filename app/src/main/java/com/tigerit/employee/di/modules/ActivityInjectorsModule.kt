package com.tigerit.employee.di.modules

import com.tigerit.employee.ui.add_employee.AddEmployeeActivity
import com.tigerit.employee.ui.add_employee.AddEmployeeModule
import com.tigerit.employee.ui.home.HomeModule
import com.tigerit.employee.ui.home.MainActivity
import com.tigerit.employee.ui.splash.SplashActivity
import com.tigerit.employee.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectorsModule {

    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun splashActivityInjector(): SplashActivity

    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @ContributesAndroidInjector(modules = [AddEmployeeModule::class])
    abstract fun addEmployeeActivityInjector(): AddEmployeeActivity
}