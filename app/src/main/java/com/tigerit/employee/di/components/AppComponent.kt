package com.tigerit.employee.di.components

import android.content.Context
import com.tigerit.employee.App
import com.tigerit.employee.di.modules.ActivityInjectorsModule
import com.tigerit.employee.di.modules.AppModule
import com.tigerit.employee.di.modules.FragmentInjectorsModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityInjectorsModule::class,
        FragmentInjectorsModule::class,
//        NetworkModule::class,
        AppModule::class
        //UtilsModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
    fun inject(context: Context)

}