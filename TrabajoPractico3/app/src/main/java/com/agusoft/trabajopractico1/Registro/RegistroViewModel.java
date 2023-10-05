package com.agusoft.trabajopractico1.Registro;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.agusoft.trabajopractico1.Login.MainActivity;
import com.agusoft.trabajopractico1.Login.MainActivityViewModel;
import com.agusoft.trabajopractico1.Modelo.Usuario;
import com.agusoft.trabajopractico1.R;
import com.agusoft.trabajopractico1.Request.ApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegistroViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Bitmap> fotoM;
    private MutableLiveData<Bitmap> foto;
    private MutableLiveData<Boolean> registro;
    private MutableLiveData<Usuario> userM;
    public RegistroViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }
    public LiveData<Usuario> getUserM(){
        if(userM==null){
            userM = new MutableLiveData<>();
        }
        return userM;
    }

    public void guardar(Usuario us){
        File carpeta=context.getFilesDir();
        File archivo=new File(carpeta,us.getMail()+".dat");

        try {
            FileOutputStream fos=new FileOutputStream(archivo);
            BufferedOutputStream bos=new BufferedOutputStream(fos);
            ObjectOutputStream ous=new ObjectOutputStream(bos);

            ous.writeObject(us);
            bos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            Toast.makeText(context,"Error al guardar",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context,"Error E/S",Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public LiveData<Bitmap> getFoto(){
        if(foto==null){
            foto=new MutableLiveData<>();
        }
        return foto;
    }

    public void respuetaDeCamara(int requestCode, int resultCode, @Nullable Intent data, int REQUEST_IMAGE_CAPTURE ,Usuario usuarioActual){
        Log.d("salida",requestCode+"");
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Recupero los datos provenientes de la camara.
            Bundle extras = data.getExtras();
            //Casteo a bitmap lo obtenido de la camara.
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //Rutina para optimizar la foto,
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            foto.setValue(imageBitmap);



            //Rutina para convertir a un arreglo de byte los datos de la imagen
            byte [] b=baos.toByteArray();


            //Aquí podría ir la rutina para llamar al servicio que recibe los bytes.
            Log.d("salida",usuarioActual.getDni()+"dniusuario");
            File archivo =new File(context.getFilesDir(),usuarioActual.getDni() +".png");

            usuarioActual.setFoto(usuarioActual.getDni()+".png");
            Log.d("salida",usuarioActual.getFoto());
            if(archivo.exists()){
                archivo.delete();
            }
            try {
                FileOutputStream fo=new FileOutputStream(archivo);
                BufferedOutputStream bo=new BufferedOutputStream(fo);
                bo.write(b);
                bo.flush();
                bo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //USUARIO LOGUEADO

    public void logeado(Bundle bundle){
        Boolean log = bundle.getBoolean("logueado");
        String email= bundle.getString("email");
        if(log){
            File carpeta=context.getFilesDir();
            File archivo=new File(carpeta,email+".dat");

            try {
                FileInputStream fis = new FileInputStream(archivo);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream ois = new ObjectInputStream(bis);

                Usuario us= (Usuario) ois.readObject();
                userM.setValue(us);
                fis.close();

            } catch (FileNotFoundException e) {
                Toast.makeText(context,"Error al leer.",Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(context,"Error E/S",Toast.LENGTH_LONG).show();
            } catch (ClassNotFoundException e) {
                Toast.makeText(context,"Error de clases",Toast.LENGTH_LONG).show();
            }
        }else{
            registro.setValue(true);
        }
    }
    public LiveData<Boolean> getRegistro(){
        if(registro==null){
            registro=new MutableLiveData<>();
        }
        return registro;
    }
    public LiveData<Bitmap> getFotoM(){
        if(fotoM==null){
            fotoM=new MutableLiveData<>();
        }
        return fotoM;

    }
    public void cargar2(String archivos) {
        if (archivos == null) {
            Toast.makeText(context, "sin imagen", Toast.LENGTH_SHORT).show();
        } else {


            File archivoF = new File(context.getFilesDir(), archivos);

            try {
                FileInputStream fs = new FileInputStream(archivoF);
                BufferedInputStream bs = new BufferedInputStream(fs);

                byte b[];
                b = new byte[bs.available()];
                bs.read(b);

                Bitmap BM = BitmapFactory.decodeByteArray(b, 0, b.length);
                this.fotoM.setValue(BM);


                bs.close();
                fs.close();

            } catch (FileNotFoundException e) {
                Log.d("salida", e.toString());
            } catch (IOException e) {
                Log.d("salida", e.toString());
            }


        }
    }

}
