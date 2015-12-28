package com.example.ramana.munchzone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RestaurantList extends Activity implements TextWatcher  {
	private SQLiteDatabase db;
	DrawerLayout dLayout;
	public static ExpandableListView expList;	
	 public static AutoCompleteTextView acTextView;
	 static ImageView searchImg;
	 static ImageView img;
	
	 static ImageButton textView;
	 static TextView ht;
	 int key= 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_rest_list);
		img=(ImageView)findViewById(R.id.search);
			searchImg=(ImageView) findViewById(R.id.searchList);
			textView =(ImageButton) findViewById(R.id.advSearch);
			ht=(TextView) findViewById(R.id.header_text);
			ht.setVisibility(View.VISIBLE);
			ht.setText("Restaurant List");
			TextView b=(TextView) findViewById(R.id.back);
			b.setVisibility(View.GONE);
			TextView n=(TextView) findViewById(R.id.next);
			n.setVisibility(View.GONE);

			searchImg.setVisibility(View.VISIBLE);

			searchImg.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					ht.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);
					searchImg.setVisibility(View.GONE);
					acTextView.setVisibility(View.VISIBLE);
					acTextView.setFocusable(true);
					acTextView.setFocusableInTouchMode(true);
					acTextView.requestFocus();
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(acTextView, InputMethodManager.SHOW_IMPLICIT);

							img.setVisibility(View.VISIBLE);

				}
			});
		android.app.Fragment rest_list_content = new RestListContent();
			android.app.FragmentManager fragmentManager = getFragmentManager();
	  fragmentManager.beginTransaction().replace(R.id.main_content, rest_list_content).commit();

		Cursor c;
			dLayout = (DrawerLayout) findViewById(R.id.right_drawer_layout);
			expList = (ExpandableListView) findViewById(R.id.rightLvExp);
			//dLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			ActionBarDrawerToggle mDrawerToggle;
			mDrawerToggle = new ActionBarDrawerToggle(RestaurantList.this, dLayout,
					R.drawable.ic_launcher, R.string.drawer_open, R.string.drawer_close) {

				/* Called when drawer is closed */
				public void onDrawerClosed(View view) {
					//Put your code here
					//	 System.out.println("In Drawer Closing");
					key=0;
					textView.setVisibility(View.INVISIBLE);
					searchImg.setVisibility(View.INVISIBLE);
					RestListContent.listview.setVisibility(View.GONE);
					RestListContent.tv.setVisibility(View.VISIBLE);
					new ReListFragment(RestaurantList.this).execute();
        	         	/*	  db=getActivity().openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
        	         		  Cursor c=db.rawQuery("SELECT * FROM cuisines_list", null);
        	         		 while(c.moveToNext())
        	         		 {
        	         			if((ExpandableListAdapter.listChildDataCusStatus.get(c.getString(0))).equals("true"))

        	         			db.execSQL("UPDATE cuisines_list SET cus_status=\""+ true+"\" WHERE cus_id=\""+c.getString(0)+"\"");
        	         			else
        	         				db.execSQL("UPDATE cuisines_list SET cus_status=\""+ false+"\" WHERE cus_id=\""+c.getString(0)+"\"");

        	         		 }
        	         		 c.close();
        	         		 c=db.rawQuery("SELECT * FROM locations_list", null);
       	         		 while(c.moveToNext())
       	         		 {
       	         			if((ExpandableListAdapter.listChildDataLocStatus.get(c.getString(0))).equals("true"))

       	         			db.execSQL("UPDATE locations_list SET loc_status=\""+ true+"\" WHERE loc_id=\""+c.getString(0)+"\"");
       	         			else
       	         				db.execSQL("UPDATE locations_list SET loc_status=\""+ false+"\" WHERE loc_id=\""+c.getString(0)+"\"");

       	         		 }
       	         		 c.close();
        	         		  */
					//		 RestListContent.populate(getActivity());

        	         /*		 Fragment rest_list_content = new RestListContent();
        	                   FragmentManager fragmentManager = getFragmentManager();
        	                   fragmentManager.beginTransaction().replace(R.id.main_content, rest_list_content).commit();
        	          */
					// ReListFragment m= (ReListFragment)
				}

				/* Called when a drawer is opened */
				public void onDrawerOpened(View drawerView) {
					//Put your code here
					// System.out.println("on opening");

				}
			};
			dLayout.setDrawerListener(mDrawerToggle);

			textView.setVisibility(View.VISIBLE);


			ArrayList<String> Cuisines= new ArrayList<String>();

			ArrayList<String> listDataHeader = new ArrayList<String>();
			HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
			db=openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
			c=db.rawQuery("SELECT DISTINCT cuisines.cus_id,cuisines.cus_name  FROM cuisines_list LEFT JOIN cuisines ON cuisines_list.cus_id=cuisines.cus_id", null);

			while(c.moveToNext())
			{

				Cuisines.add(c.getString(1));

			}

			c.close();
			ArrayList<String>  Locations = new ArrayList<String>();
			c=db.rawQuery("SELECT DISTINCT locations.loc_id,locations.loc_name  FROM locations_list LEFT JOIN locations ON locations_list.loc_id=locations.loc_id", null);
			while(c.moveToNext())
			{
				Locations.add(c.getString(1));

			}c.close();
			db.close();
			//
			listDataHeader.add("Cuisines");
			listDataHeader.add("Locations");
			listDataChild.put(listDataHeader.get(0), Cuisines);
			listDataChild.put(listDataHeader.get(1), Locations);
			final ExpandableListAdapter listAdapter= new ExpandableListAdapter(RestaurantList.this, listDataHeader, listDataChild/*,db*/,getFragmentManager());
			expList.setAdapter(listAdapter);
			expList.expandGroup(0);
