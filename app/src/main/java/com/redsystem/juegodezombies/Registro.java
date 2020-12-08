package com.redsystem.juegodezombies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Registro extends AppCompatActivity {

    //DECLARADO LAS VARIABLES
    EditText correoEt,passEt,nombreEt,edadEt,paisEt;
    TextView fechaTxt;
    Button Registrar;
    FirebaseAuth auth; //FIREBASE AUTENTICACIÓN

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //CONEXIÓN CON LA VISTA
        correoEt = findViewById(R.id.correoEt);
        passEt = findViewById(R.id.passEt);
        nombreEt = findViewById(R.id.nombreEt);
        edadEt = findViewById(R.id.edadEt);
        paisEt = findViewById(R.id.paisEt);
        fechaTxt = findViewById(R.id.fechaTxt);
        Registrar = findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d  'de' MMMM 'del' yyyy" ); /*15 de Mayo del 2020*/
        final String StringFecha = fecha.format(date);
        fechaTxt.setText(StringFecha);

        //UBICACIÓN
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Registro.this.getAssets(),ubicacion);

        Registrar.setTypeface(Tf);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correoEt.getText().toString();
                String password = passEt.getText().toString();

                    /*VALICIÓN PARA CORREO ELECTRÓNICO*/
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoEt.setError("Correo inválido");
                    correoEt.setFocusable(true);
                    /*VALIDACIÓN PARA LA CONTRASEÑA*/
                }else if(password.length()<6){
                    passEt.setError("Contraseña debe ser mayor a 6");
                    correoEt.setFocusable(true);
                }else{
                    RegitrarJugador(email,password);
                }
            }
        });

        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);

    }

    /*MÉTODO ES PARA REGISTRAR UN JUGADOR*/
    private void RegitrarJugador(String email, String password) {
        progressDialog.show();
         auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*SI EL JUGADOR FUE REGISTRADO CORRECTAMENTE*/
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();

                            int contador = 0;

                            assert user != null;  /*QUE EL USUARIO NO ES NULO*/

                            /*STRINGS*/
                            String uidString = user.getUid();
                            String correoString = correoEt.getText().toString();
                            String passString = passEt.getText().toString();
                            String nombreString = nombreEt.getText().toString();
                            String edadString = edadEt.getText().toString();
                            String paisString = paisEt.getText().toString();
                            String fechaString = fechaTxt.getText().toString();

                            HashMap<Object,Object> DatosJUGADOR  = new HashMap<>();

                            DatosJUGADOR.put("Uid",uidString);
                            DatosJUGADOR.put("Email",correoString);
                            DatosJUGADOR.put("Password",passString);
                            DatosJUGADOR.put("Nombres",nombreString);
                            DatosJUGADOR.put("Edad",edadString);
                            DatosJUGADOR.put("Pais",paisString);
                            DatosJUGADOR.put("Imagen","");
                            DatosJUGADOR.put("Fecha",fechaString);
                            DatosJUGADOR.put("Zombies",contador);

                            FirebaseDatabase database = FirebaseDatabase.getInstance(); //INSTANCIA
                            DatabaseReference reference = database.getReference("MI DATA BASE JUGADORES"); /*NOMBRE DE BD*/
                            reference.child(uidString).setValue(DatosJUGADOR);
                            startActivity(new Intent(Registro.this,Menu.class));
                            Toast.makeText(Registro.this, "USUARIO REGISTRADO EXITOSAMENTE", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Registro.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                /*SI FALLA EL REGISTRO*/
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
