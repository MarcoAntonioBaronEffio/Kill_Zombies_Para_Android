package com.redsystem.juegodezombies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class EscenarioJuego extends AppCompatActivity {

    String UIDS, NOMBRES, ZOMBIES;

    TextView TvContador, TvNombre, TvTiempo;
    ImageView IvZombie;

    TextView AnchoTv, AltoTv;

    int AnchoPantalla;
    int AltoPantalla;

    Random aleatorio;

    boolean GameOver = false;
    Dialog miDialog;

    int contador = 0;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escenario_juego);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //VERTICAL

        IvZombie = findViewById(R.id.IvZombie);

        TvContador = findViewById(R.id.TvContador);
        TvNombre = findViewById(R.id.TvNombre);
        TvTiempo = findViewById(R.id.TvTiempo);

        miDialog = new Dialog(EscenarioJuego.this);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("MI DATA BASE JUGADORES");

        Bundle intent = getIntent().getExtras();
        UIDS = intent.getString("UID");
        NOMBRES = intent.getString("NOMBRE");
        ZOMBIES = intent.getString("ZOMBIE");

        AnchoTv = findViewById(R.id.AnchoTv);
        AltoTv = findViewById(R.id.AltoTv);

        TvNombre.setText(NOMBRES);
        TvContador.setText(ZOMBIES);

        Pantalla();
        CuentaAtras();

        //AL HACER CLICK EN LA IMAGEN
        IvZombie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!GameOver) {
                    contador++; //AUMENTE DE 1 EN 1
                    TvContador.setText(String.valueOf(contador)); //SETEAMOS EL VALOR , STRING
                    IvZombie.setImageResource(R.drawable.zombieaplastado);

                    //NOS PERMITE EJECUTAR
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //AQUI ES LO EJECUTA
                            IvZombie.setImageResource(R.drawable.zombie);
                            Movimiento();
                        }
                    }, 500);
                }
            }
        });


    }

    //PARA OBTENER TAMAÑO DE PANTALLA
    private void Pantalla() {

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        AnchoPantalla = point.x;
        AltoPantalla = point.y;

        String ANCHOS = String.valueOf(AnchoPantalla);
        String ALTOS = String.valueOf(AltoPantalla);

        AnchoTv.setText(ANCHOS);
        AltoTv.setText(ALTOS);


        aleatorio = new Random();

    }

    private void Movimiento() {

        int min = 0;

        /*MAXIMO COORDENADA X*/
        int MaximoX = AnchoPantalla - IvZombie.getWidth();
        /*MAXIMO COORDENADA Y*/
        int MaximoY = AltoPantalla - IvZombie.getHeight();

        int randomX = aleatorio.nextInt(((MaximoX - min) + 1) + min);
        int randomY = aleatorio.nextInt(((MaximoY - min) + 1) + min);

        IvZombie.setX(randomX);
        IvZombie.setY(randomY);

    }

    //METODO PARA RETROCEDER EL TIEMPO
    private void CuentaAtras() {

        new CountDownTimer(10000, 1000) {

            //SE EJECUTA CADA SEGUNDO
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                TvTiempo.setText(segundosRestantes + "S");
            }

            //CUANDO SE ACABA EL TIEMPO
            public void onFinish() {
                TvTiempo.setText("0S");
                GameOver = true;
                MensajeGameOver();
                GuardarResultados("Zombies", contador);
            }
        }.start();
    }

    public void MensajeGameOver() {

        String ubicacion = "fuentes/zombie.TTF";
        Typeface typeface = Typeface.createFromAsset(EscenarioJuego.this.getAssets(), ubicacion);

        TextView SeacaboTXT, HasmatadoTXT, NumeroTXT;
        Button JUGARDENUEVO, IRMENU, PUNTAJES;

        miDialog.setContentView(R.layout.gameover);

        SeacaboTXT = miDialog.findViewById(R.id.SeacaboTXT);
        HasmatadoTXT = miDialog.findViewById(R.id.HasmatadoTXT);
        NumeroTXT = miDialog.findViewById(R.id.NumeroTXT);

        JUGARDENUEVO = miDialog.findViewById(R.id.JUEGARDENUEVO);
        IRMENU = miDialog.findViewById(R.id.IRMENU);
        PUNTAJES = miDialog.findViewById(R.id.PUNTAJES);

        String zombies = String.valueOf(contador);
        NumeroTXT.setText(zombies);


        SeacaboTXT.setTypeface(typeface);
        HasmatadoTXT.setTypeface(typeface);
        NumeroTXT.setTypeface(typeface);

        JUGARDENUEVO.setTypeface(typeface);
        IRMENU.setTypeface(typeface);
        PUNTAJES.setTypeface(typeface);

        JUGARDENUEVO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contador = 0;
                miDialog.dismiss();
                TvContador.setText("0");
                GameOver = false;
                CuentaAtras();
                Movimiento();
            }
        });

        IRMENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EscenarioJuego.this, Menu.class));
                finish();
            }
        });

        PUNTAJES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EscenarioJuego.this,Puntajes.class));
                //Toast.makeText(EscenarioJuego.this, "PUNTAJES", Toast.LENGTH_SHORT).show();
            }
        });

        miDialog.show();
        miDialog.setCancelable(false);

    }

    private void GuardarResultados(String key, int zombies) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(key, zombies);
        JUGADORES.child(user.getUid()).updateChildren(hashMap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EscenarioJuego.this, "El puntaje ha sido actualizado", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}