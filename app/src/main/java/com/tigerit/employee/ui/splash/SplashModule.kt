package com.tigerit.employee.ui.splash

import dagger.Module
import dagger.Provides
import com.tigerit.employee.di.InjectionViewModelProvider
import com.tigerit.employee.di.qualifiers.ViewModelInjection

@Module
class SplashModule {

    @Provides
    @ViewModelInjection
    fun provideSplashVM(
        activity: SplashActivity,
        viewModelProvider: InjectionViewModelProvider<SplashVM>
    ) = viewModelProvider.get(activity, SplashVM::class)
}