package com.example.luistrujillo.pomodoro

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_stats.*

class StatsActivity : AppCompatActivity() {

    lateinit var pieChart: PieChart
    var averageWorkTimeDay : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        supportActionBar?.title = "Statistics"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setUpPieChartData()
        setupBarChartData()
        setupLineChartData()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setUpPieChartData() {
        pieChart = findViewById(R.id.pieChart)

        // Show the percentage values
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f,10f,5f,5f)

        // Degree at which the pie chart rotates
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // Enables the hole in the middle of the pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.WHITE)

        // The slightly transparent circle in the middle
//        pieChart.transparentCircleRadius = 53f
        pieChart.animateY(1500, Easing.EasingOption.EaseInCubic)

        // Define the pie chart data slices
        val yValues = setupDummyPie()
        val dataSet = PieDataSet(yValues, "Week Days")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val colors = java.util.ArrayList<Int>()

        /*
        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)
        */

        for (c in ColorTemplate.PASTEL_COLORS){
            colors.add(c)
        }
        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.WHITE)

        pieChart.data = data
    }

    private fun setupBarChartData() {
        // create BarEntry for Bar Group
        val bargroup = setupDummyBar()
        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "")

        barDataSet.color = ContextCompat.getColor(this, R.color.colorAccent)

        val week = arrayOf("Mon", "Tue", "Wed", "Thr", "Fri", "Sat", "Sun")
        val data = BarData(barDataSet)
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(week)
        barChart.data = data
        data.setValueTextColor(Color.BLACK)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.labelCount = 7
//        barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
//        barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
//        barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.legend.isEnabled = false
        barChart.setPinchZoom(false)
        barChart.data.setDrawValues(true)
    }

    private fun setupLineChartData() {
        val yVals = setupDummyLine()

        val set1: LineDataSet
        set1 = LineDataSet(yVals, "DataSet 1")

//         set1.fillAlpha = 110
//         set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        // set1.enableDashedLine(5f, 5f, 0f);
        // set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.color = Color.BLUE
        set1.setCircleColor(Color.BLUE)
        set1.lineWidth = 1f
        set1.circleRadius = 3f
        set1.setDrawCircleHole(false)
        set1.valueTextSize = 0f
        set1.setDrawFilled(false)

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)

        // set data
        lineChart.data = data
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setPinchZoom(true)
        lineChart.xAxis.enableGridDashedLine(100f, 0f, 0f)
        lineChart.axisRight.enableGridDashedLine(5f, 0f, 0f)
        lineChart.axisLeft.enableGridDashedLine(5f, 0f, 0f)
        //lineChart.setDrawGridBackground()
        lineChart.xAxis.labelCount = 11
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    }

    private fun setupDummyLine(): ArrayList<Entry> {
        val yVals = ArrayList<Entry>()
        yVals.add(Entry(0f, MainActivity.finalList[0].dayHourList["hour0"]!!.toFloat(), "0"))
        yVals.add(Entry(1f, MainActivity.finalList[0].dayHourList["hour1"]!!.toFloat(), "1"))
        yVals.add(Entry(2f, MainActivity.finalList[0].dayHourList["hour2"]!!.toFloat(), "2"))
        yVals.add(Entry(3f, MainActivity.finalList[0].dayHourList["hour3"]!!.toFloat(), "3"))
        yVals.add(Entry(4f, MainActivity.finalList[0].dayHourList["hour4"]!!.toFloat(), "4"))
        yVals.add(Entry(5f, MainActivity.finalList[0].dayHourList["hour5"]!!.toFloat(), "5"))
        yVals.add(Entry(6f, MainActivity.finalList[0].dayHourList["hour6"]!!.toFloat(), "6"))
        yVals.add(Entry(7f, MainActivity.finalList[0].dayHourList["hour7"]!!.toFloat(), "7"))
        yVals.add(Entry(8f, MainActivity.finalList[0].dayHourList["hour8"]!!.toFloat(), "8"))
        yVals.add(Entry(9f, MainActivity.finalList[0].dayHourList["hour9"]!!.toFloat(), "9"))
        yVals.add(Entry(10f, MainActivity.finalList[0].dayHourList["hour10"]!!.toFloat(), "10"))
        yVals.add(Entry(11f, MainActivity.finalList[0].dayHourList["hour11"]!!.toFloat(), "11"))
        yVals.add(Entry(12f, MainActivity.finalList[0].dayHourList["hour12"]!!.toFloat(), "12"))
        yVals.add(Entry(13f, MainActivity.finalList[0].dayHourList["hour13"]!!.toFloat(), "13"))
        yVals.add(Entry(14f, MainActivity.finalList[0].dayHourList["hour14"]!!.toFloat(), "14"))
        yVals.add(Entry(15f, MainActivity.finalList[0].dayHourList["hour15"]!!.toFloat(), "15"))
        yVals.add(Entry(16f, MainActivity.finalList[0].dayHourList["hour16"]!!.toFloat(), "16"))
        yVals.add(Entry(17f, MainActivity.finalList[0].dayHourList["hour17"]!!.toFloat(), "17"))
        yVals.add(Entry(18f, MainActivity.finalList[0].dayHourList["hour18"]!!.toFloat(), "18"))
        yVals.add(Entry(19f, MainActivity.finalList[0].dayHourList["hour19"]!!.toFloat(), "19"))
        yVals.add(Entry(20f, MainActivity.finalList[0].dayHourList["hour20"]!!.toFloat(), "20"))
        yVals.add(Entry(21f, MainActivity.finalList[0].dayHourList["hour21"]!!.toFloat(), "21"))
        yVals.add(Entry(22f, MainActivity.finalList[0].dayHourList["hour22"]!!.toFloat(), "22"))
        yVals.add(Entry(23f, MainActivity.finalList[0].dayHourList["hour23"]!!.toFloat(), "23"))

        return yVals
    }

    private fun setupDummyBar(): ArrayList<BarEntry> {
        val bargroup = ArrayList<BarEntry>()
        bargroup.add(BarEntry(0f, MainActivity.finalList[0].weekHourList["Mon"]!!.toFloat(), "1"))
        bargroup.add(BarEntry(1f, MainActivity.finalList[0].weekHourList["Tue"]!!.toFloat(), "2"))
        bargroup.add(BarEntry(2f, MainActivity.finalList[0].weekHourList["Wed"]!!.toFloat(), "3"))
        bargroup.add(BarEntry(3f, MainActivity.finalList[0].weekHourList["Thr"]!!.toFloat(), "4"))
        bargroup.add(BarEntry(4f, MainActivity.finalList[0].weekHourList["Fri"]!!.toFloat(), "5"))
        bargroup.add(BarEntry(5f, MainActivity.finalList[0].weekHourList["Sat"]!!.toFloat(), "6"))
        bargroup.add(BarEntry(6f, MainActivity.finalList[0].weekHourList["Sun"]!!.toFloat(), "7"))

        return bargroup
    }

    private fun setupDummyPie(): ArrayList<PieEntry> {
        val yValues = ArrayList<PieEntry>()
//        yValues.add(PieEntry(MainActivity.finalList[0].netPomodoroCount, MainActivity.finalList[0].name))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Mon"]!!.toFloat(), "Mon"))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Tue"]!!.toFloat(), "Tue"))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Wed"]!!.toFloat(), "Wed"))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Thr"]!!.toFloat(), "Thr"))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Fri"]!!.toFloat(), "Fri"))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Sat"]!!.toFloat(), "Sat"))
        yValues.add(PieEntry(MainActivity.finalList[0].weekHourList["Sun"]!!.toFloat(), "Sun"))

        return yValues
    }
}
