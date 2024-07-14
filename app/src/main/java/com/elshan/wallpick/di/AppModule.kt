package com.elshan.wallpick.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.elshan.wallpick.main.data.local.WallpaperDatabase
import com.elshan.wallpick.main.data.repository.MainRepositoryImpl
import com.elshan.wallpick.notifications.NotificationsService
import com.elshan.wallpick.notifications.NotificationsServiceImpl
import com.elshan.wallpick.presentation.screen.login.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesMainRepository(
        firestore: FirebaseFirestore,
        wallpaperDb: WallpaperDatabase
    ) = MainRepositoryImpl(firestore, wallpaperDb)

    @Provides
    @Singleton
    fun providesWallpaperDatabase(app: Application): WallpaperDatabase {
        return Room.databaseBuilder(
            app,
            WallpaperDatabase::class.java,
            "wallpaper_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationsService = NotificationsServiceImpl(context = context)

    @Provides
    @Singleton
    fun provideSignInClient(
        @ApplicationContext context: Context
    ): SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context,
        oneTapClient: SignInClient
    ): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context),
        )
    }
}