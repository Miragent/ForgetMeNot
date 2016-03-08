package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.zerostar.forgetmenotnext.R;
import com.zerostar.forgetmenotnext.utils.Util;

public class VisualizzaPianta extends Activity {


    private ParseQueryAdapter<ParseObject> mainAdapter;

    private String id = "";
    private ParseImageView img_immagine;
    private TextView l_nome_pianta;
    private TextView l_tipo_pianta;
    private TextView l_descrizione;

    private ImageButton btn_aggiungi;

    private ListView lista_show;

    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_pianta);

        lista_show = (ListView) findViewById(R.id.lista_show);



        id = getIntent().getExtras().getString("id");
        Log.d("Loggerino:", id);

        mainAdapter = new MyAdapter(this, new ParseQueryAdapter.QueryFactory<ParseObject>(){
            public ParseQuery<ParseObject> create() {
                // Here we can configure a ParseQuery to our heart's desire.
                ParseQuery query = new ParseQuery("Plants");
                query.whereEqualTo("objectId", id);
                return query;
            }
        });
        lista_show.setAdapter(mainAdapter);


        btn_aggiungi = (ImageButton) findViewById(R.id.btn_aggiungi);
        btn_aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                util = new Util(VisualizzaPianta.this);
                if(!util.isConnected()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Connessione a internet assente",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i_aggiungi_pianta = new Intent(VisualizzaPianta.this,NuovaPianta.class);
                i_aggiungi_pianta.putExtra("idPianta",id);
                startActivity(i_aggiungi_pianta);
            }
        });
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
            v = View.inflate(getContext(), R.layout.adapter_show_info, null);
        }




        l_nome_pianta = (TextView) v.findViewById(R.id.l_nick_pianta);
        l_tipo_pianta = (TextView) v.findViewById(R.id.l_tipo_pianta);
        l_descrizione = (TextView) v.findViewById(R.id.l_descrizione);

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
        super.getItemView(object, v, parent);

        img_immagine = (ParseImageView) v.findViewById(R.id.img_avatar);
        img_immagine.setParseFile(object.getParseFile("Immagine"));
        img_immagine.loadInBackground();

        l_nome_pianta.setText(object.getString("Nome"));
        l_tipo_pianta.setText(object.getString("Tipo"));
        l_descrizione.setText(object.getString("Descrizione"));

        return v;
    }
}
}