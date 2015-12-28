package com.example.ramana.munchzone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        check();
    }

    private void check() {

        SQLiteDatabase db = openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
        Cursor c = null;
        boolean tableExists = false;

        try
        {
            c = db.query("restaurants", null, null, null, null, null, null);
            tableExists = true;
        }
        catch (Exception e) {
    	    /* fail */

        }


        if(!tableExists)
        {


            ConnectivityManager connec =
                    (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if ( connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTED) {

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        RestaurantJSONParsing RJP=new RestaurantJSONParsing(MainActivity.this);

                        int temp= RJP.populateResData();
                        Intent ListDisplay = new Intent(getBaseContext(),RestaurantList.class);
                        startActivity(ListDisplay);

                    }
                };
                mThread.start();

            } else if (

                    connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

                Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
                showAlert();
                setContentView(R.layout.activity_main);

            }

        }
        else
        {

            System.out.println("Table Exists");
            setContentView(R.layout.activity_splash_screen);
            Thread background = new Thread() {
                public void run() {

                    try {

                        synchronized(this){
                            sleep(1*1000);
                        }

                        Intent ListDisplay = new Intent(getBaseContext(),RestaurantList.class);
                        startActivity(ListDisplay);


                        finish();

                    } catch (Exception e) {

                    }
                }
            };

            // start thread
            background.start();

        }
    }

    public void showAlert(){
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(" Connection Error ");
                builder.setMessage("Please check your internet connection")
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
