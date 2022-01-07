package com.belous.v.clrc.ui.feature_main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.belous.v.clrc.MainStates
import com.belous.v.clrc.R
import com.belous.v.clrc.appComponent
import com.belous.v.clrc.databinding.MainFragmentBinding
import com.belous.v.clrc.ui.dialog.ContextDialog
import com.belous.v.clrc.ui.dialog.FoundDialog
import com.belous.v.clrc.ui.feature_yeelight.YeelightFragment
import com.belous.v.clrc.use_case.UseCases
import com.belous.v.clrc.utils.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var binding: MainFragmentBinding

    private var yeelightListAdapter: YeelightListAdapter? = null

    @Inject
    lateinit var mainStates: MainStates

    @Inject
    lateinit var useCases: UseCases

    private val viewModel by viewModels<MainViewModel>(factoryProducer = {
        ViewModelFactory(mainStates, useCases)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MainFragmentBinding.bind(view)

        val toolbar = binding.actionBar?.toolbar
        toolbar?.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        yeelightListAdapter = YeelightListAdapter(
            onClickAction = { viewId, yeelight ->
                viewModel.changeYeelightParam(viewId, yeelight)
            },
            onLonClickAction = { yeelightId ->
                showContextDialog(yeelightId)
                true
            })
        binding.clRecyclerView.adapter = yeelightListAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.reloadYeelightList()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEventFlow.collectLatest { event ->
                when (event) {
                    is MainViewModel.ListUiEvent.YeelightListFound -> {
                        FoundDialog().show(childFragmentManager, FoundDialog::class.java.simpleName)
                    }
                    is MainViewModel.ListUiEvent.OpenYeelight -> {
                        openYeelightFragment(event.yeelightId)
                    }
                }
            }
        }

        viewModel.yeelightList.observe(viewLifecycleOwner) { yeelightList ->
            yeelightListAdapter?.setData(yeelightList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            viewModel.findYeelight()
            return true
        }
        return false
    }

    private fun showContextDialog(yeelightId: Int) {
        ContextDialog().apply {
            arguments = Bundle().apply {
                putInt(ContextDialog.YEELIGHT_ID, yeelightId)
            }
        }.show(childFragmentManager, ContextDialog::class.java.simpleName)
    }

    private fun openYeelightFragment(yeelightId: Int) {
        val fragment = YeelightFragment().apply {
            arguments = Bundle().apply {
                putInt(YeelightFragment.YEELIGHT_ID, yeelightId)
            }
        }
        parentFragmentManager.commit {
            add(R.id.main_layout, fragment, null)
            addToBackStack(null)
        }
    }
}