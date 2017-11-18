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

class KeyFinderFragment : Fragment(), BeaconConsumer, RangeNotifier {

    var callback: KeyFinderFragmentCallback? = null

    private lateinit var binding: FragmentKeyFinderBinding
    private lateinit var beaconManager: BeaconManager

    private var currentlyFindingKey: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BEACON_LAYOUT))
        beaconManager.addRangeNotifier(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_key_finder, container, false)

        binding.keyRipple.stopRipple()
        binding.keyButton.visibility = View.INVISIBLE
        binding.guideLabel.visibility = View.INVISIBLE

        binding.keyButton.setOnClickListener {

            if (currentlyFindingKey) {
                stopBeaconService()

            } else {
                startBeaconService()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        if (!beaconManager.isBound(this)) {
            beaconManager.bind(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        stopBeaconService()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE
                && grantResults.first() == PackageManager.PERMISSION_GRANTED) {

            startBeaconService()

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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
        binding.keyButton.visibility = View.VISIBLE
        binding.guideLabel.visibility = View.VISIBLE
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>, region: Region) {

        activity.runOnUiThread {

            if (beacons.isNotEmpty() && currentlyFindingKey) {
                val beacon = beacons.first()

                binding.distanceLabel.text = String.format("%.1f", beacon.distance)

                callback?.onDetectBeacon(beacon)

            } else {
                binding.distanceLabel.text = "0"
            }
        }
    }

    private fun startBeaconService() {

        val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (ContextCompat.checkSelfPermission(context, permissions.first()) == PackageManager.PERMISSION_GRANTED) {

            beaconManager.startRangingBeaconsInRegion(BEACON_REGION)

            binding.guideLabel.visibility = View.INVISIBLE
            binding.keyRipple.startRipple()

            currentlyFindingKey = true

        } else {
            ActivityCompat.requestPermissions(activity, permissions, REQUEST_LOCATION_PERMISSION_CODE)
        }
    }

    private fun stopBeaconService() {
        beaconManager.stopRangingBeaconsInRegion(BEACON_REGION)

        binding.keyRipple.stopRipple()
        binding.distanceLabel.text = "0"
        binding.guideLabel.visibility = View.VISIBLE

        currentlyFindingKey = false
    }

    companion object {
        val REQUEST_LOCATION_PERMISSION_CODE = 1
        val BEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        val BEACON_REGION = Region("KeyFinder", null, null, null)
    }
}

interface KeyFinderFragmentCallback {

    fun onDetectBeacon(beacon: Beacon)
}