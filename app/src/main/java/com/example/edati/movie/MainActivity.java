package com.example.edati.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private SingerAdapter adapter;
    TextView textView;
    EditText editText;
    ProgressBar progressBar;
    ProgressDialog dialog;
    private int startNumber;
    private final static int DISPLAY_NUM = 10;
    private int totalMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("태일이가 만든 영화찾기ㅎㅎㅎ");
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        List<Movie> items = new ArrayList<>();

        adapter = new SingerAdapter(this, items);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SingerAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(SingerAdapter.ViewHolder holder, View view, int position) {
                onClickMovieLink(position);
            }
        });

        editText = findViewById(R.id.editText1);
        Button button = (Button) findViewById(R.id.button);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        if(AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager mInputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                if(editText.getText().toString().length()==0){
                    Toast toast = Toast.makeText(getApplicationContext(),"검색을 해 ㅡㅡ",Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    adapter.clearItems();
                    showProgressDialog();
                    startNumber=1;
                    sendRequest();

                    recyclerView.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {

                    if( DISPLAY_NUM < adapter.items.size() ){
                        sendRequest();
                    }
                }
            }
        });
    }

    private void onClickMovieLink(int position) {
        try {
            Movie item = adapter.getItem(position);

            String url = item.getLink();
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();

            customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
        } catch (NullPointerException e) {
            Log.e(this.getClass().getName(), "영화 링크가 존재하지 않습니다.", e);

            Toast.makeText(this, "영화 링크가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "영화 링크 이동 중 알 수 없는 오류가 발생하였습니다.", e);

            Toast.makeText(this, "영화 링크 이동 중 알 수 없는 오류가 발생하였습니다. 다시 시도해 주시기 바랍니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("잠시만요..");
        dialog.show();
    }

    private void cancelProgressDialog() {
        dialog.dismiss();
    }

    public void sendRequest() {
        String url = "https://openapi.naver.com/v1/search/movie.json?query=" + editText.getText().toString() + "&display=" + DISPLAY_NUM + "&start=" + startNumber;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        processResponse(response);
                        cancelProgressDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 - >" + error.getMessage());
                        cancelProgressDialog();
                    }
                }
        ) {
            @Override
            public Map getHeaders() throws  AuthFailureError{
                Map params = new HashMap();
                params.put("X-Naver-Client-id","8Rqbuwrc5MMZQxMmyFie");
                params.put("X-Naver-Client-Secret","WDgFwct1hx");

                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }



    public void processResponse(String response){
        Gson gson = new Gson();
        MovieListResult movieList = gson.fromJson(response, MovieListResult.class);

        if(movieList != null && movieList.items.size() !=0){
            adapter.addItems(movieList.items);
            startNumber += movieList.items.size();
            totalMovie = movieList.getTotal();


        } else {
                Toast.makeText(this,  "\'"+editText.getText().toString()+"\'  검색결과는 없습니다..", Toast.LENGTH_LONG).show();
            totalMovie = 0;
        }
    }

    public void println(String data) {
        Log.i(getClass().getName().toString(), data + "\n");
    }
}
