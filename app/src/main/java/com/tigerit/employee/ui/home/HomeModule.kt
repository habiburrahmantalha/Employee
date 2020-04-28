package com.tigerit.employee.ui.home

import dagger.Module
import dagger.Provides
import com.tigerit.employee.di.InjectionViewModelProvider
import com.tigerit.employee.di.qualifiers.ViewModelInjection

@Module
class HomeModule {

    @Provides
    @ViewModelInjection
    fun provideHomeVM(
        activity: MainActivity,
        viewModelProvider: InjectionViewModelProvider<HomeVM>
    ) = viewModelProvider.get(activity, HomeVM::class)
}