//	popup.



			textView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//	System.out.println("Clicked textview");
					if(key==0){
						key=1;
						//     System.out.println("Opening");

						dLayout.openDrawer(expList);


					}
					else if(key==1){

						dLayout.closeDrawer(expList);
						//   System.out.println("Closing");
						key=0;

                   /*Fragment rest_list_content = new RestListContent();
                   FragmentManager fragmentManager = getFragmentManager();
                   fragmentManager.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); */
					}




				} });
			db.close();

			acTextView=(AutoCompleteTextView)findViewById(R.id.AClistview);

			acTextView.clearFocus();
			int listsCount =-1;

			db=openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
			Cursor cusines=db.rawQuery("SELECT * FROM cuisines", null);
			Cursor locations=db.rawQuery("SELECT DISTINCT rest_location_id FROM restaurants", null);
			c=db.rawQuery("SELECT DISTINCT rest_id,rest_name,rest_location_id FROM restaurants", null);

			String[] lists=new String[(c.getCount())+  cusines.getCount()+locations.getCount()];

			while(cusines.moveToNext())
			{
				listsCount++;
				lists[listsCount]=cusines.getString(1);
			}cusines.close();
			cusines=null;
			while(locations.moveToNext())
			{
				Cursor s=db.rawQuery("SELECT loc_name FROM locations where loc_id='"+locations.getString(0)+"'", null);
				if(s.moveToFirst())
				{
					listsCount++;
					lists[listsCount]=s.getString(0);
				}
				s.close();
			}locations.close();

			while(c.moveToNext())
			{
				listsCount++;
				lists[listsCount]=c.getString(1);
			}
			c.close();
			acTextView.setAdapter(new ArrayAdapter<String>(RestaurantList.this, android.R.layout.simple_dropdown_item_1line, lists));
			acTextView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {



					//img.setBackgroundResource(R.drawable.cancel);
					v.setFocusable(true);
					v.setFocusableInTouchMode(true);
					return false;
				}
			});

			img.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {

					acTextView.setHint("Search by Restaurant, Locations and Cuisines");

					acTextView.setFocusable(true);
					acTextView.setFocusableInTouchMode(true);
					ht.setVisibility(View.VISIBLE);
					textView.setVisibility(View.VISIBLE);
					searchImg.setVisibility(View.VISIBLE);
					acTextView.setVisibility(View.GONE);

					img.setVisibility(View.GONE);//getActivity().getCurrentFocus().getWindowToken()
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(acTextView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					Fragment rest_list_content = new RestListContent();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction().replace(R.id.main_content, rest_list_content).commit();

				}

			})
			;
