package com.agusoft.trabajopractico1.Login;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.agusoft.trabajopractico1.Modelo.Usuario;
import com.agusoft.trabajopractico1.Registro.RegistroActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivityViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> error;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }
    public LiveData<Boolean> getError(){
        if(error==null){
            error = new MutableLiveData<>();
        }
        return error;
    }

    public void login(String email,String password){

        if(email.equals(null) && password.equals(null)){
            Toast.makeText(context, "Ingresar valores validos.", Toast.LENGTH_SHORT).show();
        }else{
            File carpeta=context.getFilesDir();
            File archivo=new File(carpeta,email+".dat");
            try {
                FileInputStream fis = new FileInputStream(archivo);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);

                Usuario us= (Usuario) ois.readObject();

                if(us==null){
                    Toast.makeText(context, "Email o Contraseña incorrectas.", Toast.LENGTH_SHORT).show();
                    error.setValue(true);
                }else if(email.equals(us.getMail())&&password.equals(us.getPassword())){
                    Intent intent = new Intent(context, RegistroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("logueado",true);
                    intent.putExtra("email",email);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Email o Contraseña incorrectas.", Toast.LENGTH_SHORT).show();
                    error.setValue(true);
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(context,"Email o Contraseña incorrectas.",Toast.LENGTH_LONG).show();
                error.setValue(true);
            } catch (IOException e) {
                Toast.makeText(context,"Error E/S",Toast.LENGTH_LONG).show();
            } catch (ClassNotFoundException e) {
                Toast.makeText(context,"Error al de clase.",Toast.LENGTH_LONG).show();
            }

        }
    }

    public void registrarse(){
        Intent intent = new Intent(context, RegistroActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("logueado",false);
        context.startActivity(intent);
    }
}
