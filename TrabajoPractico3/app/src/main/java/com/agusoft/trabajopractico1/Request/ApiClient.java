package com.agusoft.trabajopractico1.Request;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.agusoft.trabajopractico1.Modelo.Usuario;
import com.agusoft.trabajopractico1.Registro.RegistroActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ApiClient {
    private static SharedPreferences sp;
    public Context context;

    private static SharedPreferences conectar(Context context){
        if(sp==null){
            sp=context.getSharedPreferences("Datos.xml",0);
        }
        return sp;
    }
    public static void guardar(Context context, Usuario usuario){
        SharedPreferences sp = conectar(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("dni",usuario.getDni());
        editor.putString("apellido",usuario.getApellido());
        editor.putString("nombre",usuario.getNombre());
        editor.putString("mail",usuario.getMail());
        editor.putString("password",usuario.getPassword());
        editor.commit();
    }

    public static Usuario leer(Context context){
        SharedPreferences sp = conectar(context);
        Long dni = sp.getLong("dni",-1);
        String apellido = sp.getString("apellido","");
        String nombre = sp.getString("nombre","");
        String mail = sp.getString("mail","");
        String password = sp.getString("password","");

        Usuario us = new Usuario(dni,nombre, apellido,mail,password);
        return us;
    }
    public static Usuario login(Context context, String mail, String password){
        Usuario us = null;
        SharedPreferences sp = conectar(context);
        Long dni = sp.getLong("dni",-1);
        String apellido = sp.getString("apellido","");
        String nombre = sp.getString("nombre","");
        String mailU = sp.getString("mail","");
        String passwordU = sp.getString("password","");

        if(mail.equals(mailU)&& password.equals(passwordU)){
            us = new Usuario(dni,nombre,apellido,mailU,passwordU);
        }
        return us;
    }
}
