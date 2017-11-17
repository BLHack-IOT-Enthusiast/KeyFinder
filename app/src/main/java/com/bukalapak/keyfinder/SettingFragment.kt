package com.bukalapak.keyfinder

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bukalapak.keyfinder.databinding.FragmentSettingBinding

/**
 * Created on : November/17/2017
 * Author     : mnafian
 * Company    : Bukalapak
 * Project    : KeyFinder
 */
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding

    init {

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_setting, container, false)
        return binding.root
    }


}