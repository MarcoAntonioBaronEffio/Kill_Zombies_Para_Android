package com.redsystem.juegodezombies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adaptador extends RecyclerView.Adapter<Adaptador.MyHolder> {

    private Context context;
    private List <Usuario> usuarioList;
    private Typeface tf;

    //CONSTRUCTOR
    public Adaptador(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
        this.tf = Typeface.createFromAsset(context.getAssets(),"fuentes/zombie.TTF");
    }

    @NonNull
    @Override
    //INFLAMOS EL DISEÑO
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.jugadores,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {

        /*OBTENER LOS DATOS DEL MODELO*/
        final String Imagen = usuarioList.get(i).getImagen();
        final String Nombres = usuarioList.get(i).getNombres();
        final String Correo = usuarioList.get(i).getEmail();
        final String Edad = usuarioList.get(i).getEdad();
        final String Pais = usuarioList.get(i).getPais();
        int Zombies = usuarioList.get(i).getZombies();
        /*CONVERSIÓN A STRING*/
        final String Z = String.valueOf(Zombies);

        //DATOS DEL JUGADOR
        holder.NombreJugador.setText(Nombres);
        holder.CorreoJugador.setText(Correo);
        holder.PuntajeJugador.setText(Z);
        holder.EdadJugador.setText(Edad);
        holder.PaisJugador.setText(Pais);

        /*CAMBIAR TIPO DE LETRA*/
        holder.NombreJugador.setTypeface(tf);
        holder.CorreoJugador.setTypeface(tf);
        holder.PuntajeJugador.setTypeface(tf);
        /*CAMBIAR TIPO DE LETRA*/

        //IMAGEN DEL JUGADOR
        try {
            //SI EL USUARIO TIENE FOTO DE PERFIL
            Picasso.get().load(Imagen).into(holder.ImagenJugador);

        }catch (Exception e){
            //SI NO TIENE FOTO DE PERFIL
        }

        /*AL HACER CLICK EN UN JUGADOR*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetalleJugador.class);

                intent.putExtra("Imagen",Imagen);
                intent.putExtra("Nombres",Nombres);
                intent.putExtra("Correo",Correo);
                intent.putExtra("Puntaje",Z);
                intent.putExtra("Pais",Pais);
                intent.putExtra("Edad", Edad);

                context.startActivity(intent);


                //Toast.makeText(context, ""+Correo, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        CircleImageView ImagenJugador;
        TextView NombreJugador, CorreoJugador, PuntajeJugador, EdadJugador, PaisJugador;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //INICIALIZAR
            ImagenJugador = itemView.findViewById(R.id.ImagenJugador);
            NombreJugador = itemView.findViewById(R.id.NombreJugador);
            CorreoJugador = itemView.findViewById(R.id.CorreoJugador);
            PuntajeJugador = itemView.findViewById(R.id.PuntajeJugador);
            EdadJugador = itemView.findViewById(R.id.EdadJugador);
            PaisJugador = itemView.findViewById(R.id.PaisJugador);

        }
    }

}
