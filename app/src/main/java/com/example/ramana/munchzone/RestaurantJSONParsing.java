package com.example.ramana.munchzone;



import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;



 public class RestaurantJSONParsing  {
	
private Context context; 
    
    private ListView listView; 
   private AutoCompleteTextView acTextView;
 
    SQLiteDatabase db;
    
 //   private static String url = "http://10.0.2.2/MunchZone/restaurant.json";
 
  private static String url= "http://mz.stayinuae.com:81/restaurants.json";
	private static final String TAG_RESTAURANTS = "restaurants";
	private static final String TAG_LOCATIONS = "locations";
	private static final String TAG_CUISINES = "cuisines";
	private static final String TAG_REST_ID = "id";
	private static final String TAG_REST_NAME = "name";
	private static final String TAG_LOCATION_ID = "location_id";
	private static final String TAG_ADDRESS = "address";
	 private static final String TAG_REST_ALLOWS_PICKUP = "allows_pickup";
	    private static final String TAG_REST_HAS_CATERING = "has_catering";
	    private static final String TAG_REST_HAS_DELIVERY = "has_delivery";
	    private static final String TAG_REST_HAS_PARTY_BOOKING = "has_party_booking";    
	    private static final String TAG_REST_HAS_TABLE_BOOKING = "has_table_booking";
	    private static final String TAG_REST_SERVING_TYPE = "serving_type";
	private static final String TAG_LOC_ID = "id";
	private static final String TAG_LOC_NAME = "name";
	private static final String TAG_CUI_ID = "id";
	private static final String TAG_CUI_NAME = "name";
	
	    // contacts JSONArray
    JSONArray restaurants = null;
    JSONArray locations = null;
    JSONArray cuisines = null;
	public Activity activity;

	 RestaurantJSONParsing(Context context)
	 {
		 this.context=context;
	 }
	 

    public int populateResData() {
     
        db=context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS restaurants" );  
        db.execSQL("DROP TABLE IF EXISTS locations" ); 
        db.execSQL("DROP TABLE IF EXISTS cuisines" ); 
        db.execSQL("DROP TABLE IF EXISTS rest_cuisine" ); 
        db.execSQL("DROP TABLE IF EXISTS locations_list" ); 
        db.execSQL("DROP TABLE IF EXISTS cuisines_list" );
        db.execSQL("DROP TABLE IF EXISTS selected_restaurant" );  
        db.execSQL("DROP TABLE IF EXISTS restaurant_menu" ); 
        db.execSQL("DROP TABLE IF EXISTS restaurant_PC" ); 
        db.execSQL("DROP TABLE IF EXISTS restaurant_Cart" );
      	db.execSQL("CREATE TABLE IF NOT EXISTS restaurants(rest_id VARCHAR,rest_name VARCHAR,rest_location_id VARCHAR,rest_serving_type VARCHAR"
       		+ ",rest_allows_pickup VARCHAR,rest_has_catering VARCHAR,rest_has_delivery VARCHAR,rest_has_party_booking VARCHAR,rest_has_table_booking VARCHAR);");
    	db.execSQL("CREATE TABLE IF NOT EXISTS locations(loc_id VARCHAR,loc_name VARCHAR);");
    	db.execSQL("CREATE TABLE IF NOT EXISTS cuisines(cus_id VARCHAR,cus_name VARCHAR);");
    	db.execSQL("CREATE TABLE IF NOT EXISTS rest_cuisine(rest_id VARCHAR,cus_id VARCHAR);");
    	db.execSQL("CREATE TABLE IF NOT EXISTS locations_list(loc_id VARCHAR,loc_status  VARCHAR);");
    	db.execSQL("CREATE TABLE IF NOT EXISTS cuisines_list(cus_id VARCHAR,cus_status  VARCHAR);");
    	db.execSQL("DROP TABLE IF EXISTS selected_restaurant" );  
        db.execSQL("DROP TABLE IF EXISTS restaurant_menu" ); 
        db.execSQL("DROP TABLE IF EXISTS restaurant_PC" ); 
        db.execSQL("CREATE TABLE IF NOT EXISTS selected_restaurant(rest_id VARCHAR,rest_info VARCHAR,rest_class_type VARCHAR,rest_serving_type VARCHAR"
           		+ ",rest_allows_pickup VARCHAR,rest_has_catering VARCHAR,rest_has_delivery VARCHAR,rest_has_party_booking VARCHAR,rest_has_table_booking VARCHAR, rest_name VARCHAR, rest_loc_id VARCHAR, rest_address VARCHAR);");
         	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_menu(menu_id VARCHAR,menu_name VARCHAR, menu_price VARCHAR,menu_product_category_id VARCHAR);");
    	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_PC(pc_id VARCHAR,pc_name VARCHAR);");
    	 db.execSQL("CREATE TABLE IF NOT EXISTS selected_restaurant(rest_id VARCHAR,rest_info VARCHAR,rest_class_type VARCHAR,rest_serving_type VARCHAR"
    	       		+ ",rest_allows_pickup VARCHAR,rest_has_catering VARCHAR,rest_has_delivery VARCHAR,rest_has_party_booking VARCHAR,rest_has_table_booking VARCHAR);");
    	   	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_menu(menu_id VARCHAR,menu_name VARCHAR, menu_price int,menu_product_category_id VARCHAR);");
    	   	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_PC(pc_id VARCHAR,pc_name VARCHAR);");
    	   	db.execSQL("CREATE TABLE IF NOT EXISTS restaurant_Cart(item_id VARCHAR,item_name VARCHAR,item_price int,item_qty int);");
    	   	
       db.close();
    
			 
    	GetRestaurantDetails m= (GetRestaurantDetails) new GetRestaurantDetails().execute();
    	try {  
    		  
    	     Void temp = m.get();  
    	  
    	    } catch (InterruptedException e) {  
    	  
    	          // TODO Auto-generated catch block  
    	  
    	          e.printStackTrace();  
    	  
    	    } catch (ExecutionException e) {  
    	  
    	         // TODO Auto-generated catch block  
    	  
    	         e.printStackTrace();  
    	   }  
           return 0;
			
    }
    
   
    /**
     * Async task class to get json by making HTTP call
     * */
  
    		
    private class GetRestaurantDetails extends AsyncTask<Void, Void, Void> {
 
        @Override
        protected void onPreExecute() {
        	
            super.onPreExecute();
            db=context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
          
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
         
            ServiceHandler sh = new ServiceHandler();
          
           
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
          
         
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
             
                    restaurants = jsonObj.getJSONArray(TAG_RESTAURANTS);
                    locations= jsonObj.getJSONArray(TAG_LOCATIONS);
                    cuisines= jsonObj.getJSONArray(TAG_CUISINES);
                   
                    for (int i = 0; i < restaurants.length(); i++) {
                    	
                        JSONObject c = restaurants.getJSONObject(i); 
                    
                       String r_id = c.getString(TAG_REST_ID);
                    
                       String r_name = c.getString(TAG_REST_NAME).replaceAll("'", "");
                       String r_loc_id = c.getString(TAG_LOCATION_ID);
                       String r_address = c.getString(TAG_ADDRESS);
                       String r_allows_pickup = c.getString(TAG_REST_ALLOWS_PICKUP);
	                    String r_has_catering = c.getString(TAG_REST_HAS_CATERING);
	                    String r_has_delivery = c.getString(TAG_REST_HAS_DELIVERY);
	                    String r_has_party_booking = c.getString(TAG_REST_HAS_PARTY_BOOKING);
	                    String r_has_table_booking = c.getString(TAG_REST_HAS_TABLE_BOOKING);
	                    String r_serving_type = c.getString(TAG_REST_SERVING_TYPE);
                       JSONArray cuisine=c.getJSONArray(TAG_CUISINES);
                     
                       r_name=r_name.replaceAll("\"", "");
                       db.execSQL("INSERT INTO restaurants VALUES(\""+r_id+"\",\""+r_name+"\",\""+r_loc_id+"\",\""+r_serving_type+"\",\""+r_allows_pickup+"\",\""+r_has_catering+"\",\""+r_has_delivery+"\",\""+r_has_party_booking+"\",\""+r_has_table_booking+"\");");
                  
                       for(int j=0;j< cuisine.length();j++)
                       {
                    	   JSONObject t = cuisine.getJSONObject(j);
                    	 
                    	   String cus_id=t.getString(TAG_CUI_ID);
                    	   db.execSQL("INSERT INTO rest_cuisine VALUES(\""+r_id+"\",\""+cus_id+"\");");
                    	   
                       }
                       if(cuisine.length()==0)
                       {
                    	   db.execSQL("INSERT INTO rest_cuisine VALUES(\""+r_id+"\",\"Nill\");");
                       }
                    
                       
                    }
                    for (int i = 0; i < locations.length(); i++) {
                     JSONObject c = locations.getJSONObject(i); 
                      String l_id = c.getString(TAG_LOC_ID);
                       String l_name = c.getString(TAG_LOC_NAME);
                       db.execSQL("INSERT INTO locations VALUES(\""+l_id+"\",\""+l_name+"\");");
                        		                       
                    }
              
                    for (int i = 0; i < cuisines.length(); i++) {
	
                    	JSONObject c = cuisines.getJSONObject(i); 
   
   String c_id = c.getString(TAG_CUI_ID);
   String c_name = c.getString(TAG_CUI_NAME);
   db.execSQL("INSERT INTO cuisines VALUES(\""+c_id+"\",\""+c_name+"\");");
}
             
          		Cursor c=db.rawQuery("SELECT DISTINCT rest_location_id FROM restaurants", null);
          		while(c.moveToNext())
                {
          			 db.execSQL("INSERT INTO locations_list VALUES(\""+c.getString(0)+"\", \"true\");");
                }
          		c.close();
          		c=db.rawQuery("SELECT DISTINCT cus_id FROM rest_cuisine", null);
          		while(c.moveToNext())
                {
          			if(! (c.getString(0).equals("Nill")))
          			 db.execSQL("INSERT INTO cuisines_list VALUES(\""+c.getString(0)+"\", \"true\");");
                }c.close();
          		                       
                    
                } catch (JSONException e) {
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
           
         
   	 
        }
 
    }


	
   
  

	
  
}