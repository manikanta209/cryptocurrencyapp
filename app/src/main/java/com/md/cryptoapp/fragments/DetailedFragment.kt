package com.md.cryptoapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.md.cryptoapp.R
import com.md.cryptoapp.databinding.FragmentDetailedBinding
import com.md.cryptoapp.models.CryptoCurrency

class DetailedFragment : Fragment() {

    lateinit var binding: FragmentDetailedBinding

    private val item: DetailedFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailedBinding.inflate(layoutInflater)

        val data: CryptoCurrency = item.data!!

        setUpData(data)

        loadChart(data)

        setButtonOnClick(data)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setButtonOnClick(data: CryptoCurrency) {

        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5


        val onClickListener = View.OnClickListener {
            when (it.id) {
                fifteenMinute.id -> loadChartData(
                    it,
                    "15",
                    data,
                    oneDay,
                    oneHour,
                    fourHour,
                    oneWeek,
                    oneMonth
                )
                oneHour.id -> loadChartData(
                    it,
                    "1H",
                    data,
                    oneDay,
                    fifteenMinute,
                    fourHour,
                    oneWeek,
                    oneMonth
                )
                fourHour.id -> loadChartData(
                    it,
                    "4H",
                    data,
                    oneDay,
                    oneHour,
                    fifteenMinute,
                    oneWeek,
                    oneMonth
                )
                oneDay.id -> loadChartData(
                    it,
                    "D",
                    data,
                    fifteenMinute,
                    oneHour,
                    fourHour,
                    oneWeek,
                    oneMonth
                )
                oneWeek.id -> loadChartData(
                    it,
                    "W",
                    data,
                    oneDay,
                    oneHour,
                    fourHour,
                    fifteenMinute,
                    oneMonth
                )
                oneMonth.id -> loadChartData(
                    it,
                    "M",
                    data,
                    oneDay,
                    oneHour,
                    fourHour,
                    oneWeek,
                    fifteenMinute
                )
            }
        }

        fifteenMinute.setOnClickListener(onClickListener)
        oneHour.setOnClickListener(onClickListener)
        fourHour.setOnClickListener(onClickListener)
        oneDay.setOnClickListener(onClickListener)
        oneWeek.setOnClickListener(onClickListener)
        oneMonth.setOnClickListener(onClickListener)


    }

    private fun loadChartData(
        view: View,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneHour: AppCompatButton,
        fourHour: AppCompatButton,
        oneWeek: AppCompatButton,
        oneMonth: AppCompatButton
    ) {


        disableButtons(oneDay, oneHour, fourHour, oneWeek, oneMonth)

        view?.setBackgroundResource(R.drawable.active_button)

        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + item.symbol
                .toString() + "USD&interval=" + s + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun disableButtons(
        oneDay: AppCompatButton,
        oneHour: AppCompatButton,
        fourHour: AppCompatButton,
        oneWeek: AppCompatButton,
        oneMonth: AppCompatButton
    ) {
        oneHour.background = null
        fourHour.background = null
        oneDay.background = null
        oneWeek.background = null
        oneMonth.background = null
    }

    private fun loadChart(data: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        binding.detaillChartWebView.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + data.symbol
                .toString() + "USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
        )
    }

    private fun setUpData(data: CryptoCurrency) {

        binding.detailSymbolTextView.text = data.symbol

        Glide.with(requireContext()).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + data.id + ".png"
        ).thumbnail(Glide.with(requireContext()).load(R.drawable.spinner))
            .into(binding.detailImageView)

        binding.detailPriceTextView.text =
            "+${String.format("%.02f", data.quotes[0].percentChange24h)}%"

        if (data.quotes[0].percentChange24h > 0) {
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeTextView.text = "+${data.quotes[0].percentChange24h}%"

            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            //To display only 2 decimal digits
            //holder.binding.topCurrencyChangeTextView.text= "+${String.format("%.02f",item.quotes[0].percentChange24h)}%"

        } else {
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeTextView.text = "${data.quotes[0].percentChange24h}%"

            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)

        }

    }

}