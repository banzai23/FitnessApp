package com.example.fitnessapp

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.fitnessapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import java.io.FileNotFoundException
import java.io.InputStream

const val DEFAULT_CALC_FILE = "info.json"
const val DEFAULT_PRS_FILE = "prs.json"

@Serializable
data class Calc(var calc: MutableList<String>)
@Serializable
data class PRS(var equip: MutableList<String>, var weight: MutableList<String>)

lateinit var masterCalc: Calc
lateinit var masterPRS: PRS

class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var inputStream: InputStream = try {
            this.openFileInput(DEFAULT_CALC_FILE) // load this file, if not found, load the resource
        } catch (e: FileNotFoundException) {
            resources.openRawResource(R.raw.info)
        }
        var inputString = inputStream.bufferedReader().readText()
        masterCalc = Json.decodeFromString(inputString)   // load to the variable from the file
        println(masterCalc)
        inputStream = try {
            this.openFileInput(DEFAULT_PRS_FILE)
        } catch (e: FileNotFoundException) {
            resources.openRawResource(R.raw.prs)
        }
        inputString = inputStream.bufferedReader().readText()
        masterPRS = Json.decodeFromString(inputString)
        println(masterPRS)
        inputStream.close()
        // asset loaded, bind the viewIDs next
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        val binding = activityBinding
        setSupportActionBar(findViewById(R.id.toolbar))
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        fragmentTransaction(CalcFragment(),"calc")
        binding.tabsCalcPR.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {    // tabsBreakfastLunchDinner
                when (tab.position) {
                    0 -> {
                        fragmentTransaction(CalcFragment(),"CalcFrag")
                    }
                    else -> {
                        fragmentTransaction(PRFragment(),"PRFrag")
                    }
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
                println("Tab reselected!")
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
                println("Tab unselected!")
            }
        })
    }
    private fun fragmentTransaction(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val findFrag = fragmentManager.findFragmentByTag(tag)
        fragmentManager.executePendingTransactions()
        if (findFrag != null)   // following code keeps only one instance of any fragment at one time
            transaction.replace(R.id.root_layout, findFrag, tag)
        else {
            transaction.add(R.id.root_layout, fragment, tag)
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
    private fun saveDefaultFiles() {
        this.openFileOutput(DEFAULT_CALC_FILE, Context.MODE_PRIVATE).use {
            val jsonToFile = Json.encodeToString(masterCalc)
            it.write(jsonToFile.toByteArray())
        }
        this.openFileOutput(DEFAULT_PRS_FILE, Context.MODE_PRIVATE).use {
            val jsonToFile = Json.encodeToString(masterPRS)
            it.write(jsonToFile.toByteArray())
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        saveDefaultFiles()
    }
}