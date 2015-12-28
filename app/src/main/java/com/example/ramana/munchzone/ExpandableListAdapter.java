package com.example.ramana.munchzone;


import java.util.HashMap;
import java.util.List;











import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    SQLiteDatabase _db;
	private Context _context;
	private List<String> _listDataHeader; 
	private HashMap<String, List<String>> _listDataChild;
//	public static HashMap<String, String> listChildDataLocStatus = new HashMap<String, String>();
//	public static HashMap<String, String> listChildDataCusStatus = new HashMap<String, String>();
	String loc_id;
	String cus_id;
	FragmentManager _fm;
	public ExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listChildData, FragmentManager fm) {
		this._context = context;
	this._fm=fm;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		// HashMap<String, String> listChildDataLocStatus = new HashMap<String, String>();
		// HashMap<String, String> listChildDataCusStatus = new HashMap<String, String>();
	/*	 _db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
		Cursor c1=_db.rawQuery("SELECT * FROM locations_list", null);
		while(c1.moveToNext())
		 {
			listChildDataLocStatus.put(c1.getString(0), c1.getString(1));	
		 }
		c1.close();
		c1=_db.rawQuery("SELECT * FROM cuisines_list", null);
		while(c1.moveToNext())
		 {
			System.out.println("In table");
			System.out.println("Id= "+ c1.getString(0)+ "Status= "+ c1.getString(1));
			listChildDataCusStatus.put(c1.getString(0), c1.getString(1));	
			System.out.println("In array= " + listChildDataCusStatus.get(c1.getString(0)));
		 }
		c1.close();
		 _db.close();
		*/
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
			return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}
	public Object getChildStatus(int groupPosition, int childPosition) {
		final String groupText = (String) getGroup(groupPosition);
		final String childText = (String) getChild(groupPosition, childPosition);
		 _db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
		String status = "true";
		try
		{
		if(groupText.equals("Locations")) 
		{
			final Cursor c=_db.rawQuery("SELECT loc_id FROM locations WHERE loc_name LIKE '"+childText+"'", null);
				    		
			 if(c.moveToFirst())
			 {
		final Cursor c1=_db.rawQuery("SELECT loc_status FROM locations_list WHERE loc_id LIKE '"+c.getString(0)+"'", null);
			if(c1.moveToFirst())
			 {
				status=c1.getString(0);
			 }
			c1.close();
			/*
			
				 if( listChildDataLocStatus.get(c.getString(0)).equals("true"))	
	    			{
	    				status="true";
	    				
	    			}
	    			else
	    				status="false";*/
			 }
			 c.close();
			 
		}
		else
		{
			final Cursor c=_db.rawQuery("SELECT cus_id FROM cuisines WHERE cus_name LIKE '"+childText+"'", null);
    		
			 if(c.moveToFirst())
			 {
			final Cursor c1=_db.rawQuery("SELECT cus_status FROM cuisines_list WHERE cus_id LIKE '"+c.getString(0)+"'", null);
			if(c1.moveToFirst())
			 {
				status=c1.getString(0);
			 }
			c1.close();
				 /* if( listChildDataCusStatus.get(c.getString(0)).equals("true"))	
	    			{
	    				status="true";
	    				
	    			}
	    			else
	    				status="false"; */
			 }
			 c.close();
		}
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
		_db.close();
		if(status.equals("true"))
		return true;
		else
			return false;

	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
	
		return childPosition;
	}
	public long getChildIdStatus(int groupPosition, int childPosition) {
	
		return childPosition;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	    View view = convertView;
	    if (view == null) {
	    	LayoutInflater inflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        view = inflater.inflate(R.layout.alert_list_filter_item, null);
	    }
		return view;
		}
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		
		final String groupText = (String) getGroup(groupPosition);
		final String childText = (String) getChild(groupPosition, childPosition);
		final Boolean childTextStatus = (Boolean) getChildStatus(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.alert_list_filter_item, null);
		}

		final TextView txtListChild = (TextView) convertView
				.findViewById(R.id.checkListItem);
		
		txtListChild.setText(childText);
		 final ImageView txtListChildImage = (ImageView) convertView
				.findViewById(R.id.checkListItemImage);
		
		/*final CheckBox txtListChildCheck = (CheckBox) convertView
				.findViewById(R.id.checkListItemCheck);*/
		if(childTextStatus)
		txtListChildImage.setImageResource(R.drawable.checked);
		else
			txtListChildImage.setImageResource(R.drawable.unchecked); 
		/*if(childTextStatus)
			txtListChildCheck.setChecked(true);
			else
				txtListChildCheck.setChecked(false);
		txtListChildCheck.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 _db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
					boolean status = false;
					if(groupText.equals("Locations")) 
			    	{
			    		final Cursor c=_db.rawQuery("SELECT loc_id FROM locations WHERE loc_name LIKE '"+childText+"'", null);
			    		
			    		 if(c.moveToFirst())
			    		 {
			    			 final Cursor c1=_db.rawQuery("SELECT loc_status FROM locations_list WHERE  loc_id=\""+c.getString(0)+"\"", null);
			    			 if(c1.moveToFirst())
			    			 {
			    				 if(c1.getString(0).equals("true"))
			    					 status=true;
			    			 	 else
			    			 		 status=false;
			    			 }
			    			 if(status)
			    					txtListChildImage.setImageResource(R.drawable.unchecked);
			    				else
			    					txtListChildImage.setImageResource(R.drawable.checked); 
			    			 c1.close();
			    		_db.execSQL("UPDATE locations_list SET loc_status=\""+(! status)+"\" WHERE loc_id=\""+c.getString(0)+"\"");
			    	*/	
			    		/*	if( listChildDataLocStatus.get(c.getString(0)).equals("true"))	
			    			{
			    				status=true;
			    				
			    			}
			    			else
			    				status=false;
			    			
			    			listChildDataLocStatus.remove(c.getString(0));
			    			listChildDataLocStatus.put(c.getString(0), status?"false":"true"); */
			    			/* if(status)
			    					txtListChildImage.setImageResource(R.drawable.unchecked);
			    				else
			    					txtListChildImage.setImageResource(R.drawable.checked); */
			    		/*	if(childTextStatus)
			    				txtListChildCheck.setChecked(false);
			    				else
			    					txtListChildCheck.setChecked(true); */
			    	/*		 Thread mThread = new Thread() {
	                       	    @Override
	                       	    public void run() {
	                       	      
	                       	    	/*ReListFragment a=new ReListFragment((Activity) _context,_fm);
	                 				StartAsyncTaskInParallel(a);
	                	              
	                       	     Fragment rest_list_content = new RestListContent();
	                	         //  FragmentManager fragmentManager = getSupportFragmentManager();
	                	           _fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
	             	                	  
	                       	    }
	                       	    };
	                       	    mThread.start();
	                       	  
			    		_db.execSQL("UPDATE locations_list SET loc_status=\""+(! status)+"\" WHERE loc_id=\""+c.getString(0)+"\"");
			    		 }
			    		 c.close();
			    	}
			    	else
			    	{
			    		
			    		 final Cursor c=_db.rawQuery("SELECT cus_id FROM cuisines WHERE cus_name LIKE '"+childText+"'", null);
			    		 if(c.moveToFirst())
			    		 {
			    			 final Cursor c1=_db.rawQuery("SELECT cus_status FROM cuisines_list WHERE  cus_id=\""+c.getString(0)+"\"", null);
			    			 if(c1.moveToFirst())
			    			 {
			    				 if(c1.getString(0).equals("true"))
			    					 status=true;
			    			 	 else
			    			 		 status=false;
			    			 } 
			    			/* if(status)
			    					txtListChildImage.setImageResource(R.drawable.unchecked);
			    				else
			    					txtListChildImage.setImageResource(R.drawable.checked); 
			    			 c1.close();
			    		_db.execSQL("UPDATE cuisines_list SET cus_status=\""+(! status)+"\" WHERE cus_id=\""+c.getString(0)+"\"");
			    	*/
			    			/* System.out.println("Child Text"+ childText);
			    			 System.out.println("Child Text ID"+ c.getString(0));
			    			 System.out.println("Cuisine Status : "+listChildDataCusStatus.get(c.getString(0)));
			    			 if( listChildDataCusStatus.get(c.getString(0))!=null)
			    			 if( listChildDataCusStatus.get(c.getString(0)).equals("true"))	
				    			{
				    				status=true;
				    				
				    			}
				    			else
				    				status=false;
				    			
				    			listChildDataCusStatus.remove(c.getString(0));
				    			
				    			listChildDataCusStatus.put(c.getString(0), status?"false":"true"); */
				    			/* if(status)
				    					txtListChildImage.setImageResource(R.drawable.unchecked);
				    				else
				    					txtListChildImage.setImageResource(R.drawable.checked);
				    					*/
			 /*   		if(childTextStatus)
		    				txtListChildCheck.setChecked(false);
		    				else
		    					txtListChildCheck.setChecked(true); 
			    		
				    			 Thread mThread = new Thread() {
		                          	    @Override
		                          	    public void run() {
		                          	      
		                	             
		                          	    	/*ReListFragment a=new ReListFragment((Activity) _context,_fm);
		                     				StartAsyncTaskInParallel(a);
		                    	              
		                           	     Fragment rest_list_content = new RestListContent();
		                    	         //  FragmentManager fragmentManager = getSupportFragmentManager();
		                    	           _fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
		                	                	  
		                          	    }
		                          	    };
		                          	    mThread.start();
		                          		_db.execSQL("UPDATE cuisines_list SET cus_status=\""+(! status)+"\" WHERE cus_id=\""+c.getString(0)+"\"");
			    		 }
			    		 c.close();
			    		}
				
				
				_db.close();
				return false;
			}

			
			
		});*/
	convertView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 _db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
				boolean status = false;
				if(groupText.equals("Locations")) 
		    	{
		    		final Cursor c=_db.rawQuery("SELECT loc_id FROM locations WHERE loc_name LIKE '"+childText+"'", null);
		    		
		    		 if(c.moveToFirst())
		    		 {
		    			 final Cursor c1=_db.rawQuery("SELECT loc_status FROM locations_list WHERE  loc_id=\""+c.getString(0)+"\"", null);
		    			 if(c1.moveToFirst())
		    			 {
		    				 if(c1.getString(0).equals("true"))
		    					 status=true;
		    			 	 else
		    			 		 status=false;
		    			 }
		    			 
		    			 if(status)
		    					txtListChildImage.setImageResource(R.drawable.unchecked);
		    				else
		    					txtListChildImage.setImageResource(R.drawable.checked); 
		    			/* if(status)
		    					txtListChildImage.setImageResource(R.drawable.unchecked);
		    				else
		//    					txtListChildImage.setImageResource(R.drawable.checked); */
		//    			 c1.close();
		   		_db.execSQL("UPDATE locations_list SET loc_status=\""+(! status)+"\" WHERE loc_id=\""+c.getString(0)+"\"");
		    		
		    		/*	if( listChildDataLocStatus.get(c.getString(0)).equals("true"))	
		    			{
		    				status=true;
		    				
		    			}
		    			else
		    				status=false;
		    			
		    			listChildDataLocStatus.remove(c.getString(0));
		    			listChildDataLocStatus.put(c.getString(0), status?"false":"true"); */
		    			/* */
		    		/*	if(childTextStatus)
		    				txtListChildCheck.setChecked(false);
		    				else
		    					txtListChildCheck.setChecked(true); */
	//	    			 Thread mThread = new Thread() {
   //                    	    @Override
    //                   	    public void run() {
                       	      
                       	    	/*ReListFragment a=new ReListFragment((Activity) _context,_fm);
                 				StartAsyncTaskInParallel(a);
                	              */
 //                      	     Fragment rest_list_content = new RestListContent();
                	         //  FragmentManager fragmentManager = getSupportFragmentManager();
//                	           _fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
             	                	  
//                       	    }
//                       	    };
//                       	    mThread.start();
                       	  
	    		//_db.execSQL("UPDATE locations_list SET loc_status=\""+(! status)+"\" WHERE loc_id=\""+c.getString(0)+"\"");
		    		 }
		    	 c.close();
		    	}
		    	else
		    	{
		    		
		    		 final Cursor c=_db.rawQuery("SELECT cus_id FROM cuisines WHERE cus_name LIKE '"+childText+"'", null);
		    		 if(c.moveToFirst())
		    		 {
		    			 final Cursor c1=_db.rawQuery("SELECT cus_status FROM cuisines_list WHERE  cus_id=\""+c.getString(0)+"\"", null);
		    			 if(c1.moveToFirst())
		    			 {
		    				 if(c1.getString(0).equals("true"))
		    					 status=true;
		    			 	 else
		    			 		 status=false;
		    			 } 
	    			 if(status)
		    					txtListChildImage.setImageResource(R.drawable.unchecked);
		    				else
		    					txtListChildImage.setImageResource(R.drawable.checked); 
			 c1.close();
		    		_db.execSQL("UPDATE cuisines_list SET cus_status=\""+(! status)+"\" WHERE cus_id=\""+c.getString(0)+"\"");
		    		 }
		    		 c.close();
		    	}
				_db.close();
			}
	});	
		    			/* System.out.println("Child Text"+ childText);
		    			 System.out.println("Child Text ID"+ c.getString(0));
		    			 System.out.println("Cuisine Status : "+listChildDataCusStatus.get(c.getString(0)));
		    			 if( listChildDataCusStatus.get(c.getString(0))!=null)
		    			 if( listChildDataCusStatus.get(c.getString(0)).equals("true"))	
			    			{
			    				status=true;
			    				
			    			}
			    			else
			    				status=false;
			    			
			    			listChildDataCusStatus.remove(c.getString(0));
			    			
			    			listChildDataCusStatus.put(c.getString(0), status?"false":"true"); */
			    			/* if(status)
			    					txtListChildImage.setImageResource(R.drawable.unchecked);
			    				else
			    					txtListChildImage.setImageResource(R.drawable.checked);
			    					*/
		 /*   		if(childTextStatus)
	    				txtListChildCheck.setChecked(false);
	    				else
	    					txtListChildCheck.setChecked(true); */
		    		
		//	    			 Thread mThread = new Thread() {
	    //                      	    @Override
	     //                     	    public void run() {
	                          	      
	                	             
	                          	    	/*ReListFragment a=new ReListFragment((Activity) _context,_fm);
	                     				StartAsyncTaskInParallel(a);
	                    	              */
	     //                      	     Fragment rest_list_content = new RestListContent();
	                    	         //  FragmentManager fragmentManager = getSupportFragmentManager();
	    /*                	           _fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
	                	                	  
	                          	    }
	                          	    };
	                          	    mThread.start();
	                          		_db.execSQL("UPDATE cuisines_list SET cus_status=\""+(! status)+"\" WHERE cus_id=\""+c.getString(0)+"\"");
		    		 }
		    		 c.close();
		    		}
			
			
			_db.close();
			}});

	*/
		    			    	
		return convertView;
	}
	

	@Override
	public int getChildrenCount(int groupPosition) {
	
		if(_listDataChild==null ||_listDataHeader==null)
			 return 0;
		else	
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
			.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
	
		if(_listDataHeader!=null)
		return this._listDataHeader.get(groupPosition);
		else
			return null;
	}

	@Override
	public int getGroupCount() {
		
		if(_listDataHeader==null)
		return 2;
		else
			return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.alert_list_filter_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);
		View ind = convertView.findViewById( R.id.all);
		
		TextView indicator = (TextView)ind;
	indicator.setOnClickListener(new OnClickListener()
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 System.out.println(" All Clicked");
			if(groupPosition==1)
			{
			_db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
			 _db.execSQL("UPDATE locations_list SET loc_status=\""+ true+"\" ");

			 _db.close();
		
				/*listChildDataLocStatus.clear();
				_db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
				Cursor c1=_db.rawQuery("SELECT * FROM locations_list", null);
				while(c1.moveToNext())
				 {
					listChildDataLocStatus.put(c1.getString(0), "true");	
				 }
				 _db.execSQL("UPDATE locations_list SET loc_status=\""+ true+"\" ");
				c1.close(); 
				_db.close();*/
			 RestaurantList.expList.collapseGroup(1);
			 RestaurantList.expList.expandGroup(1);
			}
			else
			{
				 _db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
				_db.execSQL("UPDATE cuisines_list SET cus_status=\""+ true+"\"");
		    	_db.close();
		    	
				/* listChildDataCusStatus.clear();
				_db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
				Cursor c1=_db.rawQuery("SELECT * FROM cuisines_list", null);
				while(c1.moveToNext())
				 {
					
					
					listChildDataCusStatus.put(c1.getString(0), "true");	
					
				 }
				c1.close();
				_db.execSQL("UPDATE cuisines_list SET cus_status=\""+ true+"\"");
				 _db.close(); */
		    	 RestaurantList.expList.collapseGroup(0);
				 RestaurantList.expList.expandGroup(0);
			}
			/* Thread mThread = new Thread() {
           	    @Override
           	    public void run() {
           	      
 	             
           	    	/*ReListFragment a=new ReListFragment((Activity) _context,_fm);
     				StartAsyncTaskInParallel(a);
    	              */
           	  /*   Fragment rest_list_content = new RestListContent();
    	         //  FragmentManager fragmentManager = getSupportFragmentManager();
    	           _fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
 	                	  
           	    }
           	    };
           	    mThread.start();*/
		}
		
	});
	
	View ind1 = convertView.findViewById( R.id.none);
	
	TextView indicator1 = (TextView)ind1;
