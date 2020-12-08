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
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    // DECLARAMOS VARIABLES
    EditText correoLogin, passLogin;
    Button BtnLogin;
    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // CONEXIÓN CON LA VISTA
        correoLogin = findViewById(R.id.correoLogin);
        passLogin = findViewById(R.id.passLogin);
        BtnLogin = findViewById(R.id.BtnLogin);
        auth = FirebaseAuth.getInstance();

        //UBICACIÓN
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Login.this.getAssets(),ubicacion);

        BtnLogin.setTypeface(Tf);

        // AL HACER CLICK EN EL BOTON DE LOGIN
        BtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = correoLogin.getText().toString();
                String pass = passLogin.getText().toString();

                    /*VALICIÓN PARA CORREO ELECTRÓNICO*/
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    correoLogin.setError("Correo inválido");
                    correoLogin.setFocusable(true);
                    /*VALIDACIÓN PARA LA CONTRASEÑA*/
                }else if(pass.length()<6){
                    passLogin.setError("Contraseña debe ser mayor a 6");
                    passLogin.setFocusable(true);
                }else{
                    LogeoDeJugador(email,pass);
                }
            }
        });

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Ingresando, espere por favor");
        progressDialog.setCancelable(false);

    }

    /*MÉTODO PARA LOGEO AL JUGADOR*/
    private void LogeoDeJugador(String email, String pass) {
        progressDialog.show();
         auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            startActivity(new Intent(Login.this,Menu.class));
                            assert user != null; // afirmamos que el usuario no es nulo
                            Toast.makeText(Login.this, "BIENVENIDO(A)"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //SI ES QUE FALLA EL LOGEO NOS MUESTRE UN MENSAJE
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
