package app.shmehdi.covid19app.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.shmehdi.covid19app.R
import app.shmehdi.covid19app.network.models.Countries
import kotlinx.android.synthetic.main.item_list.view.*

class GlobalStatsAdapter(private val context: Context, private val countries: List<Countries>) :
    RecyclerView.Adapter<GlobalStatsAdapter.ViewHolder>() {

    var isMyCountryAvailable = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list, parent, false))

    override fun getItemCount(): Int = countries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(countries[position])
        holder.highLightMyCountry(position)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(countries: Countries) {
            itemView.tv_name.text = countries.country

            itemView.tv_total_cases.text = ("${countries.totalConfirmed}")
            itemView.tv_total_cases_new.text = ("+${countries.newConfirmed}")

            itemView.tv_recovered.text = "${countries.totalRecovered}"
            itemView.tv_recovereds_new.text = ("+${countries.newRecovered}")

            itemView.tv_deaths.text = "${countries.totalDeaths}"
            itemView.tv_deaths_new.text = ("+${countries.newRecovered}")
        }

        fun highLightMyCountry(position: Int) {
            if(position == 0 && isMyCountryAvailable) {
                itemView.tv_name.setTypeface(Typeface.DEFAULT_BOLD)
                itemView.tv_total_cases.setTypeface(Typeface.DEFAULT_BOLD)
                itemView.tv_recovered.setTypeface(Typeface.DEFAULT_BOLD)
                itemView.tv_deaths.setTypeface(Typeface.DEFAULT_BOLD)
            }
            else {
                itemView.tv_name.setTypeface(context.resources.getFont(R.font.gothamrounded_book))
                itemView.tv_total_cases.setTypeface(context.resources.getFont(R.font.gothamrounded_book))
                itemView.tv_recovered.setTypeface(context.resources.getFont(R.font.gothamrounded_book))
                itemView.tv_deaths.setTypeface(context.resources.getFont(R.font.gothamrounded_book))
            }
        }
    }
}