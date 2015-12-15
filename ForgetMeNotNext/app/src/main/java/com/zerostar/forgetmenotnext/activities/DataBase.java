package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.zerostar.forgetmenotnext.R;
import com.zerostar.forgetmenotnext.fragments.ShowFiltro;


public class DataBase extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView lista_piante;
    private EditText txt_nome_pianta;
    private ImageButton btn_cercaDB;
    private String nome_pianta;
    private String filtro_tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
    }

    @Override
    public void onResume(){
        super.onResume();
        lista_piante = (ListView) findViewById(R.id.lista_piante);
        txt_nome_pianta = (EditText) findViewById(R.id.txt_cercaPianta);
        btn_cercaDB = (ImageButton) findViewById(R.id.btn_cercaDB);

        setQueryAdapter();

        btn_cercaDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQueryAdapter();
            }
        });
    }

    private void setQueryAdapter(){
        nome_pianta = txt_nome_pianta.getText().toString();
        mainAdapter = new ParseQueryAdapter<ParseObject>(DataBase.this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
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
        mainAdapter.setTextKey("Nome");
        mainAdapter.setImageKey("Immagine");
        lista_piante.setAdapter(mainAdapter);
        return;
    }

    public void setFiltro_tipo(String stringa){
        filtro_tipo = stringa;
    }
}
