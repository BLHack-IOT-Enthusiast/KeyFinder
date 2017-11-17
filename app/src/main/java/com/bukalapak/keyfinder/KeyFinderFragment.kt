package com.bukalapak.keyfinder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bukalapak.keyfinder.databinding.FragmentKeyFinderBinding
import org.altbeacon.beacon.*

/**
 * Created on : November/17/2017
 * Author     : mnafian
 * Company    : Bukalapak
 * Project    : KeyFinder
 */
class KeyFinderFragment : Fragment(), BeaconConsumer, MonitorNotifier, RangeNotifier {

    private lateinit var binding: FragmentKeyFinderBinding
    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLocationPermissions()

        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_LAYOUT))
        beaconManager.addMonitorNotifier(this)
        beaconManager.bind(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_key_finder, container, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        beaconManager.unbind(this)
    }

    override fun getApplicationContext(): Context {
        return activity.applicationContext
    }

    override fun unbindService(connection: ServiceConnection?) {
        activity.unbindService(connection)
    }

    override fun bindService(service: Intent?, connection: ServiceConnection?, flags: Int): Boolean {
        return activity.bindService(service, connection, flags)
    }

    override fun onBeaconServiceConnect() {
        beaconManager.startRangingBeaconsInRegion(Region(BEACON_MONITORING_ID, null, null, null))
    }

    override fun didDetermineStateForRegion(state: Int, region: Region) {
        Log.i(TAG, "didDetermineStateForRegion $state $region")
    }

    override fun didEnterRegion(region: Region) {
        Log.i(TAG, "didEnterRegion $region")
    }

    override fun didExitRegion(region: Region) {
        Log.i(TAG, "didExitRegion $region")
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>, region: Region) {

        for (beacon in beacons) {
            Log.i(TAG, "didRangeBeacon $beacon")
        }
    }

    private fun requestLocationPermissions() {

        val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        ActivityCompat.requestPermissions(activity, permissions, REQUEST_LOCATION_PERMISSION_CODE)
    }

    companion object {
        val TAG = KeyFinderFragment::class.java.simpleName
        val REQUEST_LOCATION_PERMISSION_CODE = 1
        val BEACON_MONITORING_ID = "KeyFinder"
        val BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
    }
}