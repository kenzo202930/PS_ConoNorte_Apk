package com.example.kenzo.ps_cononorte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button Login;
    EditText Usuario,Password;
    public String URL_LOGIN = "http://192.168.0.105/api/ValidarLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Usuario = (EditText)findViewById(R.id.txtUsuario);
        Password = (EditText)findViewById(R.id.txtPassword);
        Login = (Button) findViewById(R.id.btnLogin);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String usu = Usuario.getText().toString().trim();
//                String password = Password.getText().toString().trim();
                ValidarLogin();
//                Toast.makeText(LoginActivity.this, usu + " " + password, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ValidarLogin() {
        final String usu = Usuario.getText().toString().trim();
        final String pwd = Password.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String id = ObtenerIDUsuarioLogeado(response);
//                        Toast.makeText(LoginActivity.this, id, Toast.LENGTH_SHORT).show();
                        if (!id.equals("")){
                            GenerarSharedPreferences(id);
                            Intent intent = new Intent(LoginActivity.this, OpcionesActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(LoginActivity.this, "Ah ocurrido un error al momento de iniciar sesión", Toast.LENGTH_SHORT).show();
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
                params.put("email", usu);
                params.put("password", pwd);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);

    }
    public void GenerarSharedPreferences(String id) {
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("MiSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("LoginOn", true);
        editor.putString("id", id);
        editor.apply();
    }
    public String ObtenerIDUsuarioLogeado(String response) {
        String variable = "";
        try {
            JSONObject jsonResponse = new JSONObject(response);
            variable = jsonResponse.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return variable;
    }
}
