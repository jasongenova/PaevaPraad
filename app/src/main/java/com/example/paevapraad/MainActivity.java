package com.example.paevapraad;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Elements titleElements;
    private Elements menuContainers;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private String placeName;
    private List<MenuItem> menuItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.image);

        Glide.with(this).load("https://www.paevapraad.ee/resources/img/top_image.jpg").into(imageView);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        Button button = (Button) findViewById(R.id.download_btn);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                new DownloadWebPage().execute("https://www.paevapraad.ee/kuressaare/");
            }
        });
    }

    private void setResult() {
        TextView result = (TextView) findViewById(R.id.result_text);

        result.setText(placeName);

        adapter = new MenuItemAdapter(menuItemList);

        recyclerView.setAdapter(adapter);
    }

    private class DownloadWebPage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];

            try {
                menuItemList = new ArrayList<MenuItem>();

                if (titleElements == null) {
                    Document doc = Jsoup.connect(url).get();

                    titleElements = doc.select("div.left_column > a > div > div:eq(2)");
                    menuContainers = doc.select("div.left_column > a > div > div:eq(1)");
                }

                Log.d("TITLE ELEMENTS SIZE", titleElements.size() + "");
                Log.d("MENU ELEMENTS SIZE", menuContainers.size() + "");

                Random rand = new Random();

                int placesCount = titleElements.size();

                int randomPlace = rand.nextInt(placesCount);

                placeName = titleElements.get(randomPlace).text();

                Element menuContainer = menuContainers.get(randomPlace);

                Elements menuItems = menuContainer.select(":root > div:gt(1)");

                Log.d("TOTAL MENU ITEMS", menuItems.size() + "");
                for (int i = 0; i < menuItems.size(); i++) {
                    Element menuItem = menuItems.get(i);

                    String food = menuItem.select("div").first().text().replace("€", "").trim();

                    Uri uri = Uri.parse(menuItem.select("div:eq(1) > img").attr("src"));
                    String price = uri.getQueryParameter("msg");

                    if (price == null) {
                        price = "0";
                    }

                    price = price.replace(',', '.');

                    menuItemList.add(new MenuItem(food, Double.parseDouble(price)));

                    Log.d("FOOD", food);
                    Log.d("PRICE", price);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            setResult();
        }
    }
}
