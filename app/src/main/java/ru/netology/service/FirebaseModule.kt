package ru.netology.service

import android.content.Context
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.netology.R
import ru.netology.activity.AppActivity
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Singleton
    @Provides
    fun provideCheckGoogleApiAvailability(
        @ApplicationContext context: Context,
        activity: AppActivity
    ) {
        with(provideGoogleApi()) {
            val code = isGooglePlayServicesAvailable(context)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(activity, code, 9000)?.show()
                return
            }
            Toast.makeText(context, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }
    }


    @Singleton
    @Provides
    fun provideGoogleApi(): GoogleApiAvailability = GoogleApiAvailability.getInstance()

    @Singleton
    @Provides
    fun provideFirebase(): FirebaseMessaging = FirebaseMessaging.getInstance()
}