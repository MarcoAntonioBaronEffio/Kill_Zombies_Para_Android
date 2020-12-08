package com.redsystem.juegodezombies;

public class Usuario {

    String Edad, Email, Fecha, Imagen, Nombres, Pais, Password, Uid;
    int Zombies;

    public Usuario() {

    }

    public Usuario(String edad, String email, String fecha, String imagen, String nombres, String pais, String password, String uid, int zombies) {
        Edad = edad;
        Email = email;
        Fecha = fecha;
        Imagen = imagen;
        Nombres = nombres;
        Pais = pais;
        Password = password;
        Uid = uid;
        Zombies = zombies;
    }

    public String getEdad() {
        return Edad;
    }

    public void setEdad(String edad) {
        Edad = edad;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getPais() {
        return Pais;
    }

    public void setPais(String pais) {
        Pais = pais;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public int getZombies() {
        return Zombies;
    }

    public void setZombies(int zombies) {
        Zombies = zombies;
    }
}
