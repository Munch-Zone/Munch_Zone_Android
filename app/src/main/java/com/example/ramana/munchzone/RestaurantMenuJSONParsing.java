




package com.example.ramana.munchzone;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.AdapterView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CursorAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;


 
public class RestaurantMenuJSONParsing  {
	
 Context context; 
  
   public static String rest_id;
   String rest_name;
   
 
    SQLiteDatabase db;
    
    //private String url = "http://10.0.2.2/MunchZone/";
 private String url ="http://mz.stayinuae.com:81/restaurants/";
    private static final String TAG_RES_DETAILS = "ResDetails";
    private static final String TAG_REST_ID = "id";
    private static final String TAG_REST_ADDRESS = "address";
	private static final String TAG_REST_NAME = "name";
    private static final String TAG_REST_INFO ="info";
    private static final String TAG_REST_CLASS_TYPE = "class_type";
    private static final String TAG_REST_ALLOWS_PICKUP = "allows_pickup";
    private static final String TAG_REST_HAS_CATERING = "has_catering";
    private static final String TAG_REST_HAS_DELIVERY = "has_delivery";
    private static final String TAG_REST_HAS_PARTY_BOOKING = "has_party_booking";    
    private static final String TAG_REST_HAS_TABLE_BOOKING = "has_table_booking";
    private static final String TAG_REST_SERVING_TYPE = "serving_type";
    private static final String TAG_LOCATION_ID = "location_id";
    private static final String TAG_MENU = "Menu";
    private static final String TAG_MENU_ID = "id";
    private static final String TAG_MENU_NAME = "name";
    private static final String TAG_MENU_PRICE = "price";
    private static final String TAG_MENU_PRODUCT_CATEGORY_ID = "product_category_id";
    
    private static final String TAG_PC = "PC";
    private static final String TAG_PC_ID = "id";
    private static final String TAG_PC_NAME = "name";
   
    private Activity activity;
	 ProgressDialog pd;
	 RestaurantMenuJSONParsing(String r_id, String rest_n, Activity activity, ProgressDialog pd)
	 {
		 this.context=activity;
	this.pd=pd;
		
		 this.rest_id=r_id;
		 this.rest_name=rest_n;
		 rest_name=rest_name.replaceAll(",","");
		 rest_name=rest_name.replaceAll(" ","");
		 url=url+rest_id+"-"+rest_name+".json";
		 this.activity=activity;
		 System.out.println(rest_id);
		 System.out.println(rest_name);
	 }
	 

    public int populateResMenuData() {
     
     
    	 db=context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
    	
   
       db.execSQL("DROP TABLE IF EXISTS selected_restaurant" );  
       db.execSQL("DROP TABLE IF EXISTS restaurant_menu" ); 
       db.execSQL("DROP TABLE IF EXISTS restaurant_PC" ); 
   
       db.execSQL("DROP TABLE IF EXISTS restaurant_Cart" );
       db.execSQL("CREATE TABLE IF NOT EXISTS selected_restaurant(rest_id VARCHAR,rest_info VARCHAR,rest_class_type VARCHAR,rest_serving_type VARCHAR"
       		+ ",rest_allows_pickup VARCHAR,rest_has_catering VARCHAR,rest_has_delivery VARCHAR,rest_has_party_booking VARCHAR,rest_has_table_booking VARCHAR, rest_name VARCHAR, rest_loc_id VARCHAR, rest_address VARCHAR);");
   	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_Cart(item_id VARCHAR,item_name VARCHAR,item_price int,item_qty int);");
   	
	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_menu(menu_id VARCHAR,menu_name VARCHAR, menu_price int,menu_product_category_id VARCHAR);");
   	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_PC(pc_id VARCHAR,pc_name VARCHAR);");

   	db.close();
    	GetRestaurantMenuDetails m= (GetRestaurantMenuDetails) new GetRestaurantMenuDetails(activity).execute();
    	try {  
    		  
    	   
    		
    	  
    	    } catch (Exception e) {  
    	  
    	         // TODO Auto-generated catch block  
    	  
    	         e.printStackTrace();  
    	   }  
           return 0;
			
    }
    
   
    private class GetRestaurantMenuDetails extends AsyncTask<Void, Void, Void> {
    	//private ProgressDialog pd;
    	 private Activity activity;
    	 GetRestaurantMenuDetails(Activity activity)
    	 {
    		 this.activity = activity;
    		
    	 }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           
            db=context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
         
//System.out.println("In preexecute");
            
          }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            
            ServiceHandler sh = new ServiceHandler();
       //     System.out.println("In execute");       
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
          
