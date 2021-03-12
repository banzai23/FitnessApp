package com.example.fitnessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.PrFragmentBinding

class PRFragment : Fragment(R.layout.pr_fragment) {
    private var _binding: PrFragmentBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = PrFragmentBinding.bind(view)
        _binding = binding

        binding.recyclerPrs.layoutManager = LinearLayoutManager(context)
        binding.recyclerPrs.adapter = RecyclerAdapterPRs()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    class RecyclerAdapterPRs() :
        RecyclerView.Adapter<RecyclerAdapterPRs.ViewHolder>()
    {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            private val equip: TextView = v.findViewById(R.id.tv_equip)
            private val weight: EditText = v.findViewById(R.id.et_weight)

            fun bind(position: Int) {
                equip.text = masterPRS.equip[position]
                weight.setText(masterPRS.weight[position])
            }
        }
        override fun getItemCount() = masterPRS.equip.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_prs,
                parent,
                false
            )
            return ViewHolder(inflatedView)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(position)
        }
    }
}