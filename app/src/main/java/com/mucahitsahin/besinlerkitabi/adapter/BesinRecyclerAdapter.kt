package com.mucahitsahin.besinlerkitabi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mucahitsahin.besinlerkitabi.R
import com.mucahitsahin.besinlerkitabi.databinding.BesinRecyclerRowBinding
import com.mucahitsahin.besinlerkitabi.model.Besin
import com.mucahitsahin.besinlerkitabi.util.gorselIndir
import com.mucahitsahin.besinlerkitabi.util.placeholderYap
import com.mucahitsahin.besinlerkitabi.view.BesinListesiFragmentDirections
import kotlinx.android.synthetic.main.besin_recycler_row.view.*

class BesinRecyclerAdapter(val besinListesi:ArrayList<Besin>):RecyclerView.Adapter<BesinRecyclerAdapter.BesinViewHolder>(),BesinClickListener {

    class BesinViewHolder(var view:BesinRecyclerRowBinding):RecyclerView.ViewHolder(view.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BesinViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        //val view=inflater.inflate(R.layout.besin_recycler_row,parent,false)
        val view=DataBindingUtil.inflate<BesinRecyclerRowBinding>(inflater,R.layout.besin_recycler_row,parent,false)
        return BesinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return besinListesi.size
    }

    override fun onBindViewHolder(holder: BesinViewHolder, position: Int) {
        holder.view.besin=besinListesi[position]
        holder.view.listener=this

        /*
        holder.itemView.isim.text=besinListesi.get(position).besinIsim
        holder.itemView.kalori.text=besinListesi.get(position).besinKalori

        holder.itemView.setOnClickListener {
            val action=BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinDetayiFragment(besinListesi.get(position).uuid)
            Navigation.findNavController(it).navigate(action)
        }

        holder.itemView.imageView.gorselIndir(besinListesi.get(position).besinGorsel, placeholderYap(holder.itemView.context))
    */
    }


    fun besinListesiniGuncelle(yeniBesinListesi:List<Besin>){
        besinListesi.clear()
        besinListesi.addAll(yeniBesinListesi)
        notifyDataSetChanged()
    }

    override fun besinTiklandi(view: View) {
        val uuid=view.besin_uuid.text.toString().toIntOrNull()
        uuid?.let {
            val action=BesinListesiFragmentDirections.actionBesinListesiFragmentToBesinDetayiFragment(it)
            Navigation.findNavController(view).navigate(action)

        }
    }

}