package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.DI.PlayerModule
import com.example.playlistmaker.DI.SearchModule
import com.example.playlistmaker.DI.SettingModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {

        super.onCreate()
        startKoin {
            androidLogger(org.koin.core.logger.Level.DEBUG)
            androidContext(this@App)
            modules(
                SettingModule,
                SearchModule,
                PlayerModule
            )
        }
    }
}



