package com.zerostar.forgetmenotnext.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zerostar.forgetmenotnext.R;

import java.io.ByteArrayOutputStream;

public class NuovaPianta extends Activity {

    private String id="";
    private Bitmap imageBitmap;
    //private ParseGeoPoint plant_location;
    private ParseObject plant_type;
    private ImageButton btn_fotocamera;
    private ImageButton btn_fatto;
    private EditText txt_my_nome;
    private final static int MY_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_pianta);

        //retrieve id pianta
        id =  getIntent().getExtras().getString("idPianta");
        Log.d("loggerello id",id);

        ParseQuery<ParseObject> query_find_pianta = ParseQuery.getQuery("Plants");
        query_find_pianta.whereEqualTo("objectId",id);
        try {
            plant_type = query_find_pianta.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        txt_my_nome = (EditText) findViewById((R.id.txt_nome_pianta));
        btn_fotocamera = (ImageButton) findViewById(R.id.btn_foto_pianta);
        btn_fotocamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_fotocamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i_fotocamera, MY_REQUEST_CODE);
            }
        });

        btn_fatto = (ImageButton) findViewById(R.id.btn_fatto);
        btn_fatto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_goto_giardino = new Intent(NuovaPianta.this, Giardino.class);
                String my_nome = txt_my_nome.getText().toString();
                ParseObject my_pianta = new ParseObject("MyPianta");
                my_pianta.put("createdBy", ParseUser.getCurrentUser());
                my_pianta.put("Nickname", my_nome);

                //Immagine (+ cast a bitmap)
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] parsed_image = stream.toByteArray();
                ParseFile avatar = new ParseFile("avatar.png", parsed_image);
                my_pianta.put("Avatar",avatar);

                //ParseGeoPoint
                //my_pianta.put("Location",plant_location);

                //Che Pianta è?
                my_pianta.put("Info",plant_type);
                my_pianta.saveInBackground();
                startActivity(i_goto_giardino);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");


                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Foto salvata in:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();

                btn_fotocamera.setImageBitmap(imageBitmap);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Errore: impossibile salvare la foto", Toast.LENGTH_LONG).show();
            }
        }


    }
}
