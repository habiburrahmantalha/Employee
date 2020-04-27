package com.tigerit.employee.base

import androidx.lifecycle.ViewModel
import com.tigerit.employee.di.modules.DaggerViewModelInjectorModule
import com.tigerit.employee.di.modules.ViewModelInjectorModule

abstract class BaseViewModel() : ViewModel() {
   // val compositeDisposable = CompositeDisposable()
   private val injector: ViewModelInjectorModule = DaggerViewModelInjectorModule
       .builder()
       //.networkModule(NetworkModule)
       //.utilsModule(UtilsModule)
       .build()

    init {
        inject()
    }
    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            //is AuthenticationVM -> injector.inject(this)
        }
    }
}