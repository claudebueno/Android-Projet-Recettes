package com.m2i.projetrecettes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by claudebueno on 28/02/2017.
 */

public class DetailsActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        TextView singleName = (TextView) findViewById(R.id.single_name);
        TextView singleAction  = (TextView) findViewById(R.id.single_action);
        TextView singleIngredients = (TextView) findViewById(R.id.single_ingredients);



    }
}