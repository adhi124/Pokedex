package com.example.datarsd1.pokedex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView searchresults;
    private Menu optionsMenu;
    private ArrayList<Pokedex.Pokemon> pokemonList;
    private PokeAdapter pokeadapter;
    private boolean linear;
    private String filterType1, filterType2;
    private Double currminhp, currminatk, currmindef;
    FloatingActionButton filters;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        filterType1 = "None";
        filterType2 = "None";
        currminhp = 0.0;
        currminatk = 0.0;
        currmindef = 0.0;

        searchresults = (RecyclerView) findViewById(R.id.searchresultsview);
        searchresults.setLayoutManager(new LinearLayoutManager(this));
        filters = (FloatingActionButton) findViewById(R.id.filter);
        filters.setOnClickListener(this);
        context = this;

        Pokedex dex = new Pokedex();
        pokemonList = dex.getPokemon();

        pokeadapter = new PokeAdapter(getApplicationContext(), pokemonList);
        searchresults.setAdapter(pokeadapter);

        linear = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        optionsMenu = menu;

        SearchView pokemonNameSearch = (SearchView) menu.findItem(R.id.name_search).getActionView();

        pokemonNameSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Pokedex.Pokemon> searchResultList = new ArrayList<Pokedex.Pokemon>();
                for (int i = 0 ; i < pokemonList.size(); i++)
                {
                    if (pokemonList.get(i).name.toLowerCase().startsWith(newText.toLowerCase()) || pokemonList.get(i).number.startsWith(newText))
                        searchResultList.add(pokemonList.get(i));
                }
                pokeadapter.updateList(searchResultList);
                pokeadapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.randomshuffle:
                ArrayList<Pokedex.Pokemon> randomResultsList = new ArrayList<Pokedex.Pokemon>();
                for (int i = 0 ; randomResultsList.size() < 20 ; i++)
                    randomResultsList.add(i,pokemonList.get((int)(Math.random()*pokemonList.size())));
                pokeadapter.updateList(randomResultsList);
                pokeadapter.notifyDataSetChanged();
                break;
            case R.id.layouttoggle:
                if (linear) {
                    searchresults.setLayoutManager(new GridLayoutManager(this, 2));
                    linear = false;
                }
                else {
                    searchresults.setLayoutManager(new LinearLayoutManager(this));
                    linear = true;
                }
                break;
        }
        return true;
    }

    public void filter(double minHP, double minAtk, double minDef, String type1, String type2)
    {
        ArrayList<Pokedex.Pokemon> typeFilterList = new ArrayList<Pokedex.Pokemon>();
        for (int i = 0 ; i < pokemonList.size(); i++)
        {
            if (Double.parseDouble(pokemonList.get(i).hp) > minHP && Double.parseDouble(pokemonList.get(i).attack) > minAtk && Double.parseDouble(pokemonList.get(i).defense) > minDef)
            {
                if (type1.equalsIgnoreCase("None") && type2.equalsIgnoreCase("None"))
                    typeFilterList.add(pokemonList.get(i));
                if (type2.equalsIgnoreCase("None") && !type1.equalsIgnoreCase("None"))
                    if (type1.equalsIgnoreCase(pokemonList.get(i).type))
                        typeFilterList.add(pokemonList.get(i));
                if (type1.equalsIgnoreCase("None") && !type2.equalsIgnoreCase("None"))
                    if (type2.equalsIgnoreCase(pokemonList.get(i).type))
                        typeFilterList.add(pokemonList.get(i));
                if (type1.equalsIgnoreCase(pokemonList.get(i).type) && type2.equalsIgnoreCase(pokemonList.get(i).type2))
                    typeFilterList.add(pokemonList.get(i));
            }
        }
        pokeadapter.updateList(typeFilterList);
        pokeadapter.notifyDataSetChanged();
        if (typeFilterList.size() == 0)
            Toast.makeText(getApplicationContext(), "No results found!", Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.filter:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.filter_input, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setView(promptsView);

                final EditText hpinput = (EditText) promptsView.findViewById(R.id.popuphp);
                final EditText atkinput = (EditText) promptsView.findViewById(R.id.popupatk);
                final EditText definput = (EditText) promptsView.findViewById(R.id.popupdef);

                final Spinner type1Spinner = (Spinner) promptsView.findViewById(R.id.type1spinner);
                final Spinner type2Spinner = (Spinner) promptsView.findViewById(R.id.type2spinner);

                hpinput.setText(currminhp.toString());
                atkinput.setText(currminatk.toString());
                definput.setText(currmindef.toString());

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.pokemontypes, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                type1Spinner.setAdapter(adapter);
                type2Spinner.setAdapter(adapter);
                type1Spinner.setSelection(adapter.getPosition(filterType1));
                type2Spinner.setSelection(adapter.getPosition(filterType2));

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Filter",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if filtering by numerical attributes, not filtering by type
                                        filterType1 = type1Spinner.getSelectedItem().toString();
                                        filterType2 = type2Spinner.getSelectedItem().toString();

                                        currminhp = Double.parseDouble(hpinput.getText().toString());
                                        currminatk = Double.parseDouble(atkinput.getText().toString());
                                        currmindef = Double.parseDouble(definput.getText().toString());

                                        filter(currminhp, currminatk, Double.parseDouble(definput.getText().toString()), filterType1, filterType2);
                                    }})
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }});

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }

}
