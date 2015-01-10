package com.zabrzeski.kamil.stacktest;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button go;
    private EditText idUser;
    private SharedPreferences preferences;
    static Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        go = (Button) findViewById(R.id.bGo);
        go.setOnClickListener(this);
        idUser = (EditText) findViewById(R.id.etUserNumber);
        preferences = getSharedPreferences("temp", getApplicationContext().MODE_PRIVATE);
        mContext=this;


    }

    private static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(mContext.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private static void showOfflineDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Brak połączenia");
        builder.setMessage("Połącz się z internetem i spróbuj ponownie.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }

        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bGo:
                if (isOnline()) {

                    String id = idUser.getText().toString();
                    if (id != null && !id.isEmpty()) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userId", id);
                        editor.commit();

                        Intent toFragment = new Intent(this, FragmentActivity.class);
                        startActivity(toFragment);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wprowadź id user i kontynuuj.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showOfflineDialog();
                }


                break;
        }
    }
}
