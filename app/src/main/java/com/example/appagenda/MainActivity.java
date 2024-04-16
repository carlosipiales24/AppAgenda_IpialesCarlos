package com.example.appagenda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText txtNombre, txtTelefono, txtEmail;
    TextView txtDatos;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtEmail = findViewById(R.id.txtEmail);
        txtDatos = findViewById(R.id.txtDatos);
        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("MisDatos", Context.MODE_PRIVATE);
    }
    public void guardarDatos(View view) {
        // Obtener los datos de los EditText
        String nombre = txtNombre.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();

        // Crear un objeto JSONObject para almacenar los datos
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", nombre);
            jsonObject.put("telefono", telefono);
            jsonObject.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Guardar los datos como una cadena JSON en SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(nombre, jsonObject.toString()); // Utilizamos el nombre como clave
        editor.apply();

        Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
    }


    public void buscarDatos(View view) {
        // Obtener el nombre a buscar
        String nombreBuscado = txtNombre.getText().toString();

        // Obtener los datos del contacto desde SharedPreferences
        String datosContacto = sharedPreferences.getString(nombreBuscado, null);

        if (datosContacto != null) {
            // Si se encontraron datos para el nombre ingresado, autocompletar los EditText
            try {
                JSONObject jsonObject = new JSONObject(datosContacto);
                txtNombre.setText(jsonObject.getString("nombre"));
                txtTelefono.setText(jsonObject.getString("telefono"));
                txtEmail.setText(jsonObject.getString("email"));
                Toast.makeText(this, "Datos encontrados", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Si no se encontraron datos para el nombre ingresado, mostrar un mensaje
            Toast.makeText(this, "No se encontraron datos para el nombre ingresado", Toast.LENGTH_SHORT).show();
        }
    }




    public void mostrarTodosLosDatos(View view) {
        // Obtener todas las entradas de SharedPreferences
        Map<String, ?> allEntries = sharedPreferences.getAll();

        StringBuilder datos = new StringBuilder();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            // Construir una cadena con todos los datos guardados
            datos.append("Nombre: ").append(entry.getKey()).append("\n");
            try {
                JSONObject jsonObject = new JSONObject((String) entry.getValue());
                datos.append("Teléfono: ").append(jsonObject.getString("telefono")).append("\n");
                datos.append("Email: ").append(jsonObject.getString("email")).append("\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            datos.append("\n");
        }

        // Mostrar los datos en un Toast o en cualquier otra vista
        //Toast.makeText(this, datos.toString(), Toast.LENGTH_LONG).show();
        txtDatos.setText(datos.toString());
    }

    public void eliminarTodosLosDatos(View view) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Elimina todos los datos
        editor.apply();

        Toast.makeText(this, "Todos los datos han sido eliminados", Toast.LENGTH_SHORT).show();
    }

}