package com.example.newsapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAadpter.CategoryClickInterface {

    //180e5b6564ed474a9f414368e826594f

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAadpter categoryRVAadpter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        categoryRVAadpter = new CategoryRVAadpter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAadpter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://media.istockphoto.com/id/172172227/photo/grungy-newsprint.jpg?b=1&s=170667a&w=0&k=20&c=eKMWpzWvZFlq6ml3oLn26BV6usWxvehCXjX5JClkIkA="));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://media.istockphoto.com/id/885690024/photo/touch-screen-analyser.jpg?b=1&s=170667a&w=0&k=20&c=R8uYkeO_09QQszZ1HhzBtYJvg1ey7NzoRtxIisZUdNQ="));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://media.istockphoto.com/id/165088078/photo/focus-on-the-sports.jpg?b=1&s=170667a&w=0&k=20&c=meHo0wLUq6qczkXg-6sdI9ta0QVLQ7KvYfWN6RdTvMw="));
        categoryRVModalArrayList.add(new CategoryRVModal("General","https://media.istockphoto.com/id/1146380490/photo/digital-kiosk.jpg?b=1&s=170667a&w=0&k=20&c=rFsAssKSefMbC4U11zPNYtLQwuDG_6e8J2D-tUTAPWY="));
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://media.istockphoto.com/id/1290904409/photo/abstract-digital-news-concept.jpg?b=1&s=170667a&w=0&k=20&c=WMVA8KKLRL3KbUtPCmw5RQf2frl995MtI1qEmsqKdII="));
        categoryRVModalArrayList.add(new CategoryRVModal("Health","https://media.istockphoto.com/id/1302842394/photo/stack-of-report-medical-documents-and-stethoscope-isolated-on-white-background.jpg?b=1&s=170667a&w=0&k=20&c=ym37p0sRtIHXIB5Jq-N8_2ahiM0ThTMRNmybzK9IYzc="));
        categoryRVAadpter.notifyDataSetChanged();


    }
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+ category +"&apikey=180e5b6564ed474a9f414368e826594f";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=180e5b6564ed474a9f414368e826594f";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else{
            call=retrofitAPI.getNewsByCategory(categoryURL);

        }
        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();
                for(int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),
                           articles.get(i).getUrl(),articles.get(i).getContent()));

                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get news", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCategoryClick(int position) {
      String category = categoryRVModalArrayList.get(position).getCategory();
      getNews(category);
    }
}