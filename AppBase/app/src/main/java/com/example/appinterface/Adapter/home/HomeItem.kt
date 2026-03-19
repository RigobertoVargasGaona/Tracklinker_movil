package com.example.appinterface.Adapter.home

import com.github.mikephil.charting.data.Entry

sealed class HomeItem {
    data class Kpi(val title: String, val value: String, val percent: String? = null) : HomeItem()
    data class Chart(val title: String, val entries: List<Entry>) : HomeItem()
}