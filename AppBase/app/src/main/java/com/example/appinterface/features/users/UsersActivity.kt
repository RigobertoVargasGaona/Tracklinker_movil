package com.example.appinterface.features.users

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.users.UsersAdapter
import com.example.appinterface.Adapter.users.UsersListener
import com.example.appinterface.Api.Models.CreateUser
import com.example.appinterface.Api.Models.User
import com.example.appinterface.Api.Models.UsersResponse
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersActivity : AppCompatActivity() {
    private var userList: MutableList<User> = mutableListOf()
    private lateinit var recycler: RecyclerView

    private lateinit var userAdapter: UsersAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.users_activity)

        recycler = findViewById(R.id.users_recycler_view)
        recycler.layoutManager = LinearLayoutManager(this)

        userAdapter = UsersAdapter(userList, object : UsersListener {
            override fun onShowInfo(user: User) {}
            override fun onEditUser(user: User) {
                val bottomSheet = BottomSheetDialog(this@UsersActivity)
                val modalView = layoutInflater.inflate(R.layout.edit_user_bottom_dialog, null)
                bottomSheet.setContentView(modalView)

                bottomSheet.show()

                val autoComplete = modalView.findViewById<AutoCompleteTextView>(R.id.rolSelect)
                val options = listOf("Admin", "Almacen", "Tecnico")
                val adapter = ArrayAdapter(this@UsersActivity, android.R.layout.simple_dropdown_item_1line, options)
                autoComplete.setAdapter(adapter)
                autoComplete.setText(user.rol_name, false)
                autoComplete.setOnClickListener { autoComplete.showDropDown() }

                modalView.findViewById<AutoCompleteTextView>(R.id.rolSelect).setText(user.rol_name)
                modalView.findViewById<EditText>(R.id.edit_user_name).setText(user.user_name)
                modalView.findViewById<EditText>(R.id.edit_user_first_surname).setText(user.user_first_surname)
                modalView.findViewById<EditText>(R.id.edit_user_second_surname).setText(user.user_second_surname)
                modalView.findViewById<EditText>(R.id.edit_user_email).setText(user.user_email)
                modalView.findViewById<EditText>(R.id.edit_user_phone).setText(user.user_phone)
                modalView.findViewById<EditText>(R.id.edit_user_city).setText(user.user_city)
                modalView.findViewById<EditText>(R.id.edit_user_address).setText(user.user_address)

                val editButton = modalView.findViewById<Button>(R.id.edit_user_button)
                editButton.setOnClickListener {
                    val rolText = autoComplete.text.toString()
                    val rol_id = when(rolText) {
                        "Admin" -> 1
                        "Almacen" -> 2
                        "Tecnico" -> 3
                        else -> 0
                    }
                    val name = modalView.findViewById<EditText>(R.id.edit_user_name).text.toString()
                    val firstSurname = modalView.findViewById<EditText>(R.id.edit_user_first_surname).text.toString()
                    val secondSurname = modalView.findViewById<EditText>(R.id.edit_user_second_surname).text.toString()
                    val email = modalView.findViewById<EditText>(R.id.edit_user_email).text.toString()
                    val phone = modalView.findViewById<EditText>(R.id.edit_user_phone).text.toString().toLong()
                    val city = modalView.findViewById<EditText>(R.id.edit_user_city).text.toString()
                    val address = modalView.findViewById<EditText>(R.id.edit_user_address).text.toString()

                    RetrofitInstance.usersApi.updateUser(
                        CreateUser(
                            rol_id,
                            name,
                            firstSurname,
                            secondSurname,
                            phone,
                            "12345",
                            email,
                            address,
                            city
                        ),
                        user.user_id,
                    ).enqueue(object : Callback<UsersResponse> {
                        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@UsersActivity, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                                bottomSheet.dismiss()
                                loadUsers()
                            }
                        }
                        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                            Toast.makeText(this@UsersActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onDelete(user: User) {
                val bottomSheet = BottomSheetDialog(this@UsersActivity)
                val modalView = layoutInflater.inflate(R.layout.delete_user_bottom_dialog, null)
                bottomSheet.setContentView(modalView)

                bottomSheet.show()

                modalView.findViewById<TextView>(R.id.delete_user_message_name).setText(" ${user.user_name} ${user.user_first_surname} ?")

                val deleteButton = modalView.findViewById<Button>(R.id.delete_user_button)

                deleteButton.setOnClickListener {
                    RetrofitInstance.usersApi.deleteUser(user.user_id).enqueue(object : Callback<UsersResponse> {
                        override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@UsersActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                bottomSheet.dismiss()
                                loadUsers()
                            }
                        }
                        override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                            Toast.makeText(this@UsersActivity, "Error al intentar eliminar el usuario", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        })

        recycler.adapter = userAdapter
        loadUsers()
    }

    fun loadUsers() {
        val recyclerView = findViewById<RecyclerView>(R.id.users_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.usersApi.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userList.clear()
                    userList.addAll(response.body() ?: emptyList())
                    userAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@UsersActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@UsersActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun createUser(v: View) {
        val bottomSheet = BottomSheetDialog(this)
        val modalView = layoutInflater.inflate(R.layout.add_user_bottom_dialog, null)
        bottomSheet.setContentView(modalView)

        val options = listOf("Seleccionar", "Admin", "Almacen", "Tecnico")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)

        val autoComplete = modalView.findViewById<AutoCompleteTextView>(R.id.rolSelect)
        autoComplete.setAdapter(adapter)

        autoComplete.setOnClickListener {
            autoComplete.showDropDown()
        }

        val createButton = modalView.findViewById<Button>(R.id.create_user_button)

        createButton.setOnClickListener {
            val rolText = autoComplete.text.toString()
            val rol_id = when(rolText) {
                "Admin" -> 1
                "Almacen" -> 2
                "Tecnico" -> 3
                else -> 0
            }

            val name = modalView.findViewById<EditText>(R.id.add_user_name).text.toString()
            val first_surname = modalView.findViewById<EditText>(R.id.add_user_first_surname).text.toString()
            val second_surname = modalView.findViewById<EditText>(R.id.add_user_second_surname).text.toString()
            val phone = modalView.findViewById<EditText>(R.id.add_user_phone).text.toString().toLong()
            val email = modalView.findViewById<EditText>(R.id.add_user_email).text.toString()
            val city = modalView.findViewById<EditText>(R.id.add_user_city).text.toString()
            val address = modalView.findViewById<EditText>(R.id.add_user_address).text.toString()

            RetrofitInstance.usersApi.createUser(CreateUser(
                rol_id,
                name,
                first_surname,
                second_surname,
                phone,
                "123",
                email,
                address,
                city
            )).enqueue(object : Callback<UsersResponse> {
                override fun onResponse(call: Call<UsersResponse>, response: Response<UsersResponse>) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        Toast.makeText(this@UsersActivity, "Usuario creado con exito", Toast.LENGTH_SHORT).show()
                        bottomSheet.dismiss()
                        loadUsers()
                    } else {
                        Toast.makeText(this@UsersActivity, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Toast.makeText(this@UsersActivity, "Error en la conexión con la API", Toast.LENGTH_SHORT).show()
                }
            })
        }

        bottomSheet.show()
    }
}