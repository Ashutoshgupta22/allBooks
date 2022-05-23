package com.aspark.allbooks;

import static android.content.ContentValues.TAG;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
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
    public static String ACCESS_TOKEN ;
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
    public networkRequest(Context context){
        requestQueue = Volley.newRequestQueue(context);

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
        }) ;

        requestQueue.add(objectRequest);

        return booksDataList;
    }

    public String[] getAccessToken(String authCode) {

        String[] tokens = new String[2];

        String tokenUrl ="https://oauth2.googleapis.com/token?" +
                "code="+authCode +"&" +
                "client_id="+ "906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com"+"&" +
                "client_secret="+ "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" +"&" +
                "redirect_uri=http://localhost:5000&" +
                "grant_type=authorization_code";


        JsonObjectRequest tokenObject = new JsonObjectRequest(POST, tokenUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.i(TAG, "onResponse: TOKEN "+response.toString());
                    tokens[0] = response.getString("access_token");
                    ACCESS_TOKEN = tokens[0];

//                    tokens[1] = response.getString("refresh_token");

                    getAccountData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "onErrorResponse: couldnt get token "+ error.getLocalizedMessage());

            }
        });

        requestQueue.add(tokenObject);

        return tokens;


    }

    private void getAccountData() {

        String url = "https://www.googleapis.com/books/v1/mylibrary/bookshelves/0/volumes";

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: AccountData "+response.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "onErrorResponse: getAccountData "+error.getMessage());

            }
        }){

            //add header to send authorisation code
            @Override
            public Map<String, String> getHeaders() {

                HashMap<String, String> header = new HashMap<>();

                Log.i(TAG, "onResponse: ACCESS_TOKEN "+ACCESS_TOKEN);
                header.put("Authorization","Bearer "+ACCESS_TOKEN);

                return header;
            }

        } ;

        requestQueue.add(objectRequest);


    }


}

