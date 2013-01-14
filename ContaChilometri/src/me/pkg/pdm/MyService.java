package me.pkg.pdm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

public class MyService extends Service 
{

		boolean first = true;
		double latB;
		double lngB;
        double distance;  
      //boolean v = true; //variabile per stop service
        Chronometer mChronometer;
        private Handler uiHandler;
        private  final IBinder mBinder = new LocalBinder();
        boolean expired = false;
        
     
   	 public NotificationManager mNotificationManager; //dichiarazioni variabili notifica
   	 public Notification notification;
   	 
   	 public LocationManager locationManager;
	 public MyLocationListener locationListener;
   

   	 public class LocalBinder extends Binder {
   		 MyService getService(){
   			 return MyService.this;
   		 }
   	 }
   	 
   	 
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public void onCreate(){
		 
		distance=0;
         
	}
	
	@Override
	public void onStart(Intent intent, int startid)  {
		Toast.makeText(this, "Inizia il gioco ", Toast.LENGTH_LONG).show();
		
		
				//Log.i("serviced","service started");
				locationListener = new MyLocationListener();
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
				
				
				
		                    
		            MyCount m = new MyCount(20100,20100,mHandler);      //parte il timer in background
		            m.start();
		     
		      

	}
	
	public void registerUIHandler(Handler ui)
	{
		uiHandler = ui;
	}
	
	//handler che gestisce l'invio della notifica
	private Handler mHandler = new Handler() {

    	@Override

    	public void handleMessage(Message msg) {

    	super.handleMessage(msg);
    	
    	
    	
    	sendNotification();
    	
    	
    	expired = true;
    	
    	
    	 
    	 //finisce il count, parte l'handler, lancia la notifica

    	 
    	 
    	
    	//mChronometer.stop();   //cronometro si ferma quando arriva la notifica (SBAGLIATO)
    	
    	
    	
    	     
    	}
    	
    	
    };
	public void showmessaggio(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	public void sendNotification()
	{
		//notifica
       String ns = Context.NOTIFICATION_SERVICE;
       mNotificationManager = (NotificationManager) getSystemService(ns);
       int icon = R.drawable.ic_launcher;
       CharSequence tickerText = "KM PERCORSI";
       long when = System.currentTimeMillis();

       notification = new Notification(icon, tickerText, when);
       
       Context context = getApplicationContext();
       CharSequence contentTitle = "notifica Conta Chilometri";
       CharSequence contentText = "Hai percorso "+distance +"  km";
       Intent notificationIntent = new Intent(this, Maps.class);
       notificationIntent.putExtra("value", distance);
       PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

       notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
       
       notification.defaults |=Notification.DEFAULT_VIBRATE;//vibrazione
       
       mNotificationManager.notify(1, notification);
       
       }
	
	
	private final class MyLocationListener implements LocationListener {
		

	    @Override
	    public void onLocationChanged(Location locFromGps) {
	    	
	    	if(!expired)
	    	{
	        // chiamato quando il listener è notificato con un aggiornamento della location dal GPS
	    	Log.i("service",locFromGps.toString());
	    
	       
	        
	        Location locationA = new Location("point A");  
	          
	        //latA= locFromGps.getLatitude();
	        //lngA= locFromGps.getLongitude();
	        
	        locationA.setLatitude(locFromGps.getLatitude());  
	        locationA.setLongitude(locFromGps.getLongitude());  
	        
	        
	          
	        Location locationB = new Location("point B");  
	      
	          
	        locationB.setLatitude(latB);  
	        locationB.setLongitude(lngB);  
	          
	        if(first == false)
	        	distance += locationA.distanceTo(locationB);  
	       

			latB= locationA.getLatitude();
			lngB= locationA.getLongitude();
			
			 int i = (int)distance;

	        
			 showmessaggio(i+"");
	        
          
	        first = false;
	    	}
	    	else
	    	{
	    		Log.i("mio","gioco finito "+distance);
	    		((me.pkg.pdm.Maps)getApplicationContext()).sendMsg("DST"+distance);
	    	}
	       
	        
	    }

	    @Override
	    public void onProviderDisabled(String provider) {
	       // called when the GPS provider is turned off (user turning off the GPS on the phone)
	    }

	    @Override
	    public void onProviderEnabled(String provider) {
	       // called when the GPS provider is turned on (user turning on the GPS on the phone)
	    }

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	       // called when the status of the GPS provider changes
	    }
	    
	    
	    
	    
	    
	    
	}
	
	@Override
    public void onDestroy(){
  
   	super.onDestroy();
   	
   	Log.i("destroy", "destroy");
   	
	
   }
	
	
}