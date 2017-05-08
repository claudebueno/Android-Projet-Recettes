package com.m2i.projetrecettes;

/**
 * Created by claudebueno on 28/02/2017
 */

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;

    // URL pour accéder au JSON
    // private static String url = "http://api.androidhive.info/contacts/";
    private static String url = "http://www.claudebueno.com/doc/test3.json";

    ArrayList<HashMap<String, String>> recetteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recetteList = new ArrayList<>();

        // On déclare la liste view associée au projet
        lv = (ListView) findViewById(R.id.list_view);

        new GetRecettes().execute();

        // TODO Personnalisation de l'ActionBar



        // TODO A finir
        // Depuis la ListView ouverture du détail de la recette
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

         // Alerte indiquant quelle position a été cliquée
         Toast.makeText(getApplicationContext(),
            "Click ListItem Number " + position + "parent: "+ parent + "view: "+ view + "id: "+ id, Toast.LENGTH_LONG)
            .show();
            }
        });


    }

    // Tache Asynchrone pour récupérer la liste des recettes
    private class GetRecettes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Veuillez patienter...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Interrogation de l'URL pour obtenir la réponse
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Réponse de l'URL : " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Obtenir les noeud du JSON
                    JSONArray recettes = jsonObj.getJSONArray("list");

                    // Boucle pour obtenir toutes les recettes
                    for (int i = 0; i < recettes.length(); i++) {
                        JSONObject c = recettes.getJSONObject(i);

                        String name = c.getString("nom");
                        String intro = c.getString("intro");
                        String actions = c.getString("actions");

                        // Faire un hmap des recettes
                        HashMap<String, String> recette = new HashMap<>();

                        // ajout de chaque recette en clé => valeur
                        recette.put("nom", name);
                        recette.put("intro", intro);
                        recette.put("actions", actions);

                        // ajout d'une recette à la liste des recettes
                        recetteList.add(recette);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Erreur de parsing du JSON : " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Erreur de parsing du JSON : " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();

                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, recetteList,
                    R.layout.list_item, new String[]{"nom", "intro"},
                    new int[]{R.id.name,R.id.intro});

            lv.setAdapter(adapter);
        }

    }
}
