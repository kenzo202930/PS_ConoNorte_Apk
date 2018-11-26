package com.example.kenzo.ps_cononorte;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button Especialista,Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Especialista = (Button)findViewById(R.id.btnEspecialista);
        Usuario = (Button)findViewById(R.id.btnUsuario);

        Especialista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginEspecialista();
            }
        });

        Usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SolicitarCita();
            }
        });

    }

    private void LoginEspecialista()
    {
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    private void SolicitarCita(){
        Intent intent = new Intent(MenuActivity.this, CitaActivity.class);
        startActivity(intent);
    }
}
