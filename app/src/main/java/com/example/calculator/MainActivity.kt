package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    val TANFACTOR = 1000.0
    //meter, centimeter, yard, feet, inch
    val CONVERSION_TO_METER = arrayOf<Double>(1.0, 0.01, 0.9144, 0.3048, 0.0254)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //buttons
        val distanceCal = findViewById(R.id.CalculateDistance) as Button
        val targetCal = findViewById(R.id.CalculateTarget) as Button
        val mradCAL = findViewById(R.id.calculateMRAD) as Button
        //TextView
        val distanceBox = findViewById(R.id.distance) as TextView
        val targetBox = findViewById(R.id.targetHeight) as TextView
        val mradBox = findViewById(R.id.MRAD) as TextView

        //Drop down menu settings
        val units = resources.getStringArray(R.array.units)
        var spinnerTargetUnit = findViewById(R.id.targetUnit) as Spinner
        if (spinnerTargetUnit != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
            spinnerTargetUnit.adapter = adapter

            spinnerTargetUnit.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, View: View?, position: Int, id: Long) {
                        Toast.makeText(this@MainActivity,
                            units[position], Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
            }
        }
        var spinnerDistanceUnit = findViewById(R.id.distanceUnit) as Spinner
        if(spinnerDistanceUnit != null){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, units)
            spinnerDistanceUnit.adapter = adapter
            spinnerTargetUnit.onItemSelectedListener = object:
                AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, View: View?, position: Int, id: Long) {
                        Toast.makeText(this@MainActivity,
                            units[position], Toast.LENGTH_SHORT).show()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
        }

        //button action
        distanceCal.setOnClickListener{
            var data = getValue(distanceBox, targetBox, mradBox).toMutableList()
            data[1] = converToMeter(data[1], spinnerTargetUnit.selectedItem.toString(), true)

            var distance = data[1]?.div(data[2]!!)
            distance = distance?.times(1000.0)
            distance = converToMeter(distance, spinnerDistanceUnit.selectedItem.toString(), false)
            distanceBox.setText(String.format("%.3f", distance))
            Toast.makeText(this@MainActivity, "Calculated Distance", Toast.LENGTH_SHORT).show()
        }
        targetCal.setOnClickListener{
            var data = getValue(distanceBox, targetBox, mradBox).toMutableList()
            data[0] = converToMeter(data[0], spinnerDistanceUnit.selectedItem.toString(), true)

            var targetSize = data[0]*data[2]
            targetSize = targetSize?.div(1000.0)
            targetSize = converToMeter(targetSize, spinnerTargetUnit.selectedItem.toString(), false)
            targetBox.setText(String.format("%.3f", targetSize))
            Toast.makeText(this@MainActivity, "Calculated Target Size", Toast.LENGTH_SHORT).show()
        }
        mradCAL.setOnClickListener{
            var data = getValue(distanceBox, targetBox, mradBox).toMutableList()
            data[0] = converToMeter(data[0], spinnerDistanceUnit.selectedItem.toString(), true)
            data[1] = converToMeter(data[1], spinnerTargetUnit.selectedItem.toString(), true)

            var mrad = data[1]?.div(data[0]!!)
            mrad = mrad?.times(1000)
            mradBox.setText(String.format("%.3f", mrad))
            Toast.makeText(this@MainActivity, "Calculated Mrad Value", Toast.LENGTH_SHORT).show()
        }
        //Clear textview
        distanceBox.setOnClickListener{
            distanceBox.setText("")
        }
        targetBox.setOnClickListener{
            targetBox.setText("")
        }
        mradBox.setOnClickListener{
            mradBox.setText("")
        }
    }
    fun getValue(distanceBox:TextView, targetBox: TextView, mradBox: TextView):List<Double>{
        var result = ArrayList<Double>()

        var textDistance:String? = distanceBox.text.toString()
        if(textDistance == null || textDistance == "")
            textDistance = "0.0"

        result.add(textDistance?.toDouble())

        var textTarget:String? = targetBox.text.toString()
        if(textTarget == null || textTarget == "")
            textTarget = "0.0"
        result.add(textTarget?.toDouble())

        var textMrad:String? = mradBox.text.toString()
        if(textMrad == null || textMrad == "")
            textMrad = "0.0"
        result.add(textMrad?.toDouble())

        return result
    }
    //direction true = to meter, false = to other measurement
    fun converToMeter(a: Double, unit:String, direction: Boolean): Double{
        var conversionFactor:Int = 0
        when(unit){
            "M" -> conversionFactor = 0
            "Cm" -> conversionFactor = 1
            "Yd." -> conversionFactor = 2
            "Ft." -> conversionFactor = 3
            "In." -> conversionFactor = 4
            else -> conversionFactor = 0
        }
        if(direction)
            return a.times(CONVERSION_TO_METER[conversionFactor])
        else
            return (a / CONVERSION_TO_METER[conversionFactor])
    }
}
//            android:text="0.0"