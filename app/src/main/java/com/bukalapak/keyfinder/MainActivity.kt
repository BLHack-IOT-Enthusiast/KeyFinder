package com.bukalapak.keyfinder

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region



class MainActivity : AppCompatActivity(), BeaconConsumer{

    var beaconManager : BeaconManager? = null
    val PERMISSION_REQUEST_COARSE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
                arrayOf( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_COARSE_LOCATION)

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager?.beaconParsers?.add(BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager?.bind(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onDestroy() {
        super.onDestroy()
        beaconManager?.unbind(this)
    }

    override fun onBeaconServiceConnect() {
//        beaconManager?.addMonitorNotifier(object : MonitorNotifier {
//
//            override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+p0)
//            }
//
//            override fun didEnterRegion(p0: Region?) {
//                Log.i(TAG, "I just saw an beacon for the first time!");
//            }
//
//            override fun didExitRegion(p0: Region?) {
//                Log.i(TAG, "I no longer see an beacon")
//
//            }
//        })

        beaconManager?.addRangeNotifier({ beacons, region ->
            if (beacons.size > 0) {
                Log.i(TAG, "The first beacon I see is about " + beacons.iterator().next().distance + " meters away.")
                beaconManager?.stopRangingBeaconsInRegion(DIGIBAL)
            }
        })

        try {
            beaconManager?.startRangingBeaconsInRegion(DIGIBAL)
        } catch (e: Throwable) {
            Log.e(TAG, e.toString())
        }

    }

    companion object {
        val TAG = "MainActivity"
        val DIGIBAL = Region("00001803-494C-4F47-4943-544543480000", null, null, null)
    }



}