            if (jsonStr != null) {
	            try {
					System.out.println(jsonStr);
	                JSONArray jsonarray = new JSONArray(jsonStr);
					System.out.println(2);
	                    JSONObject complete_rest_info= jsonarray.getJSONObject(1); 
	                 
	                    
	                    JSONObject details= complete_rest_info.getJSONObject(TAG_RES_DETAILS);
	                   
	                    String r_id = details.getString(TAG_REST_ID);
	                    String r_address = details.getString(TAG_REST_ADDRESS);
	                    String r_loc_id = details.getString(TAG_LOCATION_ID);
	                    String r_name = details.getString(TAG_REST_NAME).replaceAll("'", " ");
	                    String r_info = details.getString(TAG_REST_INFO).replaceAll("'", " ");
	                    String r_class_type = details.getString(TAG_REST_CLASS_TYPE);
	                    String r_allows_pickup = details.getString(TAG_REST_ALLOWS_PICKUP);
	                    String r_has_catering = details.getString(TAG_REST_HAS_CATERING);
	                    String r_has_delivery = details.getString(TAG_REST_HAS_DELIVERY);
	                    String r_has_party_booking = details.getString(TAG_REST_HAS_PARTY_BOOKING);
	                    String r_has_table_booking = details.getString(TAG_REST_HAS_TABLE_BOOKING);
	                    String r_serving_type = details.getString(TAG_REST_SERVING_TYPE);
	                    r_name=r_name.replaceAll("\"", " ");
	                    r_info=r_info.replaceAll("\"", " ");
	                    db.execSQL("INSERT INTO selected_restaurant VALUES(\""+r_id+"\",\""+r_info+"\",\""+r_class_type+"\",\""+r_serving_type+"\",\""+r_allows_pickup+"\",\""+r_has_catering+"\",\""+r_has_delivery+"\",\""+r_has_party_booking+"\",\""+r_has_table_booking+"\",\"" +r_name+ "\",\"" +r_loc_id+ "\",\"" +r_address+ "\");");
	                     
	                  JSONObject menu= complete_rest_info.getJSONObject(TAG_MENU);
	      
	                 Iterator<String> cm=menu.keys();
	                   
	              
	                    cm=menu.keys();
	                   //// db.execSQL("DROP TABLE IF EXISTS restaurant_menu" ); 
                      //  db.execSQL("DROP TABLE IF EXISTS restaurant_PC" ); 
                    	//db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_menu(menu_id VARCHAR,menu_name VARCHAR, menu_price int,menu_product_category_id VARCHAR);");
                       	//db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_PC(pc_id VARCHAR,pc_name VARCHAR);");
                       
	                    while (cm.hasNext()) {
	                        String key = cm.next();
	                       
	                        JSONArray ja = menu.getJSONArray(key);
	                     
	                        for (int i=0; i<ja.length();i++)
	                        {
	                        	 JSONObject menu_item = ja.getJSONObject(i); 
	                        	 String m_id = menu_item.getString(TAG_MENU_ID);
	      	                   String m_name = menu_item.getString(TAG_MENU_NAME).replaceAll("'", " ");
	      	                 m_name=m_name.replaceAll("\"", " ");
	      	                 int m_price = menu_item.getInt(TAG_MENU_PRICE);
	      	               String m_product_category_id = menu_item.getString(TAG_MENU_PRODUCT_CATEGORY_ID);
	      	             db.execSQL("INSERT INTO restaurant_menu VALUES(\""+m_id+"\",\""+m_name+"\","+m_price+",\""+m_product_category_id+"\");");
System.out.println("in rest menu json parsing");
	      	               
	                        }
	                     
	                    }
	                	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_PC(pc_id VARCHAR,pc_name VARCHAR);");
	                    JSONArray pc= complete_rest_info.getJSONArray(TAG_PC);
	                    for (int i = 0; i < pc.length(); i++) {
	                    	
	                        JSONObject c = pc.getJSONObject(i); 
	                    
	                 
	                    
	                       String pc_id = c.getString(TAG_PC_ID ).replaceAll("'", " ");
	                       String pc_name = c.getString(TAG_PC_NAME);
	                       db.execSQL("INSERT  INTO restaurant_PC VALUES(\""+pc_id+"\",\""+pc_name+"\");");

	                       
	                    
	                       
	                    }
	            }
             catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return null;
    }

           
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           
            db.close();
           
          //  System.out.println("In post execute");
   	    
//if(pd!=null)
  		//  System.out.println("Is showing1 = "+pd.isShowing());
  /* 	 Bundle bundle=new Bundle();
			bundle.putString("TagRestID", rest_id);
	  RestMenuMainPanel.mTabsAdapter.ChangeBundle(bundle,0);
	  
	*/

	  if(pd!=null)
	  if(pd.isShowing())
	  {
		 // System.out.println("Is showing = "+pd.isShowing());
	pd.dismiss();
	  }
	  if(pd!=null)
	  pd.dismiss();
	 pd=null;
	 //RestListContent.pd.
	 // RestListContent.pd.cancel();
   
        }
 
    }


	
   
  

	
  
}