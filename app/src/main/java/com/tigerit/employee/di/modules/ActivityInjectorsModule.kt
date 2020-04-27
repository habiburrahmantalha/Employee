package com.tigerit.employee.di.modules

import com.tigerit.employee.ui.splash.SplashActivity
import com.tigerit.employee.ui.splash.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjectorsModule {

    @ContributesAndroidInjector(modules = [SplashModule::class])
    abstract fun splashActivityInjector(): SplashActivity

}