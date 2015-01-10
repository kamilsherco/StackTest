package com.zabrzeski.kamil.stacktest;

import android.app.Activity;

import com.zabrzeski.kamil.stacktest.ListFragment.OnURLSelectedListener;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class FragmentActivity extends Activity implements OnURLSelectedListener {

    boolean detailPage = false;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);

        setContentView(R.layout.activity_main_fragment);

        if(savedInstanceState == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Bundle bundle=new Bundle();
            bundle.putString("userId",preferences.getString("userId",null) );
            ListFragment listFragment = new ListFragment();
            listFragment.setArguments(bundle);
            ft.add(R.id.displayList, listFragment, "List_Fragment");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        }

        if(findViewById(R.id.displayDetail) != null){
            detailPage = true;
            getFragmentManager().popBackStack();

            DetailFragment detailFragment = (DetailFragment) getFragmentManager().findFragmentById(R.id.displayDetail);
            if(detailFragment == null){
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                detailFragment = new DetailFragment();
                ft.replace(R.id.displayDetail, detailFragment, "Detail_Fragment1");
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }

    }


    @Override
    public void onURLSelected(String URL,String LINK,String STORE) {
        Log.v("AndroidFragmentActivity",URL);

        if(detailPage){
            DetailFragment detailFragment = (DetailFragment)
            getFragmentManager().findFragmentById(R.id.displayDetail);
            detailFragment.updateURLContent(URL,LINK,STORE);

        }
        else{
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setURLContent(URL);
            detailFragment.setLink(LINK);
            detailFragment.setStore(STORE);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.displayList, detailFragment, "Detail_Fragment2");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

}
