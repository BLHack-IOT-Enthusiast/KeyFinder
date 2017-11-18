package com.bukalapak.keyfinder

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bukalapak.keyfinder.databinding.FragmentSettingBinding
import org.altbeacon.beacon.Beacon

/**
 * Created on : November/17/2017
 * Author     : mnafian
 * Company    : Bukalapak
 * Project    : KeyFinder
 */
class SettingFragment : Fragment() {
    private var beacon: Beacon? = null
    private lateinit var binding: FragmentSettingBinding

    fun bind(beacon: Beacon) {
        this.beacon = beacon

        binding.tvAlias.text = beacon.bluetoothName
        binding.tvMac.text = beacon.bluetoothAddress
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvRename.setOnClickListener {
            binding.vgEditName.visibility = View.VISIBLE
            binding.vgName.visibility = View.GONE

            binding.etName.setText(binding.tvDevice.text)
        }

        binding.btnSimpan.setOnClickListener {
            binding.vgEditName.visibility = View.VISIBLE
            binding.vgName.visibility = View.GONE

            binding.tvDevice.setText(binding.etName.text)
        }
    }
}