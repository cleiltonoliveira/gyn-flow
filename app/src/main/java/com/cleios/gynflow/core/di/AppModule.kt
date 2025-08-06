package com.cleios.gynflow.core.di

import com.cleios.gynflow.core.auth.CustomAuthService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

//    @Provides
//    @Singleton
//    fun provideCustomAuthService(auth: FirebaseAuth): CustomAuthService {
//        return CustomAuthService(auth)
//    }
}