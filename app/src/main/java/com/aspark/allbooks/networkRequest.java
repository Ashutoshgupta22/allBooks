package com.aspark.allbooks;

import static android.content.ContentValues.TAG;
import static com.aspark.allbooks.bookshelfFrag.SHELF_REQ_CODE;
import static com.aspark.allbooks.searchFrag.SEARCH_REQ_CODE;

import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class networkRequest {
    String input;
    BooksData booksData;
    RecyclerView  recyclerView;
    List<BooksData> booksDataList;
    final static String BASE_URI = " https://www.googleapis.com/books/v1";
    final static String API_KEY = "AIzaSyAuSale2ufh6vE-gozkwcT-xsAD7cJyNCg";
    String url;
    Context context;
    RequestQueue requestQueue;
    searchFrag searchFragObj;
    //   private String[] titleArray = new String[100];
//   private String[] authorArray = new String[100];
//   private String[] coverUrlArray = new String[100];

    public networkRequest() {
    }

    public networkRequest(String input, Context context,RecyclerView recyclerView) {
        this.input = input;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        searchFragObj = new searchFrag();
        this.recyclerView =recyclerView;

    }

    public List<BooksData> search(int REQ_CODE) {

        if (REQ_CODE ==SEARCH_REQ_CODE) {

            url = BASE_URI + "/volumes?q=" + input+"&maxResults=40&key="+API_KEY;
            Log.i("", "networkSearchRequest: "+url);

        }else if (REQ_CODE == SHELF_REQ_CODE){



         url = BASE_URI + "/mylibrary/bookshelves/0/volumes?key="+API_KEY;

         Log.i("", "networkShelfRequest: "+url);

        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray ;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");
                    booksDataList = new ArrayList<>();

                    for(int i=0; i < jsonArray.length();++i) {


                        booksData = new BooksData();
                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        booksData.setTitle(volumeInfo.getString("title"));
                        if (volumeInfo.has("authors")){
                            booksData.setAuthor(volumeInfo.getJSONArray("authors").getString(0));
                        }
                        else {
                            booksData.setAuthor("Unknown");
                        }
                        if (volumeInfo.has("imageLinks")){

                            //Convert http url to https
                            String Url = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                            StringBuilder builder = new StringBuilder(Url);
                            booksData.setCoverUrl(builder.insert(4,'s').toString());
                            booksDataList.add(booksData);
                        }

                        Log.i("TAG", "onResponse: title "+booksDataList.get(0).getTitle());
                        if (REQ_CODE ==SEARCH_REQ_CODE)
                        recyclerView.setAdapter(new SearchAdapter(context,booksDataList));
                        else if (REQ_CODE==SHELF_REQ_CODE)
                            recyclerView.setAdapter(new ShelfAdapter(context,booksDataList));

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("networkRequest", "onResponse:  " +response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.getStackTrace();

            }
        }) {

            //add header to send authorisation code
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> header = new HashMap<>();
                String authToken ="";
                header.put("Authorization",authToken);

                return header;
            }

        };

        requestQueue.add(objectRequest);

        return booksDataList;
    }




}

