package com.visualstudio.m3l3m01t.apspot

import android.content.*
import android.net.wifi.*
import android.util.Log

object ApManager {

    //check whether wifi hotspot on or off
    fun isApOn(context: Context, wifiManager: WifiManager): Boolean {
        try {
            val method = wifiManager.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.isAccessible = true
            val enabled = method.invoke(wifiManager) as Boolean

            Log.d("ApManager", "WiFi enable state: $enabled")
        } catch (ignored: Throwable) {

        }

        return false
    }

    fun getWifiApConfiguration(wifimanager: WifiManager): WifiConfiguration? {
        try {
            val wifiApConfigurationMethod = wifimanager.javaClass.getMethod("getWifiApConfiguration",
                    WifiManager::class.java)

            return wifiApConfigurationMethod.invoke(wifimanager, null) as WifiConfiguration
        } catch (e: Exception) {
            return null
        }

    }

    // toggle wifi hotspot on or off
    fun configApState(context: Context): Boolean {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        try {
            // if WiFi is on, turn it off
            //            if(isApOn(context, wifiManager)) {
            //                Log.d("ApManager", "turning off WiFi");
            //            }

            //            wifiManager.setWifiEnabled(false);

            val setWifiApEnabled = wifiManager.javaClass.getMethod("setWifiApEnabled",
                    WifiConfiguration::class.java, Boolean::class.javaPrimitiveType)

            var wifiConfig: WifiConfiguration? = null

            wifiConfig = getWifiApConfiguration(wifiManager)

            Log.d("ApManager", "wifiConfig: " + wifiConfig!!)

            setWifiApEnabled.invoke(wifiManager, wifiConfig, true)

            return true
        } catch (e: Exception) {
            Log.e("ApManager", "enable Ap failed", e)
        }

        return false
    }
} // end of class