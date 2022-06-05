package com.aspark.allbooks;

import static android.content.ContentValues.TAG;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.aspark.allbooks.BookshelfFrag.SHELF_REQ_CODE;
import static com.aspark.allbooks.SearchFrag.SEARCH_REQ_CODE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

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

public class NetworkRequest {
    String input;
    DataModel booksData;
    RecyclerView  recyclerView;
    List<DataModel> booksDataList;
    final static String BASE_URI = " https://www.googleapis.com/books/v1";
    final static String API_KEY = "AIzaSyAuSale2ufh6vE-gozkwcT-xsAD7cJyNCg";
    String url;
    Context context;
    RequestQueue requestQueue;
    SearchFrag searchFragObj;
    public static String ACCESS_TOKEN ;
     String REFRESH_TOKEN ;
    public  List<DataModel> shelfList;


    public NetworkRequest() {
    }

    public NetworkRequest(String input, Context context, RecyclerView recyclerView) {
        this.input = input;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        searchFragObj = new SearchFrag();
        this.recyclerView =recyclerView;

    }
    public NetworkRequest(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

    }

    public List<DataModel> search(int REQ_CODE) {

        // checking if this method is called by SearchFrag or BookshelfFrag
        if (REQ_CODE ==SEARCH_REQ_CODE) {

            // called by SearchFrag
            url = BASE_URI + "/volumes?q=" + input+"&maxResults=40&key="+API_KEY;
            Log.i("", "networkSearchRequest: "+url);

        }else if (REQ_CODE == SHELF_REQ_CODE){

            // TODO this will not work as no header is passed , and will have to fetch access token
            // called by BookshelfFrag
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

                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        booksDataList.add(storeData(volumeInfo));

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

    private DataModel storeData( JSONObject volumeInfo) throws JSONException {

        DataModel  booksData = new DataModel();

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

        }

        if (volumeInfo.has("description"))
            booksData.setDescription(volumeInfo.getString("description"));
        else
            booksData.setDescription("No Description");

        if (volumeInfo.has("subtitle"))
            booksData.setSubTitle(volumeInfo.getString("subtitle"));
        else
            booksData.setSubTitle(null);

        if (volumeInfo.has("publisher"))
            booksData.setPublisher(volumeInfo.getString("publisher"));
        else
            booksData.setPublisher("Unknown");

        if (volumeInfo.has("publishedDate"))
            booksData.setPublishedDate(volumeInfo.getString("publishedDate"));
        else
            booksData.setPublishedDate("Unknown");

        if (volumeInfo.has("pageCount"))
            booksData.setNoOfPages(volumeInfo.getString("pageCount"));
        else
            booksData.setNoOfPages("Unknown");

        if (volumeInfo.has("language"))
            booksData.setLanguage(volumeInfo.getString("language"));
        else
            booksData.setLanguage("Unknown");

        if (volumeInfo.has("averageRating"))
            booksData.setRating(volumeInfo.getString("averageRating"));
        else
            booksData.setRating("0");

        if (volumeInfo.has("ratingsCount"))
            booksData.setRatingsCount(volumeInfo.getString("ratingsCount"));
        else
            booksData.setRatingsCount("0");


        List<String> catList = new ArrayList<>();

        if (volumeInfo.has("categories")) {

            JSONArray categories= volumeInfo.getJSONArray("categories");

            for (int i= 0 ; i< categories.length() ; ++i)
                catList.add(categories.getString(i));
        }
        booksData.setCategories(catList);



        return booksData;


    }

    public void getAccessToken(String authCode) {



        String tokenUrl ="https://oauth2.googleapis.com/token?" +
                "code="+authCode +"&" +
                "client_id="+ "906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com"+"&" +
                "client_secret="+ "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" +"&" +
                "redirect_uri=http://localhost:5000&" +
                "grant_type=authorization_code";


        JsonObjectRequest tokenObject = new JsonObjectRequest(POST, tokenUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                try {
                    Log.i(TAG, "onResponse: TOKEN "+response.toString());
                    ACCESS_TOKEN = response.getString("access_token");

                    if (response.has("refresh_token")) {

                        REFRESH_TOKEN = response.getString("refresh_token");
                        editor.putString("refresh_token",REFRESH_TOKEN);
                        Log.i(TAG, "onResponse: REFRESH_TOKEN "+REFRESH_TOKEN);
                    }
                        else {
                        Log.i(TAG, "onResponse: NO REFRESH TOKEN FOUND");
                        editor.putString("refresh_token", null);
                    }

                        editor.putString("access_token",ACCESS_TOKEN);
                     editor.apply();
//                    getAccountData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "onErrorResponse: couldn't get token "+ error.getLocalizedMessage());

            }
        });

        requestQueue.add(tokenObject);
    }

    public void getAccountData() {

        String url = "https://www.googleapis.com/books/v1/mylibrary/bookshelves/0/volumes";

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: AccountData "+response.toString());

                JSONArray jsonArray ;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");
                    shelfList = new ArrayList<>();

                    for(int i=0; i < jsonArray.length();++i) {

                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        shelfList.add(storeData(volumeInfo));
                        recyclerView.setAdapter(new ShelfAdapter(context,shelfList));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();}
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                int errorCode = error.networkResponse.statusCode;
                Log.i(TAG, " getAccountData ERROR CODE "+errorCode);

                if (errorCode == 401) {

                    refreshToken();
                }

            }
        }){

            //add header to send access token along
            @Override
            public Map<String, String> getHeaders() {
                SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

                HashMap<String, String> header = new HashMap<>();

                //TODO if there is something wrong with access token after refreshed it will loop nonstop from getRefresh to getAccount.

                if (ACCESS_TOKEN==null)
                    ACCESS_TOKEN = preferences.getString("access_token","");

                header.put("Authorization","Bearer "+ACCESS_TOKEN);

                    Log.i(TAG, "onResponse: ACCESS_TOKEN "+ACCESS_TOKEN);
                    return header;
            }

        } ;

        requestQueue.add(objectRequest);


    }

    public void refreshToken(){

        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);

        String refreshUrl = "https://oauth2.googleapis.com/token?" +
                "client_id="+ "906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com" +
                "&client_secret="+"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" +
                "&refresh_token=" +preferences.getString("refresh_token","") +
                "&grant_type=refresh_token" ;

            JsonObjectRequest objectRequest = new JsonObjectRequest(POST, refreshUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        ACCESS_TOKEN = response.getString("access_token");
                        preferences.edit().putString("access_token",ACCESS_TOKEN).apply();
                        getAccountData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "onErrorResponse: can't  refresh access token "+error.getMessage());

                }
            });

            requestQueue.add(objectRequest);

    }


    public void fromAuthor(String authorName) {

        List<DataModel> fromAuthorList = new ArrayList<>();

        String url = "https://www.googleapis.com/books/v1/volumes?q=+inauthor:"+authorName;

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray ;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");

                    for(int i=0; i < jsonArray.length();++i) {

                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        fromAuthorList.add(storeData(volumeInfo));
                        recyclerView.setAdapter(new ShelfAdapter(context,fromAuthorList));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();}


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "from Author Error "+error.getMessage());

            }
        });


        requestQueue.add(objectRequest);

    }
}



