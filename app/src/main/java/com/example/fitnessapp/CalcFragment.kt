package com.example.fitnessapp

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.fitnessapp.databinding.CalcFragmentBinding

class CalcFragment : Fragment(R.layout.calc_fragment) {
	private var _binding: CalcFragmentBinding? = null
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val binding = CalcFragmentBinding.bind(view)
		_binding = binding
		val imm: InputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

		binding.etCalcMaster.setText(masterCalc.calc[0]))
		binding.etCalc1.setText(masterCalc.calc[1])
		binding.etCalc2.setText(masterCalc.calc[2])
		binding.etCalc3.setText(masterCalc.calc[3])
		binding.etCalc4.setText(masterCalc.calc[4])

		binding.btnEquals.setOnClickListener {
			var allAdded = 0
			masterCalc.calc[1] = binding.etCalc1.text.toString()
			masterCalc.calc[2] = binding.etCalc2.text.toString()
			masterCalc.calc[3] = binding.etCalc3.text.toString()
			masterCalc.calc[4] = binding.etCalc4.text.toString()

			for (x in 1 until masterCalc.calc.size) {
				if (masterCalc.calc[x].isNotEmpty()) {
					allAdded += masterCalc.calc[x].toInt()
				}
			}
			masterCalc.calc[0] = allAdded.toString()
			binding.etCalcMaster.setText(masterCalc.calc[0])

			imm.hideSoftInputFromWindow(requireView().windowToken, 0)
		}
		binding.btnMaster.setOnClickListener {
			for (x in 2 until masterCalc.calc.size) {
				masterCalc.calc[x] = ""
			}
			masterCalc.calc[1] = masterCalc.calc[0]
			binding.etCalc1.setText(masterCalc.calc[1])
			binding.etCalc2.setText(masterCalc.calc[2])
			binding.etCalc3.setText(masterCalc.calc[3])
			binding.etCalc4.setText(masterCalc.calc[4])

			imm.hideSoftInputFromWindow(requireView().windowToken, 0)
		}
		binding.btnBlank.setOnClickListener {
			for (x in 1 until masterCalc.calc.size) {
				masterCalc.calc[x] = ""
			}
			binding.etCalc1.setText(masterCalc.calc[1])
			binding.etCalc2.setText(masterCalc.calc[2])
			binding.etCalc3.setText(masterCalc.calc[3])
			binding.etCalc4.setText(masterCalc.calc[4])

			imm.hideSoftInputFromWindow(requireView().windowToken, 0)
		}
	}
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}