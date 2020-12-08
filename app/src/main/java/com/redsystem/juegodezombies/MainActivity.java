package com.redsystem.juegodezombies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button BTNLOGIN,BTNREGISTRO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BTNLOGIN = findViewById(R.id.BTNLOGIN);
        BTNREGISTRO = findViewById(R.id.BTNREGISTRO);

        //UBICACIÓN
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(MainActivity.this.getAssets(),ubicacion);

        BTNLOGIN.setTypeface(Tf);
        BTNREGISTRO.setTypeface(Tf);


        // NOS DIRIGE A LA ACTIVIDAD LOGIN
        BTNLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        //NOS DIRIGE A LA ACTIVIDAD DE REGISTRO
        BTNREGISTRO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Registro.class);
                startActivity(intent);
            }
        });

    }
}
