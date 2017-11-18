package com.bukalapak.keyfinder

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bukalapak.keyfinder.databinding.FragmentKeyFinderBinding
import org.altbeacon.beacon.*
import java.util.*

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

        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_LAYOUT))
        beaconManager.addMonitorNotifier(this)
        beaconManager.addRangeNotifier(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_key_finder, container, false)

        binding.keyButton.setOnClickListener { startBeaconService() }
        binding.keyRipple.stopRipple()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        beaconManager.unbind(this)
        beaconManager.removeRangeNotifier(this)
        beaconManager.removeMonitorNotifier(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE
                && grantResults.first() == PackageManager.PERMISSION_GRANTED) {

            beaconManager.bind(this)
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        beaconManager.startRangingBeaconsInRegion(region)
    }

    override fun didExitRegion(region: Region) {
        beaconManager.stopRangingBeaconsInRegion(region)
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>, region: Region) {

        if (beacons.isNotEmpty()) {
            binding.distanceLabel.text = String.format("%.1f", beacons.first().distance)
            binding.nameLabel.text = beacons.first().bluetoothName

        } else {
            binding.distanceLabel.text = "0"
            binding.nameLabel.text = ""
        }
    }

    private fun startBeaconService() {

        val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (ContextCompat.checkSelfPermission(context, permissions.first()) == PackageManager.PERMISSION_GRANTED) {

            beaconManager.bind(this)
            binding.keyRipple.startRipple()

        } else {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_LOCATION_PERMISSION_CODE)
        }
    }

    companion object {
        val TAG = KeyFinderFragment::class.java.simpleName
        val REQUEST_LOCATION_PERMISSION_CODE = 1
        val BEACON_MONITORING_ID = "KeyFinder"
        val BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
    }
}