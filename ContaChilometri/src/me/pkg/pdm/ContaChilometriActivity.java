package me.pkg.pdm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ContaChilometriActivity extends Activity {
	
    /** Called when the activity is first created. */

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
                 
        Button startButton = (Button)findViewById(R.id.button1);
        
        startButton.setOnClickListener(new OnClickListener() {
        	
        	//creo e dichiaro le editText username e password
        	final EditText text1 = (EditText)findViewById(R.id.editText1);  //user
        	final EditText text2 = (EditText)findViewById(R.id.editText2);  //psw
        
        	
        	public void onClick(View v) {
        		
        		
        		//passo i parametri username e password a Maps con un intent
        		Intent intent1 = new Intent(ContaChilometriActivity.this,Maps.class);
        		
        		String iltesto1 = text1.getText().toString();
        		String iltesto2 = text2.getText().toString();

            	intent1.putExtra("iltestonelbox1",iltesto1);
            	intent1.putExtra("iltestonelbox2",iltesto2);
            	
            	startActivity(intent1);     // passo alla seconda activity Maps
        		

        	}
        	
        	
        });
         
    }
 
   
}