package com.example.olhovivoestagio.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.olhovivoestagio.R
import com.example.olhovivoestagio.databinding.ListagemParadaBinding
import com.example.olhovivoestagio.remote.models.InfoCollected

class SearchAdapter : ListAdapter<InfoCollected, SearchAdapter.SearchViewHolder>(InfoCollectedDiff()) {

    class SearchViewHolder private constructor(val binding: ListagemParadaBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: InfoCollected){
            binding.infoCollected = item
        }

        companion object{
            fun from(parent: ViewGroup) : SearchViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListagemParadaBinding = DataBindingUtil.inflate(layoutInflater, R.layout.listagem_parada, parent, false)
                return SearchViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class InfoCollectedDiff : DiffUtil.ItemCallback<InfoCollected>(){
    override fun areItemsTheSame(oldItem: InfoCollected, newItem: InfoCollected): Boolean {
        return oldItem.codigo == newItem.codigo
    }

    override fun areContentsTheSame(oldItem: InfoCollected, newItem: InfoCollected): Boolean {
        return oldItem == newItem
    }

}