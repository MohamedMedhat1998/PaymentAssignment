package com.zeal.paymentassignment.di

import android.content.ContentResolver
import com.zeal.paymentassignment.data.LoyaltyRepo
import com.zeal.paymentassignment.data.LoyaltyRepoImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    factory<ContentResolver> {
        androidApplication().contentResolver
    }
    factory<LoyaltyRepo> {
        LoyaltyRepoImpl(get())
    }
}