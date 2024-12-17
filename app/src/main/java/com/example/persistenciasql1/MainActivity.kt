package com.example.persistenciasql1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.t8_ej01_persistenciadatossqlite.DatabaseHelper
import com.example.t8_ej01_persistenciadatossqlite.Gato
import com.example.t8_ej01_persistenciadatossqlite.GatosAdapter

// Define la clase MainActivity que hereda de AppCompatActivity, una clase base para actividades que usan las características de la ActionBar de Android.
class MainActivity : AppCompatActivity() {

    // Declara variables para los elementos de la interfaz de usuario y la base de datos.
    // lateinit indica que estas variables se inicializarán más tarde.
    private lateinit var etNombre: EditText
    private lateinit var etColor: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnVerTodos: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnModificar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHandler: DatabaseHelper

    // El método onCreate se llama cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño de la interfaz de usuario definido en activity_main.xml.
        setContentView(R.layout.activity_main)

        // Inicializa las variables con los elementos de la interfaz de usuario.
        etNombre = findViewById(R.id.etNombreGato)
        etColor = findViewById(R.id.etColorGato)
        btnAgregar = findViewById(R.id.btnAdd)
        btnVerTodos = findViewById(R.id.btnViewAll)
        recyclerView = findViewById(R.id.rvGatos)

        btnEliminar = findViewById(R.id.btnDel)
        btnModificar = findViewById(R.id.btnModif)

        // Inicializa el controlador de la base de datos.
        dbHandler = DatabaseHelper(this)

        // Configura los eventos de clic para los botones.
        btnAgregar.setOnClickListener { addGato() }
        btnVerTodos.setOnClickListener { viewGatos() }
        btnEliminar.setOnClickListener{deleteGato()}
        btnModificar.setOnClickListener{modifyGato()}
        // Muestra la lista de Gatos al iniciar la actividad.
        viewGatos()
    }

    // Método para agregar un nuevo Gato a la base de datos.
    private fun addGato() {
        // Obtiene el texto de los EditText y lo convierte en String.
        val nombre = etNombre.text.toString()
        val color = etColor.text.toString()
        // Verifica que los campos no estén vacíos.
        if (nombre.isNotEmpty() && color.isNotEmpty()) {
            // Crea un objeto Gato y lo añade a la base de datos.
            val Gato = Gato(nombre = nombre, color = color)
            val status = dbHandler.addGato(Gato)
            // Verifica si la inserción fue exitosa.
            if (status > -1) {
                Toast.makeText(applicationContext, "Gato agregado", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de Gatos.
                clearEditTexts()
                viewGatos()
            }
        } else {
            // Muestra un mensaje si los campos están vacíos.
            Toast.makeText(applicationContext, "Nombre y Año son requeridos", Toast.LENGTH_LONG).show()
        }
    }

    // Método para mostrar todos los Gatos en el RecyclerView.
    private fun viewGatos() {
        // Obtiene la lista de Gatos de la base de datos.
        val GatosList = dbHandler.getAllGatos()
        // Crea un adaptador para el RecyclerView y lo configura.
        val adapter = GatosAdapter(GatosList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }


    private fun deleteGato(){
        val nombre = etNombre.text.toString()
        val color = etColor.text.toString()
        val GatosList = dbHandler.getAllGatos()
        val GatoBorrar = GatosList.find { Gato -> Gato.nombre == nombre && Gato.color == color}
        if (GatoBorrar == null) {
            Toast.makeText(applicationContext, "El Gato no existe", Toast.LENGTH_LONG).show()
        }else{
            val status = dbHandler.deleteGato(GatoBorrar)
            if (status > -1) {
                    Toast.makeText(applicationContext, "Gato eliminado", Toast.LENGTH_LONG).show()
                    // Limpia los campos de texto y actualiza la vista de Gatos.
                    clearEditTexts()
                    viewGatos()
            }
        }

    }
    private fun modifyGato() {
        val nombre = etNombre.text.toString()
        val color = etColor.text.toString()
        val GatosList = dbHandler.getAllGatos()
        val GatoModif = GatosList.find { Gato -> Gato.nombre == nombre}
        if (GatoModif == null) {
            Toast.makeText(applicationContext,"El Gato no existe" , Toast.LENGTH_LONG).show()
        }else{
            GatoModif.color = color
            val status = dbHandler.updateGato(GatoModif)
            if (status > -1) {
                Toast.makeText(applicationContext, "Gato modificado correctamente", Toast.LENGTH_LONG).show()
                // Limpia los campos de texto y actualiza la vista de Gatos.
                clearEditTexts()
                viewGatos()
            }
        }
    }
    // Método para limpiar los campos de texto.
    private fun clearEditTexts() {
        etNombre.text.clear()
        etColor.text.clear()
    }
}
