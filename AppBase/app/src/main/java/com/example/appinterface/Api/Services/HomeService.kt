package com.example.appinterface.Api.Services

import com.example.appinterface.Api.Models.DataResponseCharts
import com.example.appinterface.Api.Models.DataResponseKpis
import retrofit2.Call
import retrofit2.http.GET

interface HomeService {
    @GET("reports/get_count_of_all")
    fun getKpisInfo(): Call<DataResponseKpis>

    @GET("reports/get_monthly_products_growth")
    fun getChartInfo(): Call<DataResponseCharts>
}