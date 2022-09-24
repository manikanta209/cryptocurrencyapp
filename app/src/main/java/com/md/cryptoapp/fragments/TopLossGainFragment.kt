package com.md.cryptoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.md.cryptoapp.adapters.MarketAdapter
import com.md.cryptoapp.api.ApiInterface
import com.md.cryptoapp.api.ApiUtilities
import com.md.cryptoapp.databinding.FragmentTopLossGainBinding
import com.md.cryptoapp.models.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class TopLossGainFragment : Fragment() {

    lateinit var binder: FragmentTopLossGainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder = FragmentTopLossGainBinding.inflate(layoutInflater)

        getMarketData()

        // Inflate the layout for this fragment
        return binder.root
    }

    private fun getMarketData() {
        val position = requireArguments().getInt("position")

        lifecycleScope.launch(Dispatchers.IO) {
            val res = ApiUtilities.getInstance().create(ApiInterface::class.java).getMarketData()

            if (res.body() != null) {
                withContext(Dispatchers.Main) {
                    val dataItem = res.body()!!.data.cryptoCurrencyList

                    //SORT THE DATA
                    Collections.sort(dataItem) { o1, o2 ->
                        (o2.quotes[0].percentChange24h.toInt())
                            .compareTo(o1.quotes[0].percentChange24h.toInt())
                    }

                    binder.spinKitView.visibility=GONE

                    val list = ArrayList<CryptoCurrency>()

                    if (position == 0) {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[i])
                        }

                        binder.topGainLoseRecyclerView.adapter =
                            MarketAdapter(requireContext(), list, "home")
                    } else {
                        list.clear()
                        for (i in 0..9) {
                            list.add(dataItem[dataItem.size-1-i]) // to get last elements
                        }

                        binder.topGainLoseRecyclerView.adapter =
                            MarketAdapter(requireContext(), list, "home")

                    }

                }
            }
        }
    }

}