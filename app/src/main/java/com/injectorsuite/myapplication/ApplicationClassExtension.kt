package com.injectorsuite.myapplication

import android.app.Application

class ApplicationClassExtension: Application() {
    override fun onCreate(){
        super.onCreate()
        KeyProviderSingleInstance.registerNewKey("GiantBomb",getString(R.string.API_KEY1))
    }
}