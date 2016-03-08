package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.zerostar.forgetmenotnext.R;
import com.zerostar.forgetmenotnext.utils.Util;

public class Giardino extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private ListView lista_my_piante;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giardino);
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

        lista_my_piante = (ListView) findViewById(R.id.lista_my_piante);

        //set the query
        setQueryAdapter();

        //setup the listeners

        lista_my_piante.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                util = new Util(Giardino.this);
                if(!util.isConnected()){
                    Toast.makeText(
                            getApplicationContext(),
                            "Connessione a internet assente",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                ParseObject entry = (ParseObject) parent.getItemAtPosition(position);
                Intent i_visualizza_my_pianta = new Intent(Giardino.this,VisualizzaMyPianta.class);
                String id_my_pianta = entry.getObjectId();
                i_visualizza_my_pianta.putExtra("id",id_my_pianta);
                startActivity(i_visualizza_my_pianta);
            }
        });
    }


    private void setQueryAdapter(){

        mainAdapter = new ParseQueryAdapter<ParseObject>(Giardino.this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                // Here we can configure a ParseQuery to our heart's desire.
                ParseQuery query = new ParseQuery("MyPianta");
                // CHECK IF THE CURRENT USER HAS PLANTS
                query.whereEqualTo("createdBy", ParseUser.getCurrentUser());
                query.whereStartsWith("Nickname", "");
                return query;
            }
        });
        mainAdapter.setTextKey("Nickname");
        mainAdapter.setImageKey("Avatar");
        lista_my_piante.setAdapter(mainAdapter);
        return;
    }
}
