package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.zerostar.forgetmenotnext.R;


public class VisualizzaMyPianta extends Activity {

    private static final int ELIMINA_ID = 1;
    private static final int ESPOSIZIONE_ID = 2;

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ParseObject plant_pointer = null;
    private String my_id = "";
    private ListView lista_my_show;
    private ParseImageView img_avatar;
    private TextView l_nick;
    private TextView l_specie;
    private TextClock clock;
    private ImageButton btn_sole;
    private TextView l_sole;
    private String esposizione = "";
    private String acqua = "";
    private String terra = "";

    private ImageButton btn_goccia;

    private TextView l_goccia;
    private TextView l_scroll_goccia;
    private ScrollView scroll_goccia;
    private TextView txt_info_goccia;

    private ImageButton btn_terra;

    private TextView l_terra;
    private TextView l_info_terra;
    private TextView l_cosa_usare;
    private TextView l_info_my_pianta;
    private ImageButton btn_elimina;

    private Boolean is_luce_ok = false;
    private Boolean is_acqua_ok = false;
    private Boolean is_terra_ok = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_my_pianta);

        lista_my_show = (ListView) findViewById(R.id.lista_my_show);
        my_id = getIntent().getExtras().getString("id");
        ParseQuery query = new ParseQuery("MyPianta");
        query.whereEqualTo("objectId", my_id);
        try {
            ParseObject check = query.getFirst();
            is_luce_ok = check.getBoolean("IsLuceOk");
            is_acqua_ok = check.getBoolean("IsAcquaOk");
            is_terra_ok = check.getBoolean("IsTerraOk");

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter = new MyAdapter(this, new ParseQueryAdapter.QueryFactory<ParseObject>(){
            public ParseQuery<ParseObject> create() {
                // Here we can configure a ParseQuery to our heart's desire.
                ParseQuery query = new ParseQuery("MyPianta");
                query.whereEqualTo("objectId", my_id);
                return query;
            }
        });
        lista_my_show.setAdapter(mainAdapter);
    }

    class MyAdapter extends ParseQueryAdapter<ParseObject> {
        private Activity activity;

        public MyAdapter(Activity act, com.parse.ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
            super(act, queryFactory);
            this.activity = act;
            // setPaginationEnabled(false);
            //setObjectsPerPage(2);
            setAutoload(true);
        }

        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {

            if (v == null) {
                v = View.inflate(getContext(), R.layout.adapter_show_my_info, null);
            }

            //POPOLIAMO LA VIEW....
            img_avatar = (ParseImageView) v.findViewById(R.id.img_avatar);
            l_nick = (TextView) v.findViewById(R.id.l_nick_pianta);
            l_specie = (TextView) v.findViewById(R.id.l_tipo_my_pianta);
            clock = (TextClock) v.findViewById(R.id.clock);

            btn_sole = (ImageButton) v.findViewById(R.id.btn_sole);

            l_sole = (TextView) v.findViewById(R.id.l_sole);

            btn_goccia = (ImageButton) v.findViewById(R.id.btn_goccia);

            l_goccia = (TextView) v.findViewById(R.id.l_goccia);
            l_scroll_goccia = (TextView) v.findViewById(R.id.l_scroll_goccia);
            scroll_goccia = (ScrollView) v.findViewById(R.id.scroll_goccia);
            txt_info_goccia = (TextView) v.findViewById(R.id.txt_info_goccia);

            btn_terra = (ImageButton) v.findViewById(R.id.btn_terra);
            l_terra = (TextView) v.findViewById(R.id.l_terra);
            l_info_terra = (TextView) v.findViewById(R.id.l_info_terra);
            l_cosa_usare = (TextView) v.findViewById(R.id.l_cosa_usare);

            l_info_my_pianta= (TextView) v.findViewById(R.id.l_my_descrizione);

            btn_elimina = (ImageButton) v.findViewById(R.id.btn_cancella);


            super.getItemView(object, v, parent);


            //SET THE VIEW CONTENT...
            img_avatar.setParseFile(object.getParseFile("Avatar"));
            img_avatar.loadInBackground();

            l_nick.setText(object.getString("Nickname"));


            try {
                plant_pointer = object.getParseObject("Info").fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String specie = plant_pointer.getString("Nome");
            l_specie.setText(specie);

            //Controlla luce
            if (is_luce_ok){
                esposizione = "L'esposizione scelta è perfetta";
            }
            else{
                esposizione = "Controllare l'esposizione!";
            }
            l_sole.setText(esposizione);

            //Controlla acqua
            if (is_acqua_ok){
                acqua = "Non c'è bisogno di innaffiare";
            }
            else{
                acqua = "Bisogna innaffiare!";
            }
            l_goccia.setText(acqua);


            l_scroll_goccia.setText("Quanta acqua usare?");
            String info_acqua = plant_pointer.getString("QuantaAcqua");
            txt_info_goccia.setText(info_acqua);

            //Controlla fertilizzante
            if (is_terra_ok){
                terra = "Non c'è bisogno di fertilizzare";
            }
            else{
                terra = "E' consigliato fertilizzare!";
            }
            l_terra.setText(terra);

            l_info_terra.setText("Cosa usare?");
            String info_terra = plant_pointer.getString("Tipofertil");
            l_cosa_usare.setText(info_terra);

            String descrizione = plant_pointer.getString("Descrizione");
            l_info_my_pianta.setText(descrizione);

            //Settiamo i lister ai bottoni

            btn_sole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i_controlla_luce = new Intent(VisualizzaMyPianta.this,MenuSensore.class);
                    i_controlla_luce.putExtra("Esposizione",plant_pointer.getInt("Luce"));
                    startActivityForResult(i_controlla_luce, ESPOSIZIONE_ID);
                }
            });

            btn_goccia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!is_acqua_ok){

                    }
                    else{
                        Toast.makeText(VisualizzaMyPianta.this,"E' presto per innaffiare!",Toast.LENGTH_LONG).show();
                    }

                }
            });

            btn_terra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!is_terra_ok){

                    }
                    else {
                        Toast.makeText(VisualizzaMyPianta.this,"Non serve fertilizzare!",Toast.LENGTH_LONG).show();
                    }
                }
            });

            btn_elimina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i_elimina_pianta = new Intent(VisualizzaMyPianta.this,ShowElimina.class);
                    startActivityForResult(i_elimina_pianta,ELIMINA_ID);
                }
            });
            return v;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ParseQuery<ParseObject> query = new ParseQuery("MyPianta");
        query.whereEqualTo("objectId",my_id);

        if (requestCode == ELIMINA_ID) {
            if(resultCode == Activity.RESULT_OK){

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.deleteInBackground();
                        Toast.makeText(getApplicationContext(), "Pianta eliminata", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                return;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                return;
            }
        }

        if (requestCode == ESPOSIZIONE_ID){
            if(resultCode == Activity.RESULT_OK){
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.put("IsLuceOk",true);
                        is_luce_ok= true;
                        object.saveInBackground();


                    }
                });
                return;
            }
            else{
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.put("IsLuceOk", false);
                        is_luce_ok = false;
                        object.saveInBackground();

                    }
                });
            }
        }

    }
}
