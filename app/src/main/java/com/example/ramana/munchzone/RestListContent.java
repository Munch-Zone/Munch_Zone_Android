package com.example.ramana.munchzone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RestListContent extends Fragment implements TextWatcher  {
	private static SQLiteDatabase db;

	  FragmentManager fragmentManager;
	 public   ProgressDialog pd;
		public static ListView listview;
		public static TextView tv;
		 static Cursor c_filter = null;
        // Cursor c_nill = null;
         static int listDataCount;
        // int listsCount;
         static PostRestData[] listData ;
         //public static String[]  lists;
         int key;
        
         public static ProgressDialogFragment newFragment; 
    

	    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
	   public static  void populate(Context context)
	    {
	    	  listDataCount=-1;
	    		 //  listsCount=-1;
	    		 //  System.out.println("Restaurant ListContent is called");
	    		 
	  
	    	      db=context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
	    		      Cursor c1=db.rawQuery("SELECT * FROM cuisines_list WHERE cus_status=\"true\" ", null);
	    	         Cursor c2=db.rawQuery("SELECT * FROM locations_list WHERE loc_status=\"true\" ", null);
	    	       
	    	         if((c1.getCount()>0 ) && (c2.getCount()>0))
	    	         { 
	    	        	
	    	        	 c_filter=db.rawQuery("SELECT DISTINCT restaurants.rest_id,restaurants.rest_name,restaurants.rest_location_id FROM restaurants LEFT JOIN rest_cuisine LEFT JOIN cuisines_list LEFT JOIN locations_list ON restaurants.rest_id=rest_cuisine.rest_id AND restaurants.rest_location_id=locations_list.loc_id AND rest_cuisine.cus_id=cuisines_list.cus_id  WHERE cuisines_list.cus_status=\"true\" AND locations_list.loc_status=\"true\"  ", null);
	    	        	   //c_nill=db.rawQuery("SELECT DISTINCT restaurants.rest_id,restaurants.rest_name,restaurants.rest_location_id FROM restaurants LEFT JOIN rest_cuisine LEFT JOIN locations_list ON restaurants.rest_id=rest_cuisine.rest_id AND restaurants.rest_location_id=locations_list.loc_id  WHERE rest_cuisine.cus_id=\"Nill\" AND locations_list.loc_status=\"true\"  ", null);
	    	        	  
	    	   
	    	      
	    	         }
	    	         else if(c1.getCount()>0 && c2 .getCount()==0)
	    	         {
	    	         c_filter=db.rawQuery("SELECT DISTINCT restaurants.rest_id,restaurants.rest_name,restaurants.rest_location_id FROM restaurants LEFT JOIN rest_cuisine LEFT JOIN cuisines_list ON restaurants.rest_id=rest_cuisine.rest_id AND rest_cuisine.cus_id=cuisines_list.cus_id WHERE cuisines_list.cus_status=\"true\"", null);
	    	        // c_nill=db.rawQuery("SELECT DISTINCT restaurants.rest_id,restaurants.rest_name,restaurants.rest_location_id FROM restaurants LEFT JOIN rest_cuisine LEFT JOIN locations_list ON restaurants.rest_id=rest_cuisine.rest_id AND restaurants.rest_location_id=locations_list.loc_id  WHERE rest_cuisine.cus_id=\"Nill\" AND locations_list.loc_status=\"true\"  ", null);
	    	         
	    	             
	    	         }else if(c1.getCount()==0 && c2 .getCount()>0)
	    	         {
	    	         c_filter=db.rawQuery("SELECT DISTINCT restaurants.rest_id,restaurants.rest_name,restaurants.rest_location_id FROM restaurants LEFT JOIN locations_list ON restaurants.rest_location_id=locations_list.loc_id  WHERE locations_list.loc_status=\"true\" ", null);
	    	             
	    	         }
	    	         else if(c1.getCount()==0 && c2 .getCount()==0)
	    	         {
	    	         c_filter=db.rawQuery("SELECT DISTINCT rest_id,rest_name,rest_location_id FROM restaurants", null);
	    	            
	    	         }
	    	         c1.close(); c2.close();
	    	           	 
	    	             listData = new PostRestData[c_filter.getCount()];
	    	         
	    	         if(c_filter.getCount()>0)
	    		       {
	    		    	   
	    	        	
	    	           	PostRestData data;  	
	    	           	 while(c_filter.moveToNext())
	    	        		{
	    	           		 listDataCount++;
	    	           		// System.out.println("Inside c_filter");
	    	           		 data = new PostRestData();

	    	           		    data.rest_id = c_filter.getString(0);
	    	           		  
	    	           		    data.rest_name = c_filter.getString(1);
	    	           		 
	    	           		 Cursor t=db.rawQuery("SELECT loc_name FROM locations where loc_id LIKE \""+c_filter.getString(2)+"\"", null);
	    	                    if(t.moveToNext())
	    	                    	 data.rest_location = t.getString(0);
	    	                   t.close();
	    	                    Cursor c=db.rawQuery("SELECT cus_id FROM rest_cuisine where rest_id=\""+data.rest_id+"\"", null);
	    	                    String Cuisines = "";
	    	                    while(c.moveToNext())
	    	                    {
	    	                   	 if(! c.getString(0).equals("Nill"))
	    	                   	 {
	    	                   	  t=db.rawQuery("SELECT cus_name FROM cuisines where cus_id=\""+c.getString(0)+"\"", null);
	    	                   	 
	    	                   	  while(t.moveToNext())
	    	                   	  {
	    	                   		  Cuisines+=(t.getString(0))+", ";
	    	                   	  }
	    	                   	  t.close();
	    	                    }
	    	                    }
	    	                    c.close();
	    	                    if(Cuisines.length()>2)
	    	                        Cuisines=Cuisines.substring(0, Cuisines.length() - 2);
	    	               else
	    	               	Cuisines="";
	    	                    data.rest_cuisines = Cuisines+"\b";
	    	                    data.rest_timing = "Under Development";
	    	                    
	    	           		
	    	                    listData[listDataCount]=data;
	    	              
	    	               
	    	                    
	    	                  
	    	        }
	    	           	
	    	        
	    	           	
	    		       }
	    	         
	    		  
	    	         else
	    		       {
	    		    
	    		    	   AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    	             builder.setTitle(" Search Results ");
	    	             builder.setMessage("No result found")  
	    	                    .setCancelable(false)
	    	                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
	    	                        public void onClick(DialogInterface dialog, int id) {
	    	                        }
	    	                    });                     
	    	             AlertDialog alert = builder.create();
	    	             alert.show();    
	    		       }
	    		    	  
	    		    	   c_filter.close();
	    		    	   db.close();
	    		    	   
	    		    	   PostItemAdapter itemAdapter = new PostItemAdapter(context,R.layout.list_item, listData);
	    		           	
	    		               listview.setAdapter(itemAdapter);    
	    		             
	    		             

	    }
	   public static void populateList(View v)
	   {
		//   populate(v);
	   }

   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint("NewApi")
