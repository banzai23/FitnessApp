package com.example.fitnessapp

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.databinding.PrFragmentBinding

class PRFragment : Fragment(R.layout.pr_fragment) {
    private var _binding: PrFragmentBinding? = null
    lateinit var mainInterface: ActivityInterface
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = PrFragmentBinding.bind(view)
        _binding = binding
        mainInterface = context as ActivityInterface

        binding.recyclerPrs.layoutManager = LinearLayoutManager(context)
        binding.recyclerPrs.adapter = RecyclerAdapterPRs(masterPRS)
        val ith = setItemTouchHelper(binding.recyclerPrs, masterPRS)
        ith.attachToRecyclerView(binding.recyclerPrs)

        binding.btnSave.setOnClickListener {
            for (x in 0 until masterPRS.equip.size) {
                val editText: EditText = binding.recyclerPrs.findViewHolderForAdapterPosition(x)!!.itemView.findViewById(R.id.et_weight)
                masterPRS.equip[x].weight = editText.text.toString()
            }
            mainInterface.saveDefaultFiles()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    class RecyclerAdapterPRs(private val prs: PRS) :
        RecyclerView.Adapter<RecyclerAdapterPRs.ViewHolder>()
    {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            private val equip: TextView = v.findViewById(R.id.tv_equip)
            private val weight: EditText = v.findViewById(R.id.et_weight)

            fun bind(prs: PRS, position: Int) {
                equip.text = prs.equip[position].name
                weight.setText(prs.equip[position].weight)
            }
        }
        override fun getItemCount() = prs.equip.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflatedView = LayoutInflater.from(parent.context).inflate(
                R.layout.recycler_prs,
                parent,
                false
            )
            return ViewHolder(inflatedView)
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(prs, position)
        }
    }
    private fun setItemTouchHelper(recycler: RecyclerView, prs: PRS): ItemTouchHelper {
        val itemTouchCallback = object : ItemTouchHelper.Callback() {
            override fun isLongPressDragEnabled() = false
            override fun isItemViewSwipeEnabled() = true
            override fun getMovementFlags(
                    recyclerView: RecyclerView, viewHolder:
                    RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }
            override fun onMove(
                    recyclerView: RecyclerView, source: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val pos = viewHolder.adapterPosition
                showDeletePRSDialog(pos)
            }
            fun showDeletePRSDialog(pos: Int) {
                val builder = AlertDialog.Builder(context)
                val message: String = getString(R.string.dialog_delete_prs) + " " + prs.equip[pos].name + "?"
                builder.setMessage(message)
                builder.setPositiveButton(R.string.dialog_yes) { _, _ ->
                    prs.equip.removeAt(pos)
                    recycler.adapter!!.notifyItemRemoved(pos)
                }
                .setNegativeButton(R.string.dialog_no) { dialog, _ ->
                    dialog.dismiss()
                    recycler.adapter!!.notifyItemChanged(pos)
                }
                builder.create()
                builder.show()
            }
        }
        return ItemTouchHelper(itemTouchCallback)
    }

}