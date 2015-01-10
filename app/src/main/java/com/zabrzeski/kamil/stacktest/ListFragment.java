package com.zabrzeski.kamil.stacktest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListFragment extends Fragment {

    private OnURLSelectedListener mListener;
    private String userId;
    private ArrayList<HashMap<String, String>> postsList = new ArrayList<HashMap<String, String>>();
    private List<String> urlList = new ArrayList<String>();

    private static final String TAG_POST_ID = "post_id";
    private static final String TAG_POST_LINK = "link";
    private static final String TAG_POST_SCORE = "score";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayListView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userId = getArguments().getString("userId");
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }


    // Container Activity must implement this interface
    public interface OnURLSelectedListener {
        public void onURLSelected(String URL, String link, String Store);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnURLSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnURLSelectedListener");
        }
    }

    private void displayListView() {
        Toast.makeText(getActivity(), "Ładowanie. Proszę czekać...", Toast.LENGTH_SHORT).show();
        new PostLoader().execute();


    }
    private void loadError()
    {
        Toast.makeText(getActivity(), "Błąd wczytwania danych użytkownika.Zmień id user.", Toast.LENGTH_SHORT).show();
    }



    private class PostLoader extends AsyncTask<String, String, String> {


         private JSONParser jParser = new JSONParser();
         private String url_all_posts = "https://api.stackexchange.com/2.2/users/";
         private JSONArray posts = null;
         private boolean isErrorUser=false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        protected String doInBackground(String... args) {

            url_all_posts = url_all_posts + userId + "/posts?order=desc&sort=activity&site=stackoverflow&";
            ///  Log.d("Url: ",url_all_posts );
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            JSONObject json = jParser.makeHttpRequest(url_all_posts, "GET", params);

            Log.d("All Posts: ", json.toString());
            if (json != null) {

                try {


                    posts = json.getJSONArray("items");

                    if(posts.length()>0) {
                        for (int i = 0; i < posts.length(); i++) {

                            JSONObject c = posts.getJSONObject(i);
                            JSONObject owner = c.getJSONObject("owner");
                            String score = c.getString(TAG_POST_SCORE);
                            String id = c.getString(TAG_POST_ID);
                            String link = c.getString(TAG_POST_LINK);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TAG_POST_ID, id);
                            map.put(TAG_POST_LINK, link);
                            map.put(TAG_POST_SCORE, score);
                            postsList.add(map);
                        }
                    }else
                    {
                        isErrorUser=true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("ERRRR");
            }
            return null;
        }


        protected void onPostExecute(String file_url) {

            for (int i = 0; i < postsList.size(); i++) {
                urlList.add(postsList.get(i).get(TAG_POST_ID));
            }


            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.posts_list, urlList);
            ListView listView = (ListView) getView().findViewById(R.id.listofURLs);

            listView.setAdapter(dataAdapter);

            listView.setTextFilterEnabled(true);

            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                 if(position<postsList.size())
                    mListener.onURLSelected(postsList.get(position).get(TAG_POST_LINK), postsList.get(position).get(TAG_POST_SCORE), postsList.get(position).get(TAG_POST_LINK));

                }
            });

            if(isErrorUser)
            loadError();
        }


    }

}
