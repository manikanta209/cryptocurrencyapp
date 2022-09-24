package com.md.cryptoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.md.cryptoapp.R
import com.md.cryptoapp.databinding.CurrencyItemLayoutBinding
import com.md.cryptoapp.fragments.HomeFragmentDirections
import com.md.cryptoapp.fragments.MarketFragmentDirections
import com.md.cryptoapp.models.CryptoCurrency

class MarketAdapter(var context: Context, var list: List<CryptoCurrency>, var type: String) :
    RecyclerView.Adapter<MarketAdapter.MarketAdapterHolder>() {

    inner class MarketAdapterHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = CurrencyItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketAdapterHolder {
        return MarketAdapterHolder(
            LayoutInflater.from(context).inflate(R.layout.currency_item_layout, parent, false)
        )
    }

    fun updateData(dataItem: List<CryptoCurrency>) {
        list = dataItem
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MarketAdapterHolder, position: Int) {

        var item = list[position]

        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol


        //Glide::To update the image
        Glide.with(context).load(
            "https://s2.coinmarketcap.com/static/img/coins/64x64/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyImageView)



        Glide.with(context).load(
            "https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/" + item.id + ".png"
        ).thumbnail(Glide.with(context).load(R.drawable.spinner))
            .into(holder.binding.currencyChartImageView)


        holder.binding.currencyPriceTextView.text =
            "+${String.format("%.02f", item.quotes[0].price)}%"



        if (item.quotes[0].percentChange24h > 0) {
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+${item.quotes[0].percentChange24h}%"

            //To display only 2 decimal digits
            //holder.binding.topCurrencyChangeTextView.text= "+${String.format("%.02f",item.quotes[0].percentChange24h)}%"

        } else {
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = "${item.quotes[0].percentChange24h}%"
        }


        holder.itemView.setOnClickListener {
            if (type == "home") {
                findNavController(it).navigate(
                    HomeFragmentDirections.actionHomeFragment2ToDetailedFragment(item)
                )
            } else if(type == "market") {
                findNavController(it).navigate(
                    MarketFragmentDirections.actionMarketFragment2ToDetailedFragment(item)
                )
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}