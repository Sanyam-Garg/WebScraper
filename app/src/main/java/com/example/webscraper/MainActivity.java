package com.example.webscraper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private ParseAdapter adapter;
    private ArrayList<ParseItem> parseItems = new ArrayList<>();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParseAdapter(parseItems, this);
        recyclerView.setAdapter(adapter);


        searchView = findViewById(R.id.searchView);
        Content content = new Content();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                content.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });





    }

    private class Content extends AsyncTask<Void, Void, Void>{
        String searchItem = searchView.getQuery().toString();

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            adapter.notifyDataSetChanged();
        }

        @Override
        protected  void onCancelled(){
            super.onCancelled();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "https://www.amazon.com/s?k="+ searchItem +"&ref=nb_sb_noss_2";
                Document doc = Jsoup.connect(url).get();

                Elements data = doc.select("span.rush-component");

                int size = data.size();
                for(int i = 0; i < size; i++) {
                    String imgUrl = data.select("img.s-image").select("img").eq(i).attr("src");

                    String title = data.select("h2.a-size-mini a-spacing-none a-color-base s-line-clamp-2").select("span.a-size-medium a-color-base a-text-normal").eq(i).text();
                    parseItems.add(new ParseItem(imgUrl, title));
                    Log.d("items", "img: " + imgUrl + " . title: " + title);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


    }
}