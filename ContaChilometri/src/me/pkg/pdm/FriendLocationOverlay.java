package me.pkg.pdm;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class FriendLocationOverlay extends ItemizedOverlay<OverlayItem>  {
	
	
	private Context context;
	private Chronometer mChronometer;
	
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	

		
	public FriendLocationOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		//super(defaultMarker);
	}
	

	public FriendLocationOverlay(Drawable defaultMarker, Context context, Chronometer mChronometer){
	
		//super(context, mapView);
		this(defaultMarker);
		this.context = context;
		
		this.mChronometer = mChronometer;
	}
	
	
      // getter e setter di mChronometer
	public Chronometer getmChronometer() {
		return mChronometer;
	}
	

	public void setmChronometer(Chronometer mChronometer) {
		this.mChronometer = mChronometer;
	}
	

	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
	

	public int size() {
		return mOverlays.size();
	}
	
	
	// gestisce il tap sull overlay dell amico
	 @Override
	 protected boolean onTap(int index) {
		 super.onTap(index);
		 
		 /*
	       OverlayItem item = (OverlayItem) mOverlays.get(index);
	       AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	       dialog.setTitle(item.getTitle());
	       dialog.setMessage(item.getSnippet());
	       dialog.show();
	       */
	       
	       
	        //apre un menu
			
		 final CharSequence[] choices = {"Gioca", "Messaggia"};
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
		 builder.setTitle("menu");
		 
		 builder.setItems(choices, new DialogInterface.OnClickListener() {
				
			 public void onClick(DialogInterface dialog, int item) {
			    	Intent intent;
			    	
			    	switch (item) {
			    	
			    	
			    //GIOCO
			    	case 0: 
			    		
			    		
			    		context.startService(new Intent(context, MyService.class)); //parte il service per il joiner
			    		
						mChronometer.setBase(SystemClock.elapsedRealtime()); //resetto il cronometro
						mChronometer.start(); //parte il cronometro per il joiner
						mChronometer.setVisibility(View.VISIBLE);
			            ((me.pkg.pdm.Maps)context).sendMsg("PLYgioco"); //parte service e cronometro per chi hosta
			            
			            break;
			    		
			    		
			    		
			    		
			    //MESSAGGIO
					case 1: 
						
						Dialog customDialog = new Dialog(context);
						customDialog.setTitle("chat");
				        customDialog.setContentView(R.layout.chat);
				        customDialog.show();
				        	
				        
				        final EditText et = (EditText) customDialog.findViewById(R.id.editText1);
				        final TextView tv = (TextView) customDialog.findViewById(R.id.textView1);

				        tv.setMovementMethod(new ScrollingMovementMethod());

				        Button startButton = (Button) customDialog.findViewById(R.id.button1);
				   		    
				   		    
				   		   
				        startButton.setOnClickListener(new OnClickListener() {

				        	public void onClick(View v) {

				        		String text = "MSG:"+et.getText().toString();			   				
				        		tv.append("ME: " + text + "\n");
				   		    		
				   		    		
				        		((me.pkg.pdm.Maps)context).sendMsg(text);
				        	}
				        });
				   		
				   		
				        break;
						
						
				//DEFAULT
					default:
						break;
			    	}
			 }
		 });
		
		 builder.create().show();

		 return true;
	 }

}
	
	
