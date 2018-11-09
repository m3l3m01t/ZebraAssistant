package com.visualstudio.m3l3m01t.zebramate

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_zebra_main.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class ZebraMainActivity : AppCompatActivity() {
    private val TAG = ZebraMainActivity::class.java.toString()

    private var mReservation: WifiManager.LocalOnlyHotspotReservation? = null
    private val REQUEST_CODE_LOCATION: Int = 1
    private var _locationPermitted: Boolean = false

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun turnOnHotspot() {
        val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        manager.startLocalOnlyHotspot(object : WifiManager.LocalOnlyHotspotCallback() {
            override fun onStarted(reservation: WifiManager.LocalOnlyHotspotReservation) {
                super.onStarted(reservation)
                Log.d(TAG, "Wifi Hotspot is on now")
                mReservation = reservation
            }

            override fun onStopped() {
                super.onStopped()
                Log.d(TAG, "onStopped: ")
            }

            override fun onFailed(reason: Int) {
                super.onFailed(reason)
                Log.d(TAG, "onFailed: ")
            }
        }, Handler())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun turnOffHotspot() {
        mReservation?.close()
        mReservation = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zebra_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Personal AP control", Snackbar.LENGTH_LONG)
                    .setAction("Switch") {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            if (mReservation == null)
                                turnOnHotspot()
                            else
                                turnOffHotspot()
                        }
                    }.show()
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_LOCATION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_LOCATION) {
            _locationPermitted = (grantResults[0] == PackageManager.PERMISSION_GRANTED)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_zebra_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
