package com.redsystem.juegodezombies;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class Menu extends AppCompatActivity {

    FirebaseAuth auth ;
    FirebaseUser user;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference JUGADORES;

    Dialog dialog;

    TextView MiPuntuaciontxt;
    TextView Zombies,uid,correo,nombre,edad,pais,fecha,Menutxt;
    Button JugarBtn,EditarBtn, CambiarPassBtn, PuntuacionesBtn,AcercaDeBtn,CerrarSesion;
    CircleImageView imagenPerfil;

    private StorageReference RefereciaDeAlmacenamiento ;
    private String RutaAlmacenamiento = "FotosDePerfil/*";

    /*PERMISOS*/
    private static final int CODIGO_DE_SOLICUTUD_DE_ALMACENAMIENTO = 200;
    private static final int CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN = 300;

    /*MATRICES*/
    private String [] PermisosDeAlmacenamiento;
    private Uri imagen_uri;
    private String perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        JUGADORES = firebaseDatabase.getReference("MI DATA BASE JUGADORES");

        dialog = new Dialog(Menu.this);

        RefereciaDeAlmacenamiento = FirebaseStorage.getInstance().getReference();
        PermisosDeAlmacenamiento = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //UBICACIÓN
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);

        MiPuntuaciontxt = findViewById(R.id.MiPuntuaciontxt);

        //PERFIL
        imagenPerfil = findViewById(R.id.imagenPerfil);
        Zombies = findViewById(R.id.Zombies);
        uid = findViewById(R.id.uid);
        correo = findViewById(R.id.correo);
        fecha = findViewById(R.id.fecha);
        nombre = findViewById(R.id.nombre);
        edad = findViewById(R.id.edad);
        pais = findViewById(R.id.pais);

        Menutxt = findViewById(R.id.Menutxt);

        //OPCIONES DEL JUEGO
        JugarBtn = findViewById(R.id.JugarBtn);
        EditarBtn = findViewById(R.id.EditarBtn);
        CambiarPassBtn = findViewById(R.id.CambiarPassBtn);
        PuntuacionesBtn = findViewById(R.id.PuntuacionesBtn);
        AcercaDeBtn = findViewById(R.id.AcercaDeBtn);
        CerrarSesion = findViewById(R.id.CerrarSesionBtn);

        MiPuntuaciontxt.setTypeface(Tf);
        uid.setTypeface(Tf);
        correo.setTypeface(Tf);
        nombre.setTypeface(Tf);
        edad.setTypeface(Tf);
        pais.setTypeface(Tf);
        Zombies.setTypeface(Tf);
        Menutxt.setTypeface(Tf);
        fecha.setTypeface(Tf);

        /*CAMBIO DE FUENTE DE LETRA*/
        JugarBtn.setTypeface(Tf);
        EditarBtn.setTypeface(Tf);
        CambiarPassBtn.setTypeface(Tf);
        PuntuacionesBtn.setTypeface(Tf);
        AcercaDeBtn.setTypeface(Tf);
        CerrarSesion.setTypeface(Tf);

        JugarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "JUGAR", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Menu.this,EscenarioJuego.class);

                String UidS = uid.getText().toString();
                String NombreS = nombre.getText().toString();
                String ZombieS = Zombies.getText().toString();

                intent.putExtra("UID",UidS);
                intent.putExtra("NOMBRE",NombreS);
                intent.putExtra("ZOMBIE",ZombieS);

                startActivity(intent);
                Toast.makeText(Menu.this, "ENVIANDO PARÁMETROS", Toast.LENGTH_SHORT).show();
            }
        });

        EditarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(Menu.this, "EDITAR", Toast.LENGTH_SHORT).show();
                EditarDatos();
            }
        });

        CambiarPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Menu.this,CambioDePass.class));
                Toast.makeText(Menu.this, "CAMBIAR CONTRASEÑA", Toast.LENGTH_SHORT).show();
            }
        });

        PuntuacionesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Menu.this, "PUNTUACIONES", Toast.LENGTH_SHORT).show();
            }
        });

        AcercaDeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcercaDe();
                //Toast.makeText(Menu.this, "ACERCA DE", Toast.LENGTH_SHORT).show();
            }
        });

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
            }
        });
    }

    private void AcercaDe() {

        //UBICACIÓN
        String ubicacion = "fuentes/zombie.TTF";
        Typeface Tf = Typeface.createFromAsset(Menu.this.getAssets(),ubicacion);


        TextView DesarrolladorPOTXT, DevTXT;
        Button OK;

        dialog.setContentView(R.layout.acerca_de);

        DesarrolladorPOTXT = dialog.findViewById(R.id.DesarrolladorPOTXT);
        DevTXT = dialog.findViewById(R.id.DevTXT);
        OK = dialog.findViewById(R.id.OK);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        DesarrolladorPOTXT.setTypeface(Tf);
        DevTXT.setTypeface(Tf);

        dialog.show();
    }

    /*METODO PARA CAMBIAR LOS DATOS*/
    private void EditarDatos() {
        //DEFINIDO EL ARREGLO CON LAS OPCIONES QUE PODREMOS ELEGIR
        String [] Opciones = {"Foto de perfil","Cambiar edad","Cambiar pais"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(Opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    perfil = "Imagen";
                    ActualizarFotoPerfil();
                }

                if (i == 1){
                    ActualizarEdad("Edad");
                }

                if (i == 2){
                    ActualizarPais("Pais");
                }
            }
        });
        builder.create().show();

    }

    private void ActualizarFotoPerfil() {
        String [] opciones = {"Galeria"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen de: ");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if ( i  == 0){
                    //SELECCIONÓ DE GALERIA
                    if (!ComprobarPermisoAlmacenamiento()){
                        //SI NO SE HABILITÓ EL PERMISO
                        SolicitarPermisoAlmacenamiento();
                    }else{
                        //SI SE HABILITÓ EL PERMISO
                        ELegirImagenDeGaleria();
                    }
                }
            }
        });
        builder.create().show();

    }

    //PERMISO DE ALMACENAMIENTO EN TIEMPO DE EJECUCIÓN
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void SolicitarPermisoAlmacenamiento() {
        requestPermissions(PermisosDeAlmacenamiento,CODIGO_DE_SOLICUTUD_DE_ALMACENAMIENTO);
    }

    //COMPRUEBA SI LOS PERMISOS DE ALMACENAMIENTO ESTAN HABILITADOS O NO
    private boolean ComprobarPermisoAlmacenamiento() {

        boolean resultado = ContextCompat.checkSelfPermission(Menu.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultado;

    }

    //SE LLAMA CUANDO EL USUARIO O JUGADOR PRESIONA PERMITIR O DENEGAR EL CUADRO DE DIALOGO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case CODIGO_DE_SOLICUTUD_DE_ALMACENAMIENTO:{
                //SELECION DE LA GALERIA
                if (grantResults.length > 0){
                    boolean EscrituraDeAlmacenamientoAceptado = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (EscrituraDeAlmacenamientoAceptado){
                        //PERMISO GUE HABILITADO
                        ELegirImagenDeGaleria();
                    }else {
                        // SI EL USUARIO DIJO QUE NO
                        Toast.makeText(this, "HABILITE EL PERMISO DE A LA GALERIA", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //SE LLAMA CUANDO EL JUGADOR YA HA ELEGIDO LA IMAGEN DE LA GALERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            //DE LA IMAGEN VAMOS A OBTENER LA URI
            if (requestCode == CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN){
                imagen_uri = data.getData();
                SubirFoto(imagen_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //ESTE METODO CAMBIA LA FOTO DE PERFIL DEL JUGADOR Y ACTUALIZA LA INFORMACION EN LA BASE DE DATOS DE FIREBASE
    private void SubirFoto(Uri imagen_uri) {
        String RutaDeArchivoYNombre = RutaAlmacenamiento + "" +perfil+"_"+user.getUid();
        StorageReference storageReference = RefereciaDeAlmacenamiento.child(RutaDeArchivoYNombre);
        storageReference.putFile(imagen_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloaduri = uriTask.getResult(); //CORREGIR

                            if (uriTask.isSuccessful()) {
                                HashMap<String, Object> resultado = new HashMap<>();
                                resultado.put(perfil, downloaduri.toString());
                                JUGADORES.child(user.getUid()).updateChildren(resultado)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(Menu.this, "LA IMAGEN A SIDO CAMBIADA CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Menu.this, "HA OCURRIDO UN ERROR", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(Menu.this, "ALGO HA SALIDO MAL", Toast.LENGTH_SHORT).show();
                            }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Menu.this, "ALGO HA SALIDO MAL", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //ESTE METODO ABRE LA GALERIA
    private void ELegirImagenDeGaleria() {
        Intent IntentGaleria = new Intent(Intent.ACTION_PICK);
        IntentGaleria.setType("image/*");
        startActivityForResult(IntentGaleria, CODIGO_PARA_LA_SELECCION_DE_LA_IMAGEN);
    }

    private void ActualizarEdad(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //SI EL USUARIO HACE CLIK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    private void ActualizarPais(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar: "+key);
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this);
        linearLayoutCompat.setOrientation(LinearLayoutCompat.VERTICAL);
        linearLayoutCompat.setPadding(10,10,10,10);
        final EditText editText = new EditText(this);
        editText.setHint("Ingrese "+key);
        linearLayoutCompat.addView(editText);
        builder.setView(linearLayoutCompat);
        //SI EL USUARIO HACE CLIK EN ACTUALIZAR
        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value = editText.getText().toString().trim();
                HashMap<String,Object> result = new HashMap<>();
                result.put(key,value);
                JUGADORES.child(user.getUid()).updateChildren(result)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Menu.this, "DATO ACTUALIZADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Menu.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Menu.this, "CANCELADO POR EL USUARIO", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    // ESTE METODO SE EJECUTA CUANDO SE ABRE EL MINIJUEGO
    @Override
    protected void onStart() {
        UsuarioLogueado();
        super.onStart();
    }

    //METODO COMPRUEBA SI EL JUGADOR HA INICIADO SESIÓN
    private void UsuarioLogueado(){

        if (user != null){
            Consulta();
            Toast.makeText(this, "Jugador en linea", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(Menu.this,MainActivity.class));
            finish();
        }

    }

    //MÉTODO ES PARA CERRAR SESIÓN
    private void CerrarSesion(){
        auth.signOut();
        startActivity(new Intent(Menu.this,MainActivity.class));
        Toast.makeText(this, "Cerrado sesión exitosamente", Toast.LENGTH_SHORT).show();
    }

    /*MÉTODO PARA REALIZAR LA CONSULTA*/
    private void Consulta(){
        /*CONSULTA*/
        Query query = JUGADORES.orderByChild("Email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    //OBTENCIÓN DE DATOS
                    String zombiesString = ""+ds.child("Zombies").getValue();
                    String uidString = ""+ds.child("Uid").getValue();
                    String emailString = ""+ds.child("Email").getValue();
                    String nombreString = ""+ds.child("Nombres").getValue();
                    String edadString = ""+ds.child("Edad").getValue();
                    String paisString = ""+ds.child("Pais").getValue();
                    String imagen = ""+ds.child("Imagen").getValue();
                    String fechaString = ""+ds.child("Fecha").getValue();

                    //SETEO DE DATOS EN LOS TEXTVIEW
                    Zombies.setText(zombiesString);
                    uid.setText(uidString);
                    correo.setText("Correo: "+emailString);
                    nombre.setText("Nombres: "+nombreString);
                    edad.setText("Edad: "+edadString);
                    pais.setText("Pais: "+paisString);
                    fecha.setText("Se registro: "+fechaString);


                    try {
                        //SI EXISTE IMAGEN EN LA BASE DE DATOS
                        Picasso.get().load(imagen).into(imagenPerfil);
                    }catch (Exception e){
                        // SI NO EXISTE IMAGEN EN LA BASE DE DATOS
                        Picasso.get().load(R.drawable.soldado).into(imagenPerfil);
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
