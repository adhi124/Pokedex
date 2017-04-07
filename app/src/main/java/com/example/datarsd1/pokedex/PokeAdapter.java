package com.example.datarsd1.pokedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.w3c.dom.Text;

/**
 * Created by datarsd1 on 2/16/17.
 */

public class PokeAdapter extends RecyclerView.Adapter<PokeAdapter.CustomViewHolder> {

    private Context context;
    ArrayList<Pokedex.Pokemon> pokemonList;

    public PokeAdapter(Context context, ArrayList<Pokedex.Pokemon> pokemonList) {
        this.context = context;
        this.pokemonList = pokemonList;
    }

    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_search_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Pokedex.Pokemon pokemon = pokemonList.get(position);

        holder.pokemonTextView.setText(pokemon.name);
        holder.pokemonNumberTextView.setText("#"+pokemon.number);

        String name = pokemon.name.toLowerCase();
        String url = "http://img.pokemondb.net/artwork/" + name.replaceAll("\'","").replaceAll("é","e") + ".jpg";

        Glide.with(context)
                .load(url)
                .into(holder.pokemonImageContainer);
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView pokemonTextView, pokemonNumberTextView;
        ImageView pokemonImageContainer;

        public CustomViewHolder(View view) {
            super(view);

            this.pokemonTextView = (TextView) view.findViewById(R.id.pokemonname);
            this.pokemonImageContainer = (ImageView) view.findViewById(R.id.pokemonimagewrapper);
            this.pokemonNumberTextView = (TextView) view.findViewById(R.id.pokemonnumber);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    Pokedex.Pokemon pkmn = pokemonList.get(getAdapterPosition());
                    intent.putExtra("na",pkmn.name);
                    intent.putExtra("nu",pkmn.number);
                    intent.putExtra("sp",pkmn.species);
                    intent.putExtra("atk",pkmn.attack);
                    intent.putExtra("def",pkmn.defense);
                    intent.putExtra("hp",pkmn.hp);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public void updateList(ArrayList<Pokedex.Pokemon> newList)
    {
        this.pokemonList = newList;
    }

}