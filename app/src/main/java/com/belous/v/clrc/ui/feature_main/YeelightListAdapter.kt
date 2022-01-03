package com.belous.v.clrc.ui.feature_main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.belous.v.clrc.R
import com.belous.v.clrc.core.domain.Yeelight
import com.belous.v.clrc.core.domain.YeelightParams
import com.belous.v.clrc.databinding.YeelightItemBinding
import com.belous.v.clrc.ui.feature_main.YeelightListAdapter.YeelightItemHolder
import java.util.*

class YeelightListAdapter(
    val onClickAction: (Int, Yeelight) -> Unit,
    val onLonClickAction: (Int) -> Boolean
) : RecyclerView.Adapter<YeelightItemHolder>() {

    private val yeelightList = ArrayList<Yeelight>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Yeelight>) {
        yeelightList.clear()
        yeelightList.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YeelightItemHolder {
        val binding =
            YeelightItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YeelightItemHolder(binding)
    }

    override fun onBindViewHolder(holder: YeelightItemHolder, position: Int) {
        holder.bind(yeelightList[position])
    }

    override fun getItemCount(): Int {
        return yeelightList.size
    }

    inner class YeelightItemHolder(private val binding: YeelightItemBinding) :
        ViewHolder(binding.root) {

        fun bind(yeelight: Yeelight) {

            val isPower = yeelight.params[YeelightParams.POWER] == "on"
            val isOnline = yeelight.params[YeelightParams.ONLINE] == "1"
            val isActive = yeelight.params[YeelightParams.ACTIVE_MODE] == "1"

            val paramsVisible =
                if (isPower && isOnline) View.VISIBLE
                else View.INVISIBLE

            binding.power.setImageResource(
                if (isOnline)
                    if (isPower) R.drawable.power_on
                    else R.drawable.power_off
                else R.drawable.offline
            )
            binding.name.text = yeelight.name
            binding.mode.setImageResource(if (isActive) R.drawable.moon else R.drawable.sun)
            binding.currentState.text = if (isActive) yeelight.params[YeelightParams.NL_BR]
            else yeelight.params[YeelightParams.BRIGHT]
            binding.stepDown.visibility = paramsVisible
            binding.currentState.visibility = paramsVisible
            binding.stepUp.visibility = paramsVisible

            itemView.setOnClickListener { onClickAction(itemView.id, yeelightList[layoutPosition]) }
            itemView.setOnLongClickListener { onLonClickAction(layoutPosition) }

            binding.power.setOnClickListener {
                onClickAction(
                    binding.power.id,
                    yeelightList[layoutPosition]
                )
            }
            binding.stepUp.setOnClickListener {
                onClickAction(
                    binding.stepUp.id,
                    yeelightList[layoutPosition]
                )
            }
            binding.stepDown.setOnClickListener {
                onClickAction(
                    binding.stepDown.id,
                    yeelightList[layoutPosition]
                )
            }
        }
    }
}