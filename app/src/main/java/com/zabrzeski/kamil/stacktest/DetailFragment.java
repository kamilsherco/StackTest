package com.zabrzeski.kamil.stacktest;

/**
 * Created by Kamil on 2015-01-09.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    private String mURL = "";
    private String store = "";
    private String link = "";
    private TextView linkTv;
    private TextView storeTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mURL = savedInstanceState.getString("currentURL", "");
            store = savedInstanceState.getString("store", "");
            link = savedInstanceState.getString("link", "");
        }
        if (!mURL.trim().equalsIgnoreCase("")) {
            linkTv = (TextView) getView().findViewById(R.id.tvLink);
            storeTv = (TextView) getView().findViewById(R.id.tvStore);


            WebView myWebView = (WebView) getView().findViewById(R.id.pageInfo);
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.setWebViewClient(new MyWebViewClient());
            myWebView.loadUrl(mURL.trim());
            linkTv.setText(link);
            storeTv.setText(store);

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentURL", mURL);
        outState.putString("store", store);
        outState.putString("link", link);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        return view;
    }

    public void setURLContent(String URL) {
        mURL = URL;
    }

    public void setStore(String STORE) {
        store = STORE;


    }

    public void setLink(String LINK) {
        link = LINK;

    }

    public void updateURLContent(String URL, String LINK, String STORE) {


        mURL = URL;
        link=LINK;
        store=STORE;

        WebView myWebView = (WebView) getView().findViewById(R.id.pageInfo);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl(mURL.trim());
        linkTv = (TextView) getView().findViewById(R.id.tvLink);
        storeTv = (TextView) getView().findViewById(R.id.tvStore);
        linkTv.setText(link);
        storeTv.setText(store);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}