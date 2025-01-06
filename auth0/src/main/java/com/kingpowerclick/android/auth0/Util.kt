package com.kingpowerclick.android.auth0

import android.content.Context
import android.content.pm.PackageManager

object Util {
    fun getMetadataValue(
        key: String,
        context: Context,
    ): String? =
        try {
            val applicationInfo =
                context.packageManager.getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA,
                )
            val metaData = applicationInfo.metaData
            metaData?.getString(key) // Get the value by key
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
}