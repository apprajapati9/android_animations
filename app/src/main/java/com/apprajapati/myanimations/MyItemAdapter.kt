package com.apprajapati.myanimations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.apprajapati.myanimations.databinding.RecyclerviewItemAnimateLayoutBinding

class MyItemAdapter : RecyclerView.Adapter<MyItemAdapter.ItemViewHolder>() {

    private val items =
        arrayOf("Einstein", "Newton", "PaulDirac", "Fynman", "Neils Bohr", "Max Plank", "Frenkel",
            "Marie Curie", "Nikola Tesla", "Archimedes", "David Hilbert", "Heisenberg", "Thomas graham", "Edward witten")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RecyclerviewItemAnimateLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val text = items[position]
        holder.binding.textScientists.text = text
    }

    class ItemViewHolder( val binding: RecyclerviewItemAnimateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}