package com.bukalapak.keyfinder

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region



class MainActivity : AppCompatActivity(), BeaconConsumer{

    var beaconManager : BeaconManager? = null
    val PERMISSION_REQUEST_COARSE_LOCATION = 1
    var pagerAdapter : PagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = PagerAdapter(supportFragmentManager)
        viewContainer.adapter = pagerAdapter

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

    class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        val NUM_ITEMS = 2


        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return KeyFinderFragment()
                1 -> return SettingFragment()
                else -> return KeyFinderFragment()
            }
        }

        override fun getCount(): Int {
            return NUM_ITEMS
        }

    }

    companion object {
        val TAG = "MainActivity"
        val DIGIBAL = Region("00001803-494C-4F47-4943-544543480000", null, null, null)
    }



}
