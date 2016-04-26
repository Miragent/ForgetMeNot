package com.zerostar.forgetmenotnext.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class NuovaPianta extends Activity {

    private String id="";
    private Bitmap imageBitmap;
    //private ParseGeoPoint plant_location;
    private ParseObject plant_type;
    private ImageView img_foto_pianta;
    private ImageButton btn_fotocamera;
    private ImageButton btn_file;
    private ImageButton btn_sensore_luce;
    private ImageButton btn_fatto;
    private boolean is_luce_ok = false;
    private boolean is_notifiche_ok = false;

    private TextView txt_posto;
    private EditText txt_my_nome;
    private final static int CAMERA_REQUEST_CODE = 100;
    private final static int FILE_REQUEST_CODE = 200;
    private final static int LUCE_REQUEST_CODE = 300;
    private Util util;
    private String my_nome;

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

        img_foto_pianta = (ImageView) findViewById(R.id.img_foto_pianta);
        txt_my_nome = (EditText) findViewById((R.id.txt_nome_pianta));
        txt_posto = (TextView) findViewById(R.id.label_posto);

        // Bottone fotocamera
        btn_fotocamera = (ImageButton) findViewById(R.id.btn_camera);
        btn_fotocamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_fotocamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /*
                i_fotocamera.putExtra("crop", "true");
                i_fotocamera.putExtra("aspectX", 1);
                i_fotocamera.putExtra("aspectY",1);
                i_fotocamera.putExtra("outputX",96);
                i_fotocamera.putExtra("outputY",96);
                i_fotocamera.putExtra("return-data",true);
                */
                startActivityForResult(i_fotocamera, CAMERA_REQUEST_CODE);
            }
        });

        // Bottone file
        btn_file = (ImageButton) findViewById(R.id.btn_file);
        btn_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_galleria = new Intent();
                i_galleria.setType("image/*");
                i_galleria.setAction(Intent.ACTION_GET_CONTENT);

                /*
                i_galleria.putExtra("crop", "true");
                i_galleria.putExtra("aspectX", 1);
                i_galleria.putExtra("aspectY",1);
                i_galleria.putExtra("outputX",96);
                i_galleria.putExtra("outputY",96);
                i_galleria.putExtra("return-data",true);
                */
                startActivityForResult(Intent.createChooser(i_galleria,"Scegli Foto"),FILE_REQUEST_CODE);

            }
        });
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

                my_nome = txt_my_nome.getText().toString();
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
                my_pianta.put("Info", plant_type);
                my_pianta.put("IsAcquaOk",true);
                my_pianta.put("IsTerraOk",true);
                my_pianta.put("IsLuceOk",is_luce_ok);
                my_pianta.saveInBackground();

                //Chiediamo se vuole ricevere notifiche

                AlertDialog.Builder builder = new AlertDialog.Builder(NuovaPianta.this);
                builder.setMessage("Vuoi ricevere le notifiche relative a "+my_nome+"?").setTitle("Notifiche");
                builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, Object> params = new HashMap<String, Object>();
                        ParseUser current_user = ParseUser.getCurrentUser();
                        Calendar c = Calendar.getInstance();
                        // c.add(Calendar.DATE,30);

                        // Notifica di benvenuto
                        Date schedule_time = c.getTime();
                        params.put("userID", current_user.getObjectId());
                        params.put("plantNick", my_nome);
                        params.put("scheduleTime", schedule_time);
                        params.put("scheduleText", "Ciao, sono " + my_nome + " e ti avvertirò ogni volta che avrò bisogno delle tue attenzioni. Trattami bene!");
                        ParseCloud.callFunctionInBackground("schedule", params, new FunctionCallback<Object>() {
                            @Override
                            public void done(Object object, ParseException e) {
                                if (e == null) {
                                    Log.d("Ciao sono il cloud:", object.toString());
                                }
                            }
                        });

                        // Query per le info
                        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Plants");
                        query.whereEqualTo("objectId", id);

                        //Notifica acqua
                        try {
                            ParseObject po = query.getFirst();
                            int next_acqua = po.getInt("Acqua");
                            c = Calendar.getInstance();
                            c.add(Calendar.DATE, next_acqua);
                            schedule_time = c.getTime();
                            HashMap<String, Object> p_acqua = new HashMap<String, Object>();
                            p_acqua.put("userID", current_user.getObjectId());
                            p_acqua.put("plantNick", my_nome);
                            p_acqua.put("scheduleTime", schedule_time);
                            p_acqua.put("scheduleText", "Ciao, sono " + my_nome + ". Ho bisogno di essere innaffiata!");
                            ParseCloud.callFunctionInBackground("schedule", p_acqua, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object object, ParseException e) {
                                    if (e == null) {
                                        Log.d("Ciao sono il cloud:", object.toString());
                                    }
                                }
                            });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        //Notifica fertilizzante
                        try {
                            ParseObject po = query.getFirst();
                            int next_terra = po.getInt("Fertilizzante");
                            if (next_terra != 0) {
                                //PER DEBUG COMMENTARE LA LINEA SOTTO
                                c = Calendar.getInstance();
                                c.add(Calendar.DATE, next_terra);
                                //
                                schedule_time = c.getTime();
                                HashMap<String, Object> p_terra = new HashMap<String, Object>();
                                p_terra.put("userID", current_user.getObjectId());
                                p_terra.put("plantNick", my_nome);
                                p_terra.put("scheduleTime", schedule_time);
                                p_terra.put("scheduleText", "Ciao, sono " + my_nome + ". Gradirei del fertilizzante!");
                                ParseCloud.callFunctionInBackground("schedule", p_terra, new FunctionCallback<Object>() {
                                    @Override
                                    public void done(Object object, ParseException e) {
                                        if (e == null) {
                                            Log.d("Ciao sono il cloud:", object.toString());
                                        }
                                    }
                                });
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        Intent i_goto_giardino = new Intent(NuovaPianta.this, Giardino.class);
                        startActivity(i_goto_giardino);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i_goto_giardino = new Intent(NuovaPianta.this, Giardino.class);
                        startActivity(i_goto_giardino);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                /*
                if (is_notifiche_ok) {
                    //Set the initial notify
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    ParseUser current_user = ParseUser.getCurrentUser();
                    Calendar c = Calendar.getInstance();
                    // c.add(Calendar.DATE,30);
                    Date schedule_time = c.getTime();
                    params.put("userID", current_user.getObjectId());
                    params.put("plantNick", my_nome);
                    params.put("scheduleTime", schedule_time);
                    params.put("scheduleText", "Ciao, sono " + my_nome + " e ti avvertirò ogni volta che avrò bisogno delle tue attenzioni. Trattami bene!");
                    ParseCloud.callFunctionInBackground("schedule", params, new FunctionCallback<Object>() {
                        @Override
                        public void done(Object object, ParseException e) {
                            if (e == null) {
                                Log.d("Ciao sono il cloud:", object.toString());
                            }
                        }
                    });
                }

                // Go to giardino
                startActivity(i_goto_giardino);
                */
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

                img_foto_pianta.setImageBitmap(imageBitmap);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Errore: impossibile salvare la foto", Toast.LENGTH_LONG).show();
            }
        }

        // Capture file result
        if (requestCode == FILE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Uri imgUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imgUri);
                    img_foto_pianta.setImageBitmap(imageBitmap);

                    // Image retrieved
                    Toast.makeText(this, "Foto caricata!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else return;
        }

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
