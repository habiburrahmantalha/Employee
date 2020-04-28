package com.tigerit.employee.ui.add_employee

import com.tigerit.employee.App
import com.tigerit.employee.db.Database
import dagger.Module
import dagger.Provides
import com.tigerit.employee.di.qualifiers.ViewModelInjection
import com.tigerit.employee.di.InjectionViewModelProvider

@Module
class AddEmployeeModule {

    @Provides
    @ViewModelInjection
    fun provideAddEmployeeVM(
        activity: AddEmployeeActivity,
        viewModelProvider: InjectionViewModelProvider<AddEmployeeVM>
    ) = viewModelProvider.get(activity, AddEmployeeVM::class)
}