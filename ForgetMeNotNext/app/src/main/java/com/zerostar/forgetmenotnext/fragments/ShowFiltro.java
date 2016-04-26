package com.zerostar.forgetmenotnext.fragments;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.zerostar.forgetmenotnext.R;

import java.util.ArrayList;

public class ShowFiltro extends DialogFragment{

    private String scelta;
   // SOLUZIONE CASARECCIA
    private ArrayList<String> lista_scelte = new ArrayList<String>();

    public ShowFiltro(){
        lista_scelte.add("Pianta Aromatica");
        lista_scelte.add("Pianta Carnivora");
        lista_scelte.add("Pianta Grassa");
        lista_scelte.add("Pianta Ornamentale");
        lista_scelte.add("Pokemom Erba/Veleno");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_show_filtro, container, false);
    }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Filtra per tipo")
                   .setSingleChoiceItems((ListAdapter) lista_scelte, -1, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           scelta = lista_scelte.get(which);
                       }
                   })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // DO STUFF
                        }
                    })
                    .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }



}
