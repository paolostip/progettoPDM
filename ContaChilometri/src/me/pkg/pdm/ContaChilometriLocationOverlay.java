package me.pkg.pdm;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;






/*
 * PROVA DI LOCATIONOVERLAY
 * 
 * DA ELIMINARE!!!!
 */








public class ContaChilometriLocationOverlay extends MyLocationOverlay {
	

	//private Context context1;
	

	private Activity context;
	public ContaChilometriLocationOverlay(Activity context, MapView mapView,
										  Chronometer mCh) {
		super(context, mapView);
		this.context = context;
		mChronometer = mCh;
		// TODO Auto-generated constructor stub
		
		
		//gestisce evento di click sul marker
	}

	 Chronometer mChronometer;

	/*@Override
    protected boolean dispatchTap(){
		
		
		//apre un menu
		
        
		final CharSequence[] choices = {"Gioca", "Messaggio"};
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("menu");
		builder.setItems(choices, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	Intent intent;
		    	switch (item) {
		    	case 0:
		    		
		    		context.startService(new Intent(context, MyService.class));

		    		
		    		
		            mChronometer.setBase(SystemClock.elapsedRealtime());//resetta il cronometro
                    mChronometer.start();                               //parte il cronometro
                    
		            //MyCount m = new MyCount(30000,30000,mHandler);      //parte il timer in background
		            //m.start();
		          
		    		break;
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
			   				//tv.append("ME: " + et.getText().toString() + "\n");
			   				tv.append("ME: " + text + "\n");
			   				((me.pkg.pdm.Maps)context).sendMsg(text);
			   			
			   				
			   			}
			   		});
			   		
			   		
			   

					break;
				
				default:
					break;
				}
		    }
		});
	
		builder.create().show();
		return true;

	}
	 */
}


