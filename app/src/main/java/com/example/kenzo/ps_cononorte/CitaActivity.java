package com.example.kenzo.ps_cononorte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CitaActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    private Spinner spinner;
    private ArrayList<String> Especialidades;
    private JSONArray result;
    private String IdMedico = "";
    private EditText textDni,textApellido,textNombre;
    private Button btnCita;
    private String URL_REGISTRO = "http://192.168.0.100/api/RegistrarCitaUsuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);
        Especialidades = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.txtspinner);
        textApellido = (EditText)findViewById(R.id.txtApellido);
        textDni = (EditText)findViewById(R.id.txtDNI);
        textNombre = (EditText)findViewById(R.id.txtNombre);
        btnCita = (Button)findViewById(R.id.btnCita);

        spinner.setOnItemSelectedListener(this);
        getData();

        btnCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarCita();
            }
        });


    }

    private void getData() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                Especialidades.add(json.getString("Especialidad"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(CitaActivity.this, android.R.layout.simple_spinner_dropdown_item, Especialidades));
    }

    private String getIdMedico(int position) {
        String course = "";
        try {
            JSONObject json = result.getJSONObject(position);
            course = json.getString("Especialista_Id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }

    public void RegistrarCita() {
        final String dni = textDni.getText().toString().trim();
        final String apellido = textApellido.getText().toString().trim();
        final String nombre = textNombre.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean variable = false;
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            variable = jsonResponse.getBoolean("data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (variable){
                            Toast.makeText(CitaActivity.this, "Se ha registrado la cita con éxito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(CitaActivity.this, "Se ha excedido el numero de citas del día", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Nombre", nombre);
                params.put("Apellido", apellido);
                params.put("DNI", dni);
                params.put("Especialidad_Id", IdMedico);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(CitaActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        IdMedico = getIdMedico(i);
        Toast.makeText(this, IdMedico, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
