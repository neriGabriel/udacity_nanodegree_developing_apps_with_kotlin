package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidAdapter(private val clickListener: AdapterClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }

    class AsteroidViewHolder(var binding: AsteroidItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bindingToUI(item: Asteroid) {
            binding.asteroid = item
            binding.executePendingBindings()
        }

    }

    class AdapterClickListener (val clickListener: (asteroid: Asteroid) -> Unit ) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val binding: AsteroidItemBinding = AsteroidItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        return AsteroidViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val currentAsteroid = getItem(position)

       holder.also {
           it.itemView.setOnClickListener{
               clickListener.onClick(currentAsteroid)
           }
           it.bindingToUI(currentAsteroid)
       }
    }
}
