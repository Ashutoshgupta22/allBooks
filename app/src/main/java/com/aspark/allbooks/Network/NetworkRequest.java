package com.aspark.allbooks.Network;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.aspark.allbooks.Adapter.RecentlyViewedAdapter;
import com.aspark.allbooks.DataModel;
import com.aspark.allbooks.Adapter.SearchAdapter;
import com.aspark.allbooks.Fragment.SearchFrag;
import com.aspark.allbooks.Adapter.ShelfAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NetworkRequest {

    private final String TAG = "NetworkRequest";

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

     public NetworkRequest() {
    }

    public NetworkRequest( Context context, RecyclerView recyclerView) {

        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        searchFragObj = new SearchFrag();
        this.recyclerView =recyclerView;

    }
    public NetworkRequest(Context context){
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);

    }

    public void search(String query) {


            url = BASE_URI + "/volumes?q=" + query+"&maxResults=40&key="+API_KEY;
            Log.i("", "networkSearchRequest: "+url);

            JsonObjectRequest objectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray ;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");
                    booksDataList = new ArrayList<>();


                    for(int i=0; i < jsonArray.length();++i) {

                        DataModel dataModel = new DataModel();

                        if (jsonArray.getJSONObject(i).has("id")) {

                            String volumeId = jsonArray.getJSONObject(i).getString("id");
                            dataModel.setVolumeId(volumeId);
                        }
                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        if (volumeInfo.has("imageLinks"))
                            booksDataList.add(storeData(volumeInfo, dataModel));
                    }

                        Log.i("TAG", "onResponse: title "+booksDataList.get(0).getTitle());

                        recyclerView.setAdapter(new SearchAdapter(context,booksDataList));

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


    }

    private DataModel storeData( JSONObject volumeInfo,DataModel booksData) throws JSONException {



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

//                Log.i(TAG, "onResponse: AccountData "+response.toString());
//
//                JSONArray jsonArray ;
//                JSONObject volumeInfo;
//                try {
//                    jsonArray = response.getJSONArray("items");
//                    shelfList = new ArrayList<>();
//
//
//                    for(int i=0; i < jsonArray.length();++i) {
//
//                        DataModel dataModel = new DataModel();
//                        if (jsonArray.getJSONObject(i).has("id")) {
//
//                            String volumeId = jsonArray.getJSONObject(i).getString("id");
//                            dataModel.setVolumeId(volumeId);
//                        }
//                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");
//
//                        shelfList.add(storeData(volumeInfo, dataModel));
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();}
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                int errorCode = error.networkResponse.statusCode;
                Log.i(TAG, " getAccountData ERROR CODE "+errorCode);

                if (errorCode == 401) {
                    Log.i(TAG, "refreshing access token");
                    refreshAccessToken();
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

    public void refreshAccessToken(){

        SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        String savedRefreshToken = preferences.getString("refresh_token","");
        Log.i(TAG, "SAVED refreshToken: " +savedRefreshToken);

        String refreshUrl = "https://oauth2.googleapis.com/token?" +
                "client_id="+ "906052742414-kd8vmeo07segpllhjjpgocqjlshbhs7t.apps.googleusercontent.com" +
                "&client_secret="+"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" +
                "&refresh_token=" + savedRefreshToken +
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

                    int errorCode = error.networkResponse.statusCode;
                    if (errorCode == 400) {

                        Log.i(TAG, "onErrorResponse: Refresh token expired");
                        //  getNewRefreshToken();
                    }

                }
            });

            requestQueue.add(objectRequest);

    }

    public void fromAuthor(String authorName) {

        List<DataModel> fromAuthorList = new ArrayList<>();

        Random random  = new Random();
        int startIndex = random.nextInt(100);

        String url = "https://www.googleapis.com/books/v1/volumes?q=+inauthor:"+authorName + "&startIndex="+ startIndex+"&maxResults=40"+"&key=" + "AIzaSyAuSale2ufh6vE-gozkwcT-xsAD7cJyNCg";

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray ;
                JSONObject volumeInfo;
                Log.i(TAG, "onResponse: from Author list "+response.toString());

                try {
                    jsonArray = response.getJSONArray("items");

                    for(int i=0; i < jsonArray.length();++i) {

                        DataModel dataModel = new DataModel();

                        if (jsonArray.getJSONObject(i).has("id")) {

                            String volumeId = jsonArray.getJSONObject(i).getString("id");
                            dataModel.setVolumeId(volumeId);
                        }
                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        if (volumeInfo.has("imageLinks"))
                            fromAuthorList.add(storeData(volumeInfo, dataModel));
                    }
                        recyclerView.setAdapter(new ShelfAdapter(context,fromAuthorList));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "from Author Error "+error.getMessage());
            }
        });

        requestQueue.add(objectRequest);
    }

    public void youMayLike(List<String> categories) {

        List<DataModel> youMayLikeList = new ArrayList<>();

        //TODO search and show all categories

        Random random  = new Random();
        int startIndex = random.nextInt(200);

        String cUrl = "https://www.googleapis.com/books/v1/volumes?q=+subject:" +categories.get(0) + "&startIndex="+ startIndex+"&maxResults=40"+"&key=" + "AIzaSyAuSale2ufh6vE-gozkwcT-xsAD7cJyNCg";

        if (categories.size() >= 3)
        Log.d(TAG, "youMayLike: Categories "+categories.get(0)+ categories.get(1)+categories.get(2));

        JsonObjectRequest objectRequest = new JsonObjectRequest(GET, cUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, "YOu May like list "+response.toString());
                JSONArray jsonArray ;
                JSONObject volumeInfo;
                try {
                    jsonArray = response.getJSONArray("items");
                    for(int i=0; i < jsonArray.length() && youMayLikeList.size() <=15;++i) {

                        DataModel dataModel = new DataModel();

                        if (jsonArray.getJSONObject(i).has("id")) {

                            String volumeId = jsonArray.getJSONObject(i).getString("id");
                            dataModel.setVolumeId(volumeId);
                        }

                        volumeInfo = jsonArray.getJSONObject(i).getJSONObject("volumeInfo");

                        if (volumeInfo.has("imageLinks"))
                            youMayLikeList.add(storeData(volumeInfo, dataModel));
                    }
                        recyclerView.setAdapter(new ShelfAdapter(context,youMayLikeList));

                } catch (JSONException e) {
                    e.printStackTrace();
                 }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Cant get you may like section "+error.getLocalizedMessage());
            }
        });
        requestQueue.add(objectRequest);
    }

    public void postRecentlyViewed(String volumeId) {

        String vUrl = "https://www.googleapis.com/books/v1/mylibrary/bookshelves/6/addVolume?volumeId="+ volumeId +"&key="+API_KEY;

        JsonObjectRequest objectRequest = new JsonObjectRequest(POST, vUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, "Recent View Posted ");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i(TAG, "Could not post Recent Viewed "+ error.getLocalizedMessage());

            }
        } ){
            @Override
            public Map<String, String> getHeaders() {

                SharedPreferences preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);

                HashMap<String, String> header = new HashMap<>();


                if (ACCESS_TOKEN==null)
                    ACCESS_TOKEN = preferences.getString("access_token","");

                header.put("Authorization","Bearer "+ACCESS_TOKEN);

                Log.i(TAG, "onResponse: ACCESS_TOKEN "+ACCESS_TOKEN);
                return header;
            }
        };

        requestQueue.add(objectRequest);


    }

    public void showRecentlyViewed(RecyclerView recentlyViewed_rv, ArrayList<String> list) {

        ArrayList<DataModel> recentlyViewedList = new ArrayList<>();

        for (int i=0 ; i<list.size();++i)
            recentlyViewedList.add(null);

        for ( String volumeId: list) {

            Log.d(TAG, "showRecentlyViewed: volumeIds "+volumeId);
            url = "https://www.googleapis.com/books/v1/volumes/" + volumeId + "?key=" + API_KEY;

            JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject volumeInfo;
                    try {
                        booksDataList = new ArrayList<>();
                        DataModel dataModel = new DataModel();

                        dataModel.setVolumeId(volumeId);

                        volumeInfo = response.getJSONObject("volumeInfo");


                        // Cannot use arrayList because i may have to add objects at position larger than size of arrayList
                        // It throws indexOutOfBoundException

                        int index = list.indexOf(volumeId);

                        recentlyViewedList.remove(index);
                        Log.d(TAG, "onResponse: adding "+volumeId +" at "+index);
                        recentlyViewedList.add(index,storeData(volumeInfo, dataModel));

//                          recentViewList[list.indexOf(volumeId)] = storeData(volumeInfo,dataModel);
                        Log.i(TAG, "onResponse: recentlyView volumeId "+volumeId);
                        Log.d(TAG, "onResponse: recentList size "+recentlyViewedList.size());
                        Log.d(TAG, "onResponse: List size "+list.size());

                        if (!recentlyViewedList.contains(null)) {

                            //to sort the recentlyViewedList according to list.
                        //    sortRecentlyViewedList(recentlyViewedList,list);

                            recentlyViewed_rv.setAdapter(new ShelfAdapter(context, recentlyViewedList));
//                            recentlyViewed_rv.setAdapter((new ShelfAdapter(context,dataModel)));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e(TAG, "RecentlyViewed VolumeId ERROR " +error.getMessage() );
                }
            });

            requestQueue.add(objectRequest);
        }
    }

    private void sortRecentlyViewedList(ArrayList<DataModel> recentlyViewedList, ArrayList<String> list) {

        for (int i=0; i< list.size(); ++i) {
            DataModel getDataModel = recentlyViewedList.get(i);

            Log.d(TAG, "onResponse: got getDataModel");

            if (! list.get(i).equals(getDataModel.getVolumeId()))

                for (int j=i+1; j< recentlyViewedList.size(); j++) {

                    DataModel swapDataModel = recentlyViewedList.get(j);

                    if ( list.get(i).equals(swapDataModel.getVolumeId())){

                        Log.d(TAG, "onResponse: swapping");

                        recentlyViewedList.remove(j);
                        recentlyViewedList.add(j, getDataModel);
                        recentlyViewedList.remove(i);
                        recentlyViewedList.add(i,swapDataModel);

                    }
                    Log.d(TAG, "onResponse: recentlyList size "+recentlyViewedList.size());
                    Log.d(TAG, "onResponse: j "+j);
                }
            Log.i(TAG, "onResponse: RecentlyViewList "+recentlyViewedList.get(i).getVolumeId());
        }



    }

    public void getBookshelf(RecyclerView bookshelf_rv, List<String> bookshelfList) {

        ArrayList<DataModel> bookList = new ArrayList<>();

        for ( String volumeId: bookshelfList ) {

            url = "https://www.googleapis.com/books/v1/volumes/" + volumeId + "?key=" + API_KEY;

            JsonObjectRequest objectRequest = new JsonObjectRequest(GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    JSONObject volumeInfo;
                    try {

                        DataModel dataModel = new DataModel();
                        dataModel.setVolumeId(volumeId);

                        volumeInfo = response.getJSONObject("volumeInfo");

                        bookList.add(storeData(volumeInfo, dataModel));
                        Log.i("TAG", "book list "+bookList.size());
                        bookshelf_rv.setAdapter(new RecentlyViewedAdapter(bookList));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e(TAG, "getBookshelf VolumeId ERROR " +error.getMessage() );
                }
            });

            requestQueue.add(objectRequest);
        }
        Log.i(TAG, "getBookshelf: setting Adapter from Network "+bookList.size());

     }
}



