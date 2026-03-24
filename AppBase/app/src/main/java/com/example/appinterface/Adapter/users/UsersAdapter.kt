package com.example.appinterface.Adapter.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Api.Models.User
import com.example.appinterface.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class UsersAdapter(
    private val users: List<User>,
    private val listener: UsersListener
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_field, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position], listener)
    }

    override fun getItemCount(): Int = users.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, listener: UsersListener) {
            val nameTv: TextView = itemView.findViewById(R.id.nameTv)
            val phoneTv: TextView = itemView.findViewById(R.id.phoneTv)
            val fullName = "${user.user_name} ${user.user_first_surname} ${user.user_second_surname}"

            nameTv.text = fullName
            phoneTv.text = user.user_phone

            itemView.setOnClickListener {
                val bottomSheet = BottomSheetDialog(itemView.context)
                val modalView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.user_info_bottom_dialog, null)

                modalView.findViewById<TextView>(R.id.info_modal_user_name).text = " ${fullName}"
                modalView.findViewById<TextView>(R.id.info_modal_user_rol).text = " ${user.rol_name}"
                modalView.findViewById<TextView>(R.id.info_modal_user_phone).text = " ${user.user_phone}"
                modalView.findViewById<TextView>(R.id.info_modal_user_email).text = " ${user.user_email}"
                modalView.findViewById<TextView>(R.id.info_modal_user_city).text = " ${user.user_city}"
                modalView.findViewById<TextView>(R.id.info_modal_user_address).text = " ${user.user_address}"
                modalView.findViewById<TextView>(R.id.info_modal_user_date).text = " ${user.user_date}"

                bottomSheet.setContentView(modalView)
                bottomSheet.show()
            }

            itemView.findViewById<ImageButton>(R.id.editUserButton).setOnClickListener {
                listener.onEditUser(user)
            }

            itemView.findViewById<ImageButton>(R.id.deleteUserButton).setOnClickListener {
                listener.onDelete(user)
            }
        }
    }
}