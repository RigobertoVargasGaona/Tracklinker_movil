package com.example.appinterface.helpers

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.appinterface.MainActivity
import com.example.appinterface.R
import com.example.appinterface.features.categories.CategoriesActivity
import com.example.appinterface.features.products.ProductsActivity
import com.example.appinterface.features.users.UsersActivity
import com.example.appinterface.features.warranties.WarrantiesActivity
//import com.example.appinterface.features.outputs.OutputsActivity /

object BottomNavHelper {

    private val navMap = mapOf(
        R.id.nav_home        to MainActivity::class.java,
        R.id.nav_users       to UsersActivity::class.java,
        R.id.nav_categories  to CategoriesActivity::class.java,
        R.id.nav_products    to ProductsActivity::class.java,
        R.id.nav_warranties  to WarrantiesActivity::class.java,
       // R.id.nav_outputs     to OutputsActivity::class.java
    )

    private val labelMap = mapOf(
        R.id.nav_home       to R.id.nav_home_label,
        R.id.nav_users      to R.id.nav_users_label,
        R.id.nav_categories to R.id.nav_categories_label,
        R.id.nav_products   to R.id.nav_products_label,
        R.id.nav_warranties to R.id.nav_warranties_label,
        R.id.nav_outputs    to R.id.nav_outputs_label
    )

    fun setup(activity: AppCompatActivity, activeButtonId: Int) {
        val navRoot = activity.findViewById<View>(R.id.bottomNav)

        navMap.keys.forEach { buttonId ->
            val label = navRoot.findViewById<TextView>(labelMap[buttonId]!!)
            val button = navRoot.findViewById<ImageButton>(buttonId)
            val container = button.parent as ViewGroup // Esto detecta el FlexboxLayout de 80dp

            if (buttonId == activeButtonId) {
                container.setBackgroundResource(R.drawable.bg_nav_active)
                button.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                label.setTextColor(Color.parseColor("#FFFFFF"))
                label.typeface = ResourcesCompat.getFont(activity, R.font.dmsansextrabold)
            } else {
                container.background = null
                button.imageTintList = ColorStateList.valueOf(Color.parseColor("#9ca3af"))
                label.setTextColor(Color.parseColor("#9ca3af"))
                label.typeface = ResourcesCompat.getFont(activity, R.font.dmsansmedium)
            }

            container.setOnClickListener {
                if (buttonId != activeButtonId) {
                    val intent = Intent(activity, navMap[buttonId])
                    activity.startActivity(intent)
                    activity.overridePendingTransition(0, 0)
                }
            }

            button.isClickable = false
        }
    }
}