package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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

public class DataBase extends Activity {

    static final int FILTER_ID = 1;


    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView lista_piante;
    private EditText txt_nome_pianta;
    private ImageButton btn_cercaDB;
    private ImageButton btn_query_opt;
    private String nome_pianta;
    private String filtro_tipo = "";
    private Util util;

    private ParseImageView img_icon;
    private TextView l_nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
    }

    @Override
    public void onResume(){
        super.onResume();

        util = new Util(this);
        if(!util.isConnected()){
            Toast.makeText(
                    getApplicationContext(),
                    "Connessione a internet assente",
                    Toast.LENGTH_LONG).show();
        }

        lista_piante = (ListView) findViewById(R.id.lista_piante);
        txt_nome_pianta = (EditText) findViewById(R.id.txt_cercaPianta);
        btn_cercaDB = (ImageButton) findViewById(R.id.btn_cercaDB);
        btn_query_opt = (ImageButton)findViewById(R.id.btn_query_opt);

        setQueryAdapter();

        //setup the listeners

        lista_piante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                util = new Util(DataBase.this);
                if(!util.isConnected()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Connessione a internet assente",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                ParseObject entry = (ParseObject) parent.getItemAtPosition(position);
                String id_pianta = entry.getObjectId();
                Log.d("Loggerello:", id_pianta);
                Intent i_visualizza_pianta = new Intent(DataBase.this,VisualizzaPianta.class);
                i_visualizza_pianta.putExtra("id",id_pianta);
                startActivity(i_visualizza_pianta);
            }
        });

        btn_cercaDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQueryAdapter();
            }
        });
        btn_query_opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_show_filtri = new Intent(DataBase.this, ShowFiltro.class);
                startActivityForResult(i_show_filtri, FILTER_ID);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILTER_ID) {
            if(resultCode == Activity.RESULT_OK){
                filtro_tipo = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                filtro_tipo = "";
            }
        }

    }

    private void setQueryAdapter(){
        nome_pianta = txt_nome_pianta.getText().toString();
        mainAdapter = new MyAdapter(DataBase.this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {

            // Here we can configure a ParseQuery to our heart's desire.
                ParseQuery query = new ParseQuery("Plants");

                query.whereStartsWith("Nome", nome_pianta);
                if(!filtro_tipo.equals("")) {
                    query.whereEqualTo("Tipo", filtro_tipo);
                }
                return query;
            }
        });


        lista_piante.setAdapter(mainAdapter);
        return;
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
                v = View.inflate(getContext(), R.layout.adapter_show_piante, null);
            }

            img_icon = (ParseImageView) v.findViewById(R.id.img_icon1);
            l_nome = (TextView) v.findViewById((R.id.l_text1));

            super.getItemView(object, v, parent);

            img_icon.setParseFile(object.getParseFile("Immagine"));
            img_icon.loadInBackground();
            l_nome.setText(object.getString("Nome"));

            return v;
        }
    }
}
