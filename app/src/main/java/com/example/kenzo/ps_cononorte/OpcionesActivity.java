package com.example.kenzo.ps_cononorte;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OpcionesActivity extends AppCompatActivity {

    FloatingActionButton floatingActionButton;
    private int notificationId = 1;
    private String URL_REGISTRO_ASISTENCIA = "http://192.168.0.100/api/RegistrarMedicoAsistencia";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(OpcionesActivity.this, "Click", Toast.LENGTH_SHORT).show();
                EnviarNotificacionAsistencia();
            }
        });

        IniciarAlarma();
    }

    public String ObtenerIDUsuario() {
        SharedPreferences sharedPreferences = OpcionesActivity.this.getSharedPreferences("MiSesion", Context.MODE_PRIVATE);
        String id_usuario = sharedPreferences.getString("id", "");
        return id_usuario;
    }

    public void EnviarNotificacionAsistencia(){
        final String MedicoId = ObtenerIDUsuario();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTRO_ASISTENCIA,
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
                            Toast.makeText(OpcionesActivity.this, "Se ha registrado la asistencia con éxito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(OpcionesActivity.this, "Ah ocurrido un problema al marcar la asistencia", Toast.LENGTH_SHORT).show();
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
                params.put("MedicoId", MedicoId);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(OpcionesActivity.this);
        requestQueue.add(stringRequest);
    }

    public void IniciarAlarma(){
        Intent intent = new Intent(OpcionesActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("todo", "Probando");

        // getBroadcast(context, requestCode, intent, flags)
        PendingIntent alarmIntent = PendingIntent.getBroadcast(OpcionesActivity.this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);

        int hour = 1;
        int minute = 17;

//        Toast.makeText(this, String.valueOf(hour) + " - " + String.valueOf(minute), Toast.LENGTH_SHORT).show();
        // Create time.
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, hour);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();

        // Set alarm.
        // set(type, milliseconds, intent)
        alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);

        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();
    }
}
