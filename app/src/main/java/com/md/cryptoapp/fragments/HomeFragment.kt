package com.md.cryptoapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.md.cryptoapp.R
import com.md.cryptoapp.adapters.TopLossGainPagerAdapter
import com.md.cryptoapp.adapters.TopMarketAdapter
import com.md.cryptoapp.api.ApiInterface
import com.md.cryptoapp.api.ApiUtilities
import com.md.cryptoapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var viewBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentHomeBinding.inflate(layoutInflater)

        getTopCurrencyList()

        setTabLayout()

        return viewBinding.root
    }

    private fun setTabLayout() {

        val adapter = TopLossGainPagerAdapter(this)

        viewBinding.contentViewPager.adapter = adapter

        viewBinding.contentViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 0) {
                    viewBinding.topGainIndicator.visibility = VISIBLE
                    viewBinding.topLoseIndicator.visibility = GONE
                } else {
                    viewBinding.topGainIndicator.visibility = GONE
                    viewBinding.topLoseIndicator.visibility = VISIBLE
                }
            }
        })


        TabLayoutMediator(viewBinding.tabLayout, viewBinding.contentViewPager) { tab, position ->
            var title = if (position == 0) {
                "Top Gainers"
            } else {
                "Top Losers"
            }
            tab.text= title
        }.attach()

    }

    private fun getTopCurrencyList() {

        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()


            withContext(Dispatchers.Main) {
                viewBinding.topCurrencyRecyclerView.adapter = TopMarketAdapter(
                    requireContext(),
                    res.body()!!.data.cryptoCurrencyList
                )
            }

            Log.d(
                "ReceivedData", "getTopCurrencyList: ${
                    res.body()?.data?.cryptoCurrencyList
                }"
            )
        }

    }

}