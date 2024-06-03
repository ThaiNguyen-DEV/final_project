package com.app.shopfee.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.app.shopfee.R
import com.app.shopfee.prefs.DataStoreManager
import com.app.shopfee.utils.GlobalFunction
import com.app.shopfee.utils.StringUtil

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ goToActivity() }, 2000)
    }

    private fun goToActivity() {
        if (DataStoreManager.user != null
            && !StringUtil.isEmpty(DataStoreManager.user!!.email)
        ) {
            GlobalFunction.startActivity(this, MainActivity::class.java)
        } else {
            GlobalFunction.startActivity(this, LoginActivity::class.java)
        }
        finish()
    }
}