package com.example.datarsd1.pokedex;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    private Pokedex.Pokemon currentPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView nameView, speciesView, atkv, defv, hpv;
        ImageView profilePic;

        nameView = (TextView) findViewById(R.id.profilename); speciesView = (TextView) findViewById(R.id.profilespecies);
        profilePic = (ImageView) findViewById(R.id.profileimage);

        nameView.setText("#"+getIntent().getStringExtra("nu")+": "+getIntent().getStringExtra("na"));
        speciesView.setText("the "+getIntent().getStringExtra("sp"));

        atkv = (TextView) findViewById(R.id.profileatk);atkv.setText("Attack: "+getIntent().getStringExtra("atk"));
        defv = (TextView) findViewById(R.id.profiledef);defv.setText("Defense: "+getIntent().getStringExtra("def"));
        hpv = (TextView) findViewById(R.id.profilehp);hpv.setText("HitPoints: "+getIntent().getStringExtra("hp"));

        Glide.with(this)
                .load("http://img.pokemondb.net/artwork/" + getIntent().getStringExtra("na").replaceAll(" ", "").toLowerCase() + ".jpg")
                .into(profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
                search.putExtra(SearchManager.QUERY, (getIntent().getStringExtra("na")));
                startActivity(search);
            }
        });
    }
}
