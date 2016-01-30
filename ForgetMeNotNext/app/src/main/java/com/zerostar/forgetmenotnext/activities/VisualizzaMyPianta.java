package com.zerostar.forgetmenotnext.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.zerostar.forgetmenotnext.R;

import org.w3c.dom.Text;

public class VisualizzaMyPianta extends Activity {

    private ParseQueryAdapter<ParseObject> mainAdapter;
    private String my_id = "";
    private ListView lista_my_show;
    private ParseImageView img_avatar;
    private TextView l_nick;
    private TextView l_specie;
    private TextClock clock;
    private ImageButton btn_sole;
    private ProgressBar bar_sole;
    private TextView l_sole;
    private TextView l_scroll_sole;
    private ScrollView scroll_sole;
    private TextView txt_info_sole;
    private ImageButton btn_goccia;
    private ProgressBar bar_goccia;
    private TextView l_goccia;
    private TextView l_scroll_goccia;
    private ScrollView scroll_goccia;
    private TextView txt_info_goccia;
    private ImageButton btn_terra;
    private ProgressBar bar_terra;
    private TextView l_terra;
    private TextView l_info_terra;
    private TextView l_info_my_pianta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_my_pianta);

        lista_my_show = (ListView) findViewById(R.id.lista_my_show);



        my_id = getIntent().getExtras().getString("id");
        Log.d("Id My Pianta:", my_id);

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
            bar_sole = (ProgressBar) v.findViewById(R.id.bar_sole);
            l_sole = (TextView) v.findViewById(R.id.l_sole);

            /*
            l_scroll_sole = (TextView) v.findViewById(R.id.l_scroll_sole);
            scroll_sole = (ScrollView) v.findViewById(R.id.scroll_sole);
            txt_info_sole = (TextView) v.findViewById(R.id.txt_info_sole);
            */

            btn_goccia = (ImageButton) v.findViewById(R.id.btn_goccia);
            bar_goccia = (ProgressBar) v.findViewById(R.id.bar_goccia);
            l_goccia = (TextView) v.findViewById(R.id.l_goccia);
            l_scroll_goccia = (TextView) v.findViewById(R.id.l_scroll_goccia);
            scroll_goccia = (ScrollView) v.findViewById(R.id.scroll_goccia);
            txt_info_goccia = (TextView) v.findViewById(R.id.txt_info_goccia);

            btn_terra = (ImageButton) v.findViewById(R.id.btn_terra);
            bar_terra = (ProgressBar) v.findViewById(R.id.bar_terra);
            l_terra = (TextView) v.findViewById(R.id.l_terra);
            l_info_terra= (TextView) v.findViewById(R.id.l_info_terra);

            l_info_my_pianta= (TextView) v.findViewById(R.id.l_my_descrizione);


            // Take advantage of ParseQueryAdapter's getItemView logic for
            // populating the main TextView/ImageView.
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            // if it will be populating a TextView or ImageView for you.
            super.getItemView(object, v, parent);


            //SET THE VIEW CONTENT...
            img_avatar.setParseFile(object.getParseFile("Avatar"));
            img_avatar.loadInBackground();

            l_nick.setText(object.getString("Nickname"));


            ParseObject plant_pointer = null;
            try {
                plant_pointer = object.getParseObject("Info").fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String specie = plant_pointer.getString("Nome");
            l_specie.setText(specie);

            String meteo = "Oggi c'è il sole!";
            l_sole.setText(meteo);

            String acqua = "Perfetto!";
            l_goccia.setText(acqua);
            l_scroll_goccia.setText("Quanta acqua usare?");

            String info_acqua = plant_pointer.getString("QuantaAcqua");
            txt_info_goccia.setText(info_acqua);

            String terra = "Fertilizzare tra un mese";
            l_terra.setText(terra);
            l_info_terra.setText("Cosa usare?");

            String descrizione = plant_pointer.getString("Descrizione");
            l_info_my_pianta.setText(descrizione);

            return v;

        }
    }
}
