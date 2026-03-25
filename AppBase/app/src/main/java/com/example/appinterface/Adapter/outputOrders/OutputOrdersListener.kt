package com.example.appinterface.Adapter.outputOrders

import com.example.appinterface.Api.Models.OutputOrder

interface OutputOrdersListener {
    fun onShowInfo(outputOrder: OutputOrder)
    fun onEditOutputOrder(outputOrder: OutputOrder)
    fun onDisable(outputOrder: OutputOrder)
    fun onEnable(outputOrder: OutputOrder)
}