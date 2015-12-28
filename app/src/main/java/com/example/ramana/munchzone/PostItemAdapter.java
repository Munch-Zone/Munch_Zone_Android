package com.example.ramana.munchzone;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PostItemAdapter extends ArrayAdapter<PostRestData> {
    private Activity myContext;
    private PostRestData[] datas;
    public PostItemAdapter(Context context, int textViewResourceId,PostRestData[] objects) {
        super(context, textViewResourceId, objects);
        // TODO Auto-generated constructor stub
        myContext = (Activity) context;
        datas = objects;
       
    }
   
	public View getView(int position, View convertView, ViewGroup parent) {
		
        LayoutInflater inflater = myContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null);
        
        TextView postRestName = (TextView) rowView
                .findViewById(R.id.rest_name);
        postRestName.setText(datas[position].rest_name);
        TextView postRestId = (TextView) rowView
                .findViewById(R.id.rest_id);
        postRestId.setText(datas[position].rest_id);
        TextView postRestLocation = (TextView) rowView
                .findViewById(R.id.rest_location);
        postRestLocation.setText(datas[position].rest_location);
        TextView postRestCuisienes = (TextView) rowView
                .findViewById(R.id.rest_cuisines);
        postRestCuisienes.setText(datas[position].rest_cuisines);
       
        SQLiteDatabase db = myContext.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
        Cursor c=db.rawQuery("SELECT * FROM restaurants where rest_id=\""+datas[position].rest_id+"\"", null);
        if(c.moveToFirst())
        {
        	 String r_serving_type = c.getString(3);
             
             String r_has_delivery = c.getString(6);
          
                       
             LinearLayout rootLayout = (LinearLayout) rowView.findViewById(R.id.LinearLayout01);
             LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            		    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
         
             if(r_has_delivery.equals("true"))
             {
           
            	 ImageView iv = new ImageView(myContext);
            	 iv.setPadding(5, 5, 5, 5);
                 iv.setLayoutParams(params);
                 iv.setImageResource(R.drawable.delivery);
                 rootLayout.addView(iv);
             }
         
             	if(r_serving_type.equals("Veg"))
             	{
             		
             		ImageView iv = new ImageView(myContext);
             		 iv.setPadding(5, 5, 5, 5);
                    iv.setLayoutParams(params);
                    iv.setImageResource(R.drawable.veg);
                    rootLayout.addView(iv);
             	}
             	else
             	{
             		ImageView iv = new ImageView(myContext);
            		 iv.setPadding(5, 5, 5, 5);
                   iv.setLayoutParams(params);
                   iv.setImageResource(R.drawable.veg_non_veg);
                   rootLayout.addView(iv);
             	}
        }
        c.close();
        db.close();
return rowView;
    }

}



