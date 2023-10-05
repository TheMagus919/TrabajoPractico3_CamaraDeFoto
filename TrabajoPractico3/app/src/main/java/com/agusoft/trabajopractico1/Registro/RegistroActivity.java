package com.agusoft.trabajopractico1.Registro;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.agusoft.trabajopractico1.Modelo.Usuario;
import com.agusoft.trabajopractico1.databinding.RegistroBinding;

public class RegistroActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private RegistroViewModel rv;
    private RegistroBinding binding;
    private Usuario usuarioActual=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rv = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(RegistroViewModel.class);
        Bundle bundle = getIntent().getExtras();
        rv.getUserM().observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                binding.edDniR.setText(usuario.getDni()+"");
                binding.edNombreR.setText(usuario.getNombre());
                binding.edApellidoR.setText(usuario.getApellido());
                binding.edEmailR.setText(usuario.getMail());
                binding.edPasswordR.setText(usuario.getPassword());
                usuarioActual=usuario;
                rv.cargar2(usuario.getDni()+".png");
            }
        });
        rv.getFotoM().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.image1.setImageBitmap(bitmap);
            }
        });
        rv.getFoto().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                binding.image1.setImageBitmap(bitmap);
            }
        });
        rv.getRegistro().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.image1.setVisibility(View.INVISIBLE);
                binding.btTomarFoto.setVisibility(View.INVISIBLE);
            }
        });
        binding.btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dni = binding.edDniR.getText().toString();
                Usuario user = new Usuario( Long.parseLong(dni),binding.edNombreR.getText().toString(),binding.edApellidoR.getText().toString(),binding.edEmailR.getText().toString(),binding.edPasswordR.getText().toString());
                rv.guardar(user);
            }
        });
        rv.logeado(bundle);
    }
    public void tomarFoto(View v){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        rv.respuetaDeCamara(requestCode,resultCode,data,1, usuarioActual);
    }

}
