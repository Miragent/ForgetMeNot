package com.zerostar.forgetmenotnext.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zerostar.forgetmenotnext.R;
import com.zerostar.forgetmenotnext.utils.Util;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NuovaPianta extends Activity {

    private String id="";
    private Bitmap imageBitmap;
    //private ParseGeoPoint plant_location;
    private ParseObject plant_type;
    private ImageButton btn_fotocamera;
    private ImageButton btn_sensore_luce;
    private ImageButton btn_fatto;
    private boolean is_luce_ok = false;
    private TextView txt_posto;
    private EditText txt_my_nome;
    private final static int CAMERA_REQUEST_CODE = 100;
    private final static int LUCE_REQUEST_CODE = 300;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_pianta);

        //retrieve id pianta
        id =  getIntent().getExtras().getString("idPianta");
        Log.d("loggerello id", id);

        ParseQuery<ParseObject> query_find_pianta = ParseQuery.getQuery("Plants");
        query_find_pianta.whereEqualTo("objectId",id);
        try {
            plant_type = query_find_pianta.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        txt_my_nome = (EditText) findViewById((R.id.txt_nome_pianta));


        // Bottone fotocamera
        btn_fotocamera = (ImageButton) findViewById(R.id.btn_foto_pianta);
        btn_fotocamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_fotocamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i_fotocamera, CAMERA_REQUEST_CODE);
            }
        });

        txt_posto = (TextView) findViewById(R.id.label_posto);

        // Bottone luce

        btn_sensore_luce = (ImageButton) findViewById(R.id.btn_luce);
        btn_sensore_luce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_sensore_luce = new Intent(NuovaPianta.this, MenuSensore.class);
                int esposizione = plant_type.getInt("Luce");
                i_sensore_luce.putExtra("Esposizione",esposizione);
                startActivityForResult(i_sensore_luce, LUCE_REQUEST_CODE);
            }
        });

        //Bottone fatto
        btn_fatto = (ImageButton) findViewById(R.id.btn_fatto);
        btn_fatto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                util = new Util(NuovaPianta.this);
                if(!util.isConnected()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Connessione a internet assente",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                ParseObject my_pianta = new ParseObject("MyPianta");

                String my_nome = txt_my_nome.getText().toString();
                Log.d("Nomerino Planterino:", " "+ my_nome);

                if (my_nome.equals("")){
                    Toast.makeText(NuovaPianta.this, "Dai un nick alla tua pianta!", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    //Nickname
                    my_pianta.put("createdBy", ParseUser.getCurrentUser());
                    my_pianta.put("Nickname", my_nome);
                }

               if (imageBitmap==null){
                   Toast.makeText(NuovaPianta.this, "Devi specificare un avatar per la tua pianta!", Toast.LENGTH_LONG).show();
                   return;
               }else {
                   //Immagine (+ cast a bitmap)
                   ByteArrayOutputStream stream = new ByteArrayOutputStream();
                   imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                   byte[] parsed_image = stream.toByteArray();
                   ParseFile avatar = new ParseFile("avatar.png", parsed_image);
                   my_pianta.put("Avatar", avatar);
               }

                //ParseGeoPoint
                //my_pianta.put("Location",plant_location);

                //Che Pianta è?
                Intent i_goto_giardino = new Intent(NuovaPianta.this, Giardino.class);
                my_pianta.put("Info", plant_type);
                my_pianta.saveInBackground();


                //Set the initial notify
                HashMap<String, Object> params = new HashMap<String, Object>();
                ParseUser current_user = ParseUser.getCurrentUser();
                Calendar c = Calendar.getInstance();
               // c.add(Calendar.DATE,30);
                Date schedule_time = c.getTime();


                params.put("userID", current_user.getObjectId());
                params.put("plantNick", my_nome);
                params.put("scheduleTime", schedule_time);
                ParseCloud.callFunctionInBackground("schedule", params, new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        if (e == null) {
                            Log.d("Ciao sono il cloud:", object.toString());
                        }
                    }
                });


                // Go to giardino
                startActivity(i_goto_giardino);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        // Capture camera result
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");


                // Image captured and saved
                Toast.makeText(this, "Foto salvata!", Toast.LENGTH_LONG).show();

                btn_fotocamera.setImageBitmap(imageBitmap);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Errore: impossibile salvare la foto", Toast.LENGTH_LONG).show();
            }
        }

        // Capture file result

        // Capture luce result
        if (requestCode == LUCE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                is_luce_ok = true;
                txt_posto.setText("L'esposizione trovata è perfetta!");
            }
            else {
                is_luce_ok = false;
                txt_posto.setText("Attenzione, l'esposizione trovata non è ottimale!");
            }
        }
    }
}
