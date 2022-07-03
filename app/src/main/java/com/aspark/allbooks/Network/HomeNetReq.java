package com.aspark.allbooks.Network;

import static com.android.volley.Request.Method.GET;
import static com.aspark.allbooks.Network.NetworkRequest.ACCESS_TOKEN;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aspark.allbooks.DataModel;
import com.aspark.allbooks.R;
import com.aspark.allbooks.Adapter.ShelfAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomeNetReq {

    //TODO try to use abstraction or inheritance by extending NetworkRequest class.


    Context context;
    String TAG = "HomeNetReq";
    RequestQueue requestQueue;

    public HomeNetReq(Context context) {

        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void searchFreeEbook() {
        String url = "https://www.googleapis.com/books/v1/volumes?q=&filter=free-ebooks&key=" + R.string.google_books_api_key;

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "onResponse: GOT the free ebooks");


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "onErrorResponse: " + error.getMessage());

            }
        });
    }

    public void searchGenre(String genre, RecyclerView recyclerView) {

        List<DataModel> genreList = new ArrayList<>();

        Random random  = new Random();
        int startIndex = random.nextInt(200);
        Log.d(TAG, "searchGenre: "+ genre+" startIndex= "+startIndex);

        //TODO fix the random startIndex for horror & thriller. Throws error when startIndex > 50
        if (genre.equals("horror thriller") && startIndex >= 50) {
            startIndex = random.nextInt(50);
            Log.i(TAG, "searchGenre: startIndex of horror=" +startIndex);
        }

        String url = "https://www.googleapis.com/books/v1/volumes?q=+subject:"+ genre+ "&startIndex="+ startIndex+"&maxResults=40"+"&key=" + "AIzaSyAuSale2ufh6vE-gozkwcT-xsAD7cJyNCg";

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "onResponse: GOT the genre");
                Log.i(TAG, response.toString());

                JSONArray jsonArray;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length() && genreList.size() <=15; ++i) {

                        DataModel dataModel = new DataModel();

                        if (jsonArray.getJSONObject(i).has("id")) {

                            String volumeId = jsonArray.getJSONObject(i).getString("id");
                            dataModel.setVolumeId(volumeId);
                        }

                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        if (volumeInfo.has("imageLinks"))
                            genreList.add(storeData(volumeInfo, dataModel));
                    }
                    recyclerView.setAdapter(new ShelfAdapter(context, genreList));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: COUld not load genre book list");
            }
        });
        requestQueue.add(objectRequest);

}

    private DataModel storeData( JSONObject volumeInfo,DataModel booksData) throws JSONException {

        //Convert http url to https
        String Url = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
        StringBuilder builder = new StringBuilder(Url);
        booksData.setCoverUrl(builder.insert(4,'s').toString());

        booksData.setTitle(volumeInfo.getString("title"));

        if (volumeInfo.has("authors")){
            booksData.setAuthor(volumeInfo.getJSONArray("authors").getString(0));
        }
        else {
            booksData.setAuthor("Unknown");
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


    public void getRecommendedBooks(RecyclerView recommended_RV){

        List<DataModel> recommendedList = new ArrayList<>();

        Random random = new Random();
        int startIndex = random.nextInt(60);
        Log.i(TAG, "getRecommendedBooks: startIndex= "+startIndex);

        String url ="https://www.googleapis.com/books/v1/mylibrary/bookshelves/8/volumes?"+"&startIndex="+ startIndex+"&maxResults=40"+"&key=AIzaSyAuSale2ufh6vE-gozkwcT-xsAD7cJyNCg";

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "onResponse: GOT the Recommended Books");
                Log.i(TAG, response.toString());

                JSONArray jsonArray;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length() && recommendedList.size() <=16; ++i) {

                        DataModel dataModel = new DataModel();

                        if (jsonArray.getJSONObject(i).has("id")) {

                            String volumeId = jsonArray.getJSONObject(i).getString("id");
                            dataModel.setVolumeId(volumeId);
                        }

                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        if (volumeInfo.has("imageLinks"))
                            recommendedList.add(storeData(volumeInfo, dataModel));

                    }
                    recommended_RV.setAdapter(new ShelfAdapter(context, recommendedList));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "onErrorResponse: Could not get recommended list "+error.getMessage());

            }
        }){

            @Override
            public Map<String, String> getHeaders() {

                SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
                HashMap<String ,String> header = new HashMap<>();

                if (ACCESS_TOKEN==null)
                    ACCESS_TOKEN = preferences.getString("access_token","");

                header.put("Authorization","Bearer "+ACCESS_TOKEN);
                Log.d(TAG, "Recommended books access token "+ACCESS_TOKEN);

                return header;

            }
        };
        requestQueue.add(objectRequest);
    }
}
