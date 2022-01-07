package com.belous.v.clrc.ui.feature_yeelight

import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.R
import com.belous.v.clrc.appComponent
import com.belous.v.clrc.databinding.YeelightFragmentBinding
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.use_case.UseCases
import com.belous.v.clrc.utils.ViewModelFactory
import com.belous.v.clrc.utils.findChildren
import javax.inject.Inject

class YeelightFragment : Fragment(R.layout.yeelight_fragment) {

    companion object {
        const val YEELIGHT_ID = "yeelight_id"
    }

    private var viewModel: YeelightViewModel? = null
    private lateinit var binding: YeelightFragmentBinding

    @Inject
    lateinit var mainStates: MainStates

    @Inject
    lateinit var useCases: UseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = YeelightFragmentBinding.bind(view)

        arguments?.let {
            val yeelightId = it.getInt(YEELIGHT_ID)
            viewModel = ViewModelFactory(
                yeelightId,
                mainStates,
                useCases
            ).create(YeelightViewModel::class.java)

            viewModel?.yeelightData?.observe(viewLifecycleOwner) { yeelight ->
                showYeelightData(yeelight)
            }

            binding.swipeRefreshLayout.setOnRefreshListener {
                binding.swipeRefreshLayout.isRefreshing = false
                viewModel?.reloadYeelight()
            }

            binding.buttonsLayout.findChildren()
                .filter { child -> child.id != -1 }
                .forEach { child -> child.setOnClickListener { view -> viewModel?.onSetParams(view.id) } }
        }
    }

    private fun showYeelightData(yeelight: Yeelight) {

        binding.powerChange.setTextColor(
            if (yeelight.isPower && yeelight.isOnline) getColor(requireContext(), R.color.green)
            else getColor(requireContext(), R.color.red)
        )

        binding.modeChange.setCompoundDrawablesWithIntrinsicBounds(
            null, null, null,
            getDrawable(
                requireContext(),
                if (yeelight.isActive) R.drawable.status_on else R.drawable.status_off
            )
        )
        var brightText =
            if (yeelight.isPower) "${yeelight.bright}%" else getString(R.string.off)
        if (!yeelight.isOnline) {
            brightText = getString(R.string.offline)
        }
        binding.bright.text = brightText
        binding.mode.setCompoundDrawablesWithIntrinsicBounds(
            null,
            null,
            getDrawable(
                requireContext(),
                if (yeelight.isActive) R.drawable.moon else R.drawable.sun
            ),
            null
        )

        val yeelightCt = yeelight.ct
        val tempText =
            getString(R.string.cl_temp) + " " + (if (yeelightCt == 0) 3400 else yeelightCt) + "K"
        binding.temp.text = tempText
    }
}