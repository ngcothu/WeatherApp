package com.example.myweather

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ModListRAdapter(private val weatherList:java.util.ArrayList<WeatherData>)
    : RecyclerView.Adapter<ModListRAdapter.ModListViewHolder>()
{
    class ModListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textViewModCity : TextView = itemView.findViewById(R.id.tvModCity)
        val button = itemView.findViewById<Button>(R.id.btItemDelete)
        private var modListRAdapter = ModListRAdapter(weatherList)

        init
        {
            if(textViewModCity.text.toString() == "Current City")
            {
                button.visibility = View.INVISIBLE
                modListRAdapter.notifyItemChanged(adapterPosition)
            }
            itemView.findViewById<Button>(R.id.btItemDelete).setOnClickListener{
                if(adapterPosition != 0)
                {
                    modListRAdapter.removeCity(adapterPosition)
//                    modListAdapter.weatherList.removeAt(adapterPosition)
//                    modListAdapter.notifyItemRemoved(adapterPosition)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModListViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.modify_item, parent, false)

        return ModListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ModListViewHolder, position: Int)
    {
        val weather = weatherList[position]
        holder.textViewModCity.text = weather.zWeatherData.city
        if(position == 0)
        {
            holder.button.visibility = View.INVISIBLE
        }
        holder.button.setOnClickListener{
            if(position != 0)
            {
                removeCity(position)
            }
        }
    }

    override fun getItemCount(): Int
    {
        return weatherList.size
    }

    fun removeCity(position: Int) {
        // ".remove" is returning error above
        weatherList.removeAt(position)
        notifyDataSetChanged()
    }

}