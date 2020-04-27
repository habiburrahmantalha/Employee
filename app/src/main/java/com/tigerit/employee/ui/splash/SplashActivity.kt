package com.tigerit.employee.ui.splash

import android.content.Intent
import javax.inject.Inject
import android.os.Bundle
import com.tigerit.employee.ui.home.MainActivity
import com.tigerit.employee.R
import com.tigerit.employee.base.BaseActivity
import com.tigerit.employee.di.ViewModelInjectionField
import com.tigerit.employee.di.qualifiers.ViewModelInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {

    @Inject
    @ViewModelInjection
    lateinit var viewModel: ViewModelInjectionField<SplashVM>

    override fun layoutRes() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GlobalScope.launch(context = Dispatchers.Main) {
            delay(500)
            navigateToHome()
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}