/*if(acTextView.hasFocus())
	  img.setBackgroundResource(R.drawable.cancel);
else
	  img.setBackgroundResource(R.drawable.search);
*/
			ItemAutoTextAdapter adapter = new ItemAutoTextAdapter();
			acTextView.setOnItemClickListener(adapter);

			acTextView.addTextChangedListener(new TextWatcher()
			{

				public void onTextChanged(CharSequence s, int start, int before, int count)
				{

				}

				public void beforeTextChanged(CharSequence s, int start, int count,
											  int after)
				{
				}

				public void afterTextChanged(Editable s)
				{

				}

			});
			acTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEARCH) {
						RestListContent.listview.setVisibility(View.GONE);

						InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

						if(acTextView.getText().length()==0 )

						{


							Fragment rest_list_content = new RestListContent();
							FragmentManager fragmentManager = getFragmentManager();
							fragmentManager.beginTransaction().replace(R.id.main_content, rest_list_content).commit();


						}
						else
						{
							acTextView.dismissDropDown();
							System.out.println("In else part");
							RestListContent.listview.setVisibility(View.GONE);
							RestListContent.tv.setVisibility(View.VISIBLE);
							new ReListSearchFragment(RestaurantList.this).execute();

						}

						return true;
					}
					return false;
				}


			});



			acTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {

						acTextView.setHint("");
						acTextView.setFocusable(true);
						acTextView.setFocusableInTouchMode(true);
						ht.setVisibility(View.VISIBLE);
						textView.setVisibility(View.VISIBLE);
						searchImg.setVisibility(View.VISIBLE);
						acTextView.setVisibility(View.GONE);
						img.setVisibility(View.GONE);
						RestListContent.listview.setEnabled(true);

					}
				}
			});


	}



	class ItemAutoTextAdapter extends CursorAdapter
	    implements OnItemClickListener {

			
	public ItemAutoTextAdapter(Context context, Cursor c) {
				super(context, c);
				// TODO Auto-generated constructor stub
			}



	public ItemAutoTextAdapter() {
		super(RestaurantList.this, null);
		
		// TODO Auto-generated constructor stub
	}
	/**
	 * Called by the AutoCompleteTextView field when a choice has been made
	 * by the user.
	 * 
	 * @param listView
	 *            The ListView containing the choices that were displayed to
	 *            the user.
	 * @param view
	 *            The field representing the selected choice
	 * @param position
	 *            The position of the choice within the list (0-based)
	 * @param id
	 *            The id of the row that was chosen (as provided by the _id
	 *            column in the cursor.)
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
	  
		 acTextView.setFocusable(false);
	   acTextView.setFocusableInTouchMode(false); 
		
		Cursor t,s = null;
		 
		  
		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		   acTextView.clearFocus(); 
		   db=openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
		 Cursor c=db.rawQuery("SELECT * FROM restaurants where rest_name LIKE '"+acTextView.getText()+"'", null);
		 if(c.getCount()==0)
		 {
			 t=db.rawQuery("SELECT * FROM locations where loc_name LIKE '"+acTextView.getText()+"'", null);
			 if(t.moveToNext())
			 {
				 c=db.rawQuery("SELECT * FROM restaurants where rest_location_id LIKE '"+t.getString(0)+"'", null);
			 }
			 t.close();
		 }
		 if(c.getCount()==0)
		 {
			 t=db.rawQuery("SELECT * FROM cuisines where cus_name LIKE '"+acTextView.getText()+"'", null);
			 if(t.moveToNext())
			 {
			
				 s=db.rawQuery("SELECT * FROM rest_cuisine where cus_id LIKE '"+t.getString(0)+"'", null);
			 }
			 String Query="SELECT * FROM restaurants where "; 
			 while(s.moveToNext())
				 Query+="rest_id LIKE'"+s.getString(0)+ "' OR ";
			 Query+="rest_id LIKE 'Nil'";
			 t.close();
			 s.close();
			
			 c=db.rawQuery(Query, null);
			 
			
		 }
		
		 if(c.getCount()==0)
		 {
			 AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantList.this);
             builder.setTitle(" Search Results ");
             builder.setMessage("No result found")  
                    .setCancelable(false)
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });                     
             AlertDialog alert = builder.create();
             alert.show(); 
             searchImg.setVisibility(View.GONE);
		 }
		 PostRestData[] listData1 = new PostRestData[c.getCount()];
	
		PostRestData data;
		 int i=-1;
		
		 while(c.moveToNext())
			{
			 i++;
			 data = new PostRestData();
			 data.rest_id = c.getString(0);
			    data.rest_name = c.getString(1);
			  
			    Cursor t1=db.rawQuery("SELECT loc_name FROM locations where loc_id=\""+c.getString(2)+"\"", null);
              while(t1.moveToNext())
              	 data.rest_location = t1.getString(0);
              t1.close();   
              Cursor c1=db.rawQuery("SELECT cus_id FROM rest_cuisine where rest_id=\""+data.rest_id+"\"", null);
              String Cuisines = "";
              while(c1.moveToNext())
              {
             	 if(! c1.getString(0).equals("Nill"))
             	 {
             	  t=db.rawQuery("SELECT cus_name FROM cuisines where cus_id=\""+c1.getString(0)+"\"", null);
             	 
             	  while(t.moveToNext())
             	  {
             		  Cuisines+=(t.getString(0))+", ";
             	  }
             	  t.close();
              }
              } c1.close();
              if(Cuisines.length()>2)
              	Cuisines=Cuisines.substring(0, Cuisines.length()-2);
              	else
              		Cuisines="";
              data.rest_cuisines = Cuisines+"\b";
              data.rest_timing = "Under Development";
	      listData1[i]=data;
	                                            
			}
		 c.close();
         db.close();

		 PostItemAdapter itemAdapter1 = new PostItemAdapter(RestaurantList.this,R.layout.list_item, listData1);
	   
		 RestListContent.listview.setAdapter(itemAdapter1);
		 acTextView.setVisibility(View.VISIBLE);
		 img.setVisibility(View.VISIBLE);
		 textView.setVisibility(View.GONE);
		 ht.setVisibility(View.GONE);
		 searchImg.setVisibility(View.GONE);
	  
	     
	}

	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	}  


   
   public static void call()
   {
   	 acTextView.setHint("Search by Restaurant, Cuisines and Locations");
   	   acTextView.setFocusable(true);
   	   acTextView.setFocusableInTouchMode(true);
   		ht.setVisibility(View.GONE);
   		textView.setVisibility(View.GONE);
   		searchImg.setVisibility(View.GONE);
   		acTextView.setVisibility(View.VISIBLE);
   		img.setVisibility(View.VISIBLE);
   		RestListContent.listview.setEnabled(true);
   
   }

@Override
public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	
	
}



@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	
}



@Override
public void afterTextChanged(Editable s) {
	// TODO Auto-generated method stub
	
}
  
private class ReListFragment extends AsyncTask<Void, Void, Void> {
	
	 private Activity activity;
	 ProgressDialog pd1;
	 private TransparentProgressDialog pd;
	 ReListFragment(Activity activity)
	 {
		 this.activity = activity;
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
           FragmentManager fragmentManager = getFragmentManager();
           fragmentManager.beginTransaction().replace(R.id.main_content, rest_list_content).commit(); 
       //    System.out.println("In execute of search");
		return null;
	}
	 @Override
     protected void onPostExecute(Void result) {
       
         super.onPostExecute(result);
      //   pd.dismiss();
         textView.setVisibility(View.VISIBLE);
  		searchImg.setVisibility(View.VISIBLE);
  		RestListContent.listview.setVisibility(View.VISIBLE);
  		RestListContent.tv.setVisibility(View.GONE);
       //  System.out.println("In Postexecute of search");
//         searchImg.setVisibility(View.VISIBLE);
     }
}

private class ReListSearchFragment extends AsyncTask<Void, Void, Void> {
	
	 private Activity activity;
	 ProgressDialog pd1;
	 private TransparentProgressDialog pd;
	 ReListSearchFragment(Activity activity)
	 {
		 this.activity = activity;
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
		
		//boolean f=relistsearch();
		
		System.out.println("AutoComplete Text: "+ acTextView.getText());
		 db=openOrCreateDatabase("MunchZone", Context.MODE_PRIVATE, null);
		Cursor  temp=db.rawQuery("SELECT DISTINCT restaurants.rest_id,restaurants.rest_name,restaurants.rest_location_id FROM restaurants LEFT JOIN rest_cuisine LEFT JOIN locations LEFT JOIN cuisines ON restaurants.rest_id=rest_cuisine.rest_id AND restaurants.rest_location_id=locations.loc_id AND rest_cuisine.cus_id=cuisines.cus_id  WHERE restaurants.rest_name LIKE '%" + acTextView.getText() +"%' OR cuisines.cus_name LIKE '%" + acTextView.getText() +"%' OR locations.loc_name LIKE '%" + acTextView.getText() +"%'", null);
		
		System.out.println("Data Base info "+ temp.getCount());
		 if(temp.getCount()==0)
		 {
			 AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantList.this);
	        builder.setTitle(" Search Results ");
	        builder.setMessage("No result found")  
	               .setCancelable(false)
	               .setPositiveButton("OK",new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                   }
	               });                     
	        AlertDialog alert = builder.create();
	        alert.show(); 
	        searchImg.setVisibility(View.GONE);
		 }
		 PostRestData[] listData1 = new PostRestData[temp.getCount()];

		PostRestData data;
		 int i=-1;
		
		 while(temp.moveToNext())
			{
			 i++;
			 data = new PostRestData();
			 data.rest_id = temp.getString(0);
			    data.rest_name = temp.getString(1);
			  
			    Cursor t1=db.rawQuery("SELECT loc_name FROM locations where loc_id=\""+temp.getString(2)+"\"", null);
	         while(t1.moveToNext())
	         	 data.rest_location = t1.getString(0);
	         t1.close();   
	         Cursor c1=db.rawQuery("SELECT cus_id FROM rest_cuisine where rest_id=\""+data.rest_id+"\"", null);
	         String Cuisines = "";
	         Cursor t;
	         while(c1.moveToNext())
	         {
	        	 if(! c1.getString(0).equals("Nill"))
	        	 {
	        	  t=db.rawQuery("SELECT cus_name FROM cuisines where cus_id=\""+c1.getString(0)+"\"", null);
	        	 
	        	  while(t.moveToNext())
	        	  {
	        		  Cuisines+=(t.getString(0))+", ";
	        	  }
	        	  t.close();
	         }
	         } c1.close();
	         if(Cuisines.length()>2)
	         	Cuisines=Cuisines.substring(0, Cuisines.length()-2);
	         	else
	         		Cuisines="";
	         data.rest_cuisines = Cuisines+"\b";
	         data.rest_timing = "Under Development";
	     listData1[i]=data;
	                                           
			}
		 temp.close();
	    db.close();

		 final PostItemAdapter itemAdapter1 = new PostItemAdapter(RestaurantList.this,R.layout.list_item, listData1);
	 try
	 {
		  activity.runOnUiThread(new Runnable() {
			     @Override
			     public void run() {
			    	 RestListContent.listview.setAdapter(itemAdapter1);

			    }
			});
		              
	  }
	  catch(Exception e)
	  {
		//   System.out.println(e);
	  }
		return null;
	}
	 @Override
    protected void onPostExecute(Void result) {
      
        super.onPostExecute(result);
     //   pd.dismiss();
        acTextView.setVisibility(View.VISIBLE);
		
		 img.setVisibility(View.VISIBLE);
		 textView.setVisibility(View.GONE);
		 ht.setVisibility(View.GONE);
		 searchImg.setVisibility(View.GONE);
 		RestListContent.listview.setVisibility(View.VISIBLE);
 		RestListContent.tv.setVisibility(View.GONE);
      //  System.out.println("In Postexecute of search");
//        searchImg.setVisibility(View.VISIBLE);
    }
}
 
public boolean relistsearch()
{
	
return false;

}
private class TransparentProgressDialog extends Dialog {
	
	private ImageView iv;
	
	public TransparentProgressDialog(Context context, int resourceIdOfImage) {
		super(context, R.style.TransparentProgressDialog);
    	WindowManager.LayoutParams wlmp = getWindow().getAttributes();
    	wlmp.gravity = Gravity.CENTER_HORIZONTAL;
    	getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		iv = new ImageView(context);
		iv.setImageResource(resourceIdOfImage);
		layout.addView(iv, params);
		addContentView(layout, params);
	}
	
	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f , Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}
}

}