indicator1.setOnClickListener(new OnClickListener()
{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 System.out.println(" None Clicked");
		if(groupPosition ==1)
		{
		 _db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
		 _db.execSQL("UPDATE locations_list SET loc_status=\""+ false+"\" ");

		 _db.close();
		 
			/* listChildDataLocStatus.clear();
			_db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
			Cursor c1=_db.rawQuery("SELECT * FROM locations_list", null);
			while(c1.moveToNext())
			 {
				listChildDataLocStatus.put(c1.getString(0), "false");	
			 }
			c1.close();
			 _db.execSQL("UPDATE locations_list SET loc_status=\""+ false+"\" ");
			_db.close();
			*/
		 RestaurantList.expList.collapseGroup(1);
		 RestaurantList.expList.expandGroup(1);
		}
		else
		{
			_db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
			_db.execSQL("UPDATE cuisines_list SET cus_status=\""+ false+"\"");
	    	_db.close();
	    	/*
			listChildDataCusStatus.clear();
			_db=_context.openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
			Cursor c1=_db.rawQuery("SELECT * FROM cuisines_list", null);
			while(c1.moveToNext())
			 {
				
				
				listChildDataCusStatus.put(c1.getString(0), "false");	
				
			 }
			c1.close();
			_db.execSQL("UPDATE cuisines_list SET cus_status=\""+ false+"\"");
			 _db.close(); */
	    	 RestaurantList.expList.collapseGroup(0);
			 RestaurantList.expList.expandGroup(0);
		}
	/*	 Thread mThread = new Thread() {
       	    @Override
       	    public void run() {
       	      
	             
       	    	/*ReListFragment a=new ReListFragment((Activity) _context,_fm);
 				StartAsyncTaskInParallel(a);
	              */
       	  /*   Fragment rest_list_content = new RestListContent();
	         //  FragmentManager fragmentManager = getSupportFragmentManager();
	           _fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 

       	    }
       	    };
       	    mThread.start(); */
	}
	
});
	
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
}
	/* @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	
	 private void StartAsyncTaskInParallel(ReListFragment task) {
	     if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	         task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	     else
	         task.execute();
	 }
	@Override
	public boolean hasStableIds() {
	
		return false;
	}
	

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
	
		return true;
	}
	
	
	private class ReListFragment extends AsyncTask<Void, Void, Void> {
		
		 private Activity activity;
			FragmentManager fm;
		 ReListFragment(Activity activity, FragmentManager fm)
		 {
			 this.activity = activity;
			 this.fm=fm;
		//	 pd = new TransparentProgressDialog(activity, R.drawable.spinner);
			
		 }
		 @Override
	     protected void onPreExecute() {
	         super.onPreExecute();
	       //  pd= new ProgressDialog(activity);
	         //pd = MyCustomProgressDialog.ctor(activity);
	        // pd.setCancelable(false);
	      //   System.out.println("In Preexecute of search");
	        // pd.setMessage("Relisting ...");
	       //  pd.show();
	         
		 }
		@Override
		protected Void doInBackground(Void... params) {
			
		//	 pd.show();
			// RestListContent.populate(getActivity());
			 
			    Fragment rest_list_content = new RestListContent();
	         //  FragmentManager fragmentManager = getSupportFragmentManager();
	           fm.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
	       //    System.out.println("In execute of search");
			return null;
		}
		 @Override
	     protected void onPostExecute(Void result) {
	       
	         super.onPostExecute(result);
	      //   pd.dismiss();
	       //  textView.setVisibility(View.VISIBLE);
	  		//searchImg.setVisibility(View.VISIBLE);
	  	//	RestListContent.listview.setVisibility(View.VISIBLE);
	  	//	RestListContent.tv.setVisibility(View.GONE);
	       //  System.out.println("In Postexecute of search");
//	         searchImg.setVisibility(View.VISIBLE);
	     }
	}

}*/
