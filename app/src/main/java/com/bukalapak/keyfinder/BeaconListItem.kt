package com.bukalapak.keyfinder

import android.content.Context
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_key_finder.*
import org.altbeacon.beacon.Beacon

/**
 * Created by handrata on 11/17/17.
 */
class BeaconRecyclerItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    fun setBeaconData(beacons : MutableCollection<Beacon>) {
        val mLayoutManager = LinearLayoutManager(this.context)
        layoutManager = mLayoutManager
        itemAnimator = DefaultItemAnimator()

        if (adapter == null) {
            val newAdapter = BeaconListAdapter(beacons = beacons.toMutableList())
            adapter = newAdapter
        } else {
            adapter.notifyDataSetChanged()
        }
    }

    inner class BeaconListAdapter(private val beacons: MutableList<Beacon>) : RecyclerView.Adapter<BeaconListAdapter.BeaconViewHolder>() {
        inner class BeaconViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById<View>(R.id.tvName) as TextView
            val tvDistance: TextView = view.findViewById<View>(R.id.tvDistance) as TextView

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeaconViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_scan_beacon, parent, false)

            return BeaconViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: BeaconViewHolder, position: Int) {
            val beacon = beacons[position]
            holder.tvName.text = beacon.bluetoothName
            holder.tvDistance.text = context.getString(R.string.beacon_list_item_distance, beacon.distance.toString())
        }

        override fun getItemCount(): Int {
            return beacons.size
        }
    }
}