public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
		   View v = inflater.inflate(R.layout.restaurant_list, container, false);
		   listview=	 (ListView)v.findViewById(R.id.Postlistview);
		   tv=(TextView)v.findViewById(R.id.load);
		   populate(getActivity());
		   
	               listview.setOnItemClickListener(new OnItemClickListener() {
	            		
	       			@Override
	       			public void onItemClick(AdapterView<?> parent, final View view,
	       					int position, long id) {
	       			//	System.out.println("In Single Item Click");
	       				RestListContent.listview.setEnabled(false);
	       			//	System.out.println("listview + Enabale" +RestListContent.listview.isEnabled());
	       				if( RestaurantList.acTextView.isFocused())
	       				{
	       					RestaurantList.acTextView.clearFocus();
	       					InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	       		         inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	       					RestaurantList.call();
	       				}
	       				else
	       				{
	       			 ConnectivityManager connec =  
	                           (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	             
	               // Check for network connections
	                if ( connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTED ||
	                     connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == android.net.NetworkInfo.State.CONNECTING ||
	                     connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTING ||
	                     connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == android.net.NetworkInfo.State.CONNECTED) {
	       				final String rest_name = ((TextView) view.findViewById(R.id.rest_name))
    	       					.getText().toString();
    	       				final String rest_id = ((TextView) view.findViewById(R.id.rest_id))
    	       						.getText().toString();
    	       				pd=null;
    	       			   pd= new ProgressDialog(getActivity());
    		       			pd.setCancelable(false);
    		                pd.setMessage("Loading Menu ...");
    	       			   pd.show();
    	       		
    	       				Thread mThread = new Thread() {
                          	    @Override
                          	    public void run() {
                          	      
                          	    	
                	             
                          	    	Looper.prepare();
                	       				final RestaurantMenuJSONParsing RMJP=new RestaurantMenuJSONParsing(/*db,*/rest_id,rest_name,getActivity(),pd);
                	                	    RMJP.populateResMenuData();
                	                	  
                          	    }
                          	    };
                          	    mThread.start();
                          	              	  try {
      							mThread.join();
      						} catch (InterruptedException e) {
      							// TODO Auto-generated catch block
      			 				e.printStackTrace();
      						} 

                                 
                                      	
	       			}
	       			else if ( 
	                        connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
	                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
	                      
	                         showAlert();
	       			}
	       				}
	       				RestListContent.listview.setEnabled(true);
	       			}

					private void showAlert() {
						// TODO Auto-generated method stub
						 getActivity().runOnUiThread(new Runnable() {
					            public void run() {
					                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
	       		});
	              
	        
	          
	             
        return v;
    }
  
  
   @Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}


}
