package com.example.appinterface.Adapter.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class HomeAdapter(private val items: MutableList<HomeItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_KPI = 0
        const val TYPE_CHART = 1
    }

    fun updateData(newItems: List<HomeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is HomeItem.Kpi -> TYPE_KPI
        is HomeItem.Chart -> TYPE_CHART
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_KPI -> KpiViewHolder(inflater.inflate(R.layout.kpi_card, parent, false))
            TYPE_CHART -> ChartViewHolder(inflater.inflate(R.layout.wave_chart_card, parent, false))
            else -> throw IllegalArgumentException("ViewType desconocido: $viewType")
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is HomeItem.Kpi -> (holder as KpiViewHolder).bind(item)
            is HomeItem.Chart -> (holder as ChartViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class KpiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.KpiName)
        private val value: TextView = view.findViewById(R.id.KpiMetricValue)
        private val subtitle: TextView = view.findViewById(R.id.KpiPercentValue)

        fun bind(kpi: HomeItem.Kpi) {
            title.text = kpi.title
            value.text = kpi.value
            subtitle.text = kpi.percent
        }
    }

    class ChartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.chartTitle)
        private val chart: LineChart = view.findViewById(R.id.chart)

        fun bind(item: HomeItem.Chart) {
            title.text = item.title

            val dataSet = LineDataSet(item.entries, "").apply {
                color = Color.rgb(108, 99, 255)
                setCircleColor(Color.rgb(108, 99, 255) )
                lineWidth = 2f
                circleRadius = 4f
                setDrawFilled(true)
                fillColor = Color.rgb(108, 99, 255)
                fillAlpha = 30
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.apply {
                data = LineData(dataSet)
                description.isEnabled = false
                legend.isEnabled = false
                setTouchEnabled(false)
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                invalidate()
            }
        }
    }
}