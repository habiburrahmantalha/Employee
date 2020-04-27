package com.tigerit.employee.di.modules
import com.tigerit.employee.ui.splash.SplashVM
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [
//    NetworkModule::class
//    UtilsModule::class
])
interface ViewModelInjectorModule {

    fun inject(splashVM: SplashVM)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjectorModule
//        fun networkModule(networkModule: NetworkModule): Builder
//        fun utilsModule(utilsModule: UtilsModule): Builder
    }
}