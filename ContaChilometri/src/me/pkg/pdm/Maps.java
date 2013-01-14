package me.pkg.pdm;

import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class Maps extends MapActivity {
	
	public Notification notification1;//notifica pulmino
	public NotificationManager mNotificationManager;
	
	private Context context;
	private MyLocationOverlay myLocationOverlay;
	private MapView mapView; 
	public static final String PREFS = "MyPrefs"; // preference per il salvataggio dei parametri per la connessione
	private TextView tvLatitudine;
    private TextView tvLongitudine;
    static final int COMANDO1 = 0;
    private double val = 0;
    MapController mapController;
    
    LocationManager locationManager;
    
	Chronometer mChronometer;
	 
	 
	 private String amico="";
	 private String friend=null;
	 private String parameter1 = null;
	 private String parameter2 = null;
	   
	 private String lon;
	 private String lat;
	 
	 Connection connection;
	 
	 
	 
	 //Metodo per gestione toast
	public void showmessage(String body) {
    	Toast.makeText(Maps.this, body, Toast.LENGTH_LONG).show();
    	
	}
	
	
	
	 //salva i parametri per la connessioni
	public void saveState(){
		SharedPreferences settings = this.getSharedPreferences(PREFS, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("USR", parameter1);
		editor.putString("PWD", parameter2);
		editor.commit();
	}
	
	

	 //salva i parametri per l'amico
		public void saveStateFriend(){
			SharedPreferences settings = this.getSharedPreferences(PREFS, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("FRD", amico);
			editor.commit();
			
			Log.i("stato", "salva");
		}

	
	
	//protocollo di messaggistica
	public Handler handler = new Handler(){ 
		
		@Override
	  public void handleMessage(android.os.Message msg){ 
			
			//Log.i("paolo","receveid:"+(String) msg.obj);
			
			String raw = (String)msg.obj;
			
			if(raw.length()<5) {
				Log.i("paolo","Messaggio troppo corto");
			}
			
			String tag = raw.substring(0, 3); 				//prime 3 lettere del msg sono il TAG
			String body = raw.substring(4, raw.length());	//succesive lettere del msg sono il BODY
			//Log.i("paolo","tag: "+tag+" body:"+body);
			
			
			//MESSAGGIO
			if(tag.equals("MSG")) {
				showmessage(body);
			}	
			
			
			//DISTANZA                    
			if(tag.equals("DST"))	
			{
				Log.i("mio", "amico ha percorso" + body + " io percorso"+ val);
				showmessage("Il tuo amico ha percorso "+body+"  m");
			}
			
			
			//PLAY
			if(tag.equals("PLY")) {
				context.startService(new Intent(context, MyService.class));
				mChronometer.setBase(SystemClock.elapsedRealtime()); //resetto il cronometro
	            mChronometer.start(); //parte il cronometro
	            mChronometer.setVisibility(View.VISIBLE);
	            Log.i("chronometer visible","chronometer visible");	
			}
			
			
			//STOP GIOCO						//NON FUNZIONA
			if(tag.equals("STP")) {
	            mChronometer.stop(); //stop cronometro
	            
	            Log.i("stopservice", "stopservice");
				context.stopService(new Intent(context, MyService.class));
			}
			
			
			//POSIZIONE
			if(tag.equals("POS")) {
				String split [] = body.split(";");
				lat = split[0];
				lon = split[1];
				
				 Drawable drawable = getResources().getDrawable(R.drawable.yellow2);
			     
			     FriendLocationOverlay itemizedoverlay = new FriendLocationOverlay(drawable, context,mChronometer);
			       
			     mapView.invalidate();

			        
			     double lat2 = Double.parseDouble(lat);
			     double lon2 =Double.parseDouble(lon);
					
			     int lat3= (int)lat2;
			     int lon3 = (int)lon2;
					
			     List<Overlay> overlays = mapView.getOverlays();
			        
					
			     GeoPoint point = new GeoPoint(lat3,lon3);
			     OverlayItem overlayitem = new OverlayItem(point, "ao", "ciao");
			        
			     itemizedoverlay.addOverlay(overlayitem);
			     overlays.add(itemizedoverlay);
			        
			     //overlays.add(new OverlayItem(new GeoPoint(lat3,lon3),"p","n"));
			      
				//Log.i("paolo","Posizione di "+amico+" "+lat+" "+lon);
			}
	       
	  } 
	}; 
	
	
	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        
        
        setContentView(R.layout.maps);
        Log.i("mio", "ONCreate");
        context = this;
        
       //creiamo un cronometro
        mChronometer = (Chronometer) findViewById(R.id.chronometer1);
        
        mChronometer.start();
        
        mChronometer.setVisibility(View.GONE);
        
        //creiamo in alto alla mappa i campi Latitudine e Longitudine
        tvLatitudine = (TextView)this.findViewById(R.id.tvLatitudine);
        tvLongitudine = (TextView)this.findViewById(R.id.tvLongitudine);
        
        mapView= (MapView) findViewById(R.id.mapview);
        
        mapView.setClickable(true);                 //abilita il pan(trascinamento mappa)
        mapView.setBuiltInZoomControls(true);      	//abilita lo zoom
        mapView.setSatellite(true);              	//abilita la vista con mappe satelitari
        
        
        
        // Aggiungiamo 'overlay sulla mappa della nostra posizione
        myLocationOverlay = new ContaChilometriLocationOverlay(this, mapView, mChronometer);
        List<Overlay> overlays = mapView.getOverlays();
        overlays.add(myLocationOverlay);
        myLocationOverlay.enableMyLocation(); 
        
      
        
        // Otteniamo il riferimento al controller
        mapController = mapView.getController();
        
        
         // Otteniamo il riferimento al LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        // Verifichiamo se il GPS è abilitato altrimenti avvisiamo l'utente
        if(!locationManager.isProviderEnabled("gps")){
                Toast.makeText(this, "GPS è attualmente disabilitato. E' possibile abilitarlo dal menu impostazioni.", Toast.LENGTH_LONG).show();
        }
        
        
        
        //Registriamo il LocationListener al provider GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
       
        
    	
        //Si prendono i riferimenti dalla prima activity
        if(parameter1 == null && getIntent().hasExtra("iltestonelbox1")
           && parameter2 == null && getIntent().hasExtra("iltestonelbox2")){
        	
        	parameter1 = getIntent().getExtras().getString("iltestonelbox1");
     		parameter2 = getIntent().getExtras().getString("iltestonelbox2");
     		
     		
     		saveState();
     		
        }
        else
        {
        	//i parametri non provengono dalla prima activity
        	SharedPreferences settings = this.getSharedPreferences(PREFS, 0);
        	parameter1 = settings.getString("USR", null);
        	parameter2 = settings.getString("PWD", null);
        }
        Log.i("mio","user: "+parameter1+"pwd: "+parameter2);
        
       

        
      //Si prendono i riferimenti dalla prima activity
        if(!amico.equals("")){
        	
        	//i parametri non provengono dalla prima activity
        	SharedPreferences settings = this.getSharedPreferences(PREFS, 0);
        	amico = settings.getString("FRD", null);
        	Log.i("mio","user: "+amico);
        }
        
        


        
        
      //connessione al server
        if(connection == null || !connection.isConnected() )
        {
        try{
			ConnectionConfiguration config = new ConnectionConfiguration("ppl.eln.uniroma2.it", 5222);
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
			config.setReconnectionAllowed(true);
			Log.i("paolo","congfigurazione effettuata");
			connection = (Connection) new XMPPConnection(config);
			Log.i("paolo","connection creata");
			((XMPPConnection) connection).connect();
			Log.i("paolo","connessione effettuata");
			((org.jivesoftware.smack.Connection) connection).login(parameter1,parameter2);
			Log.i("paolo","logged in");
			
		    Toast.makeText(context, "connessione al server effettuata", Toast.LENGTH_LONG).show();

			
		} catch (XMPPException e) {
			
			//se i parametri sono errati, si riva nella activity di log in
			Intent intenterror = new Intent(Maps.this,ContaChilometriActivity.class);
			startActivity(intenterror);
			
		    Toast.makeText(context, "accesso al server negato: username o password ERRATI o non siete connessi a internet", Toast.LENGTH_LONG).show();

		       
		       

			e.printStackTrace();
		}
        
        
        
        
        //gestione messaggi in arrivo
        
        ((org.jivesoftware.smack.Connection) connection).addPacketListener(new PacketListener() {
			@Override
			public void processPacket(Packet pkt) {
				
				Message msg = (Message) pkt;
				String from = msg.getFrom();
				
				
				
				//Log.i("paolo","from: "+from);
				String body = msg.getBody();
				
				amico = from.split("@")[0];
				Log.i("paolo","nuovo amico: "+amico);
				
				
				handler.obtainMessage(1, body).sendToTarget();
				
				

			}
		},new MessageTypeFilter(Message.Type.normal));
    
        }
        
        
        
       //aggiunge alla notifica il valore dei km percorsi
       Log.i("mio","conected");
       if(getIntent().hasExtra("value"))
       {
       	val = getIntent().getExtras().getDouble("value");
       	Log.i("mio","Oncreate mydistance "+val);
       	
       	showmessage("Hai percorso "+val+"  m");
       	
       	sendMsg("DST:"+val);
       	
       	//inivare val a mattia tramite xmpp
       }
       else
       {
       	Log.i("mio","no extra value ");
       }
    }
    
 /*   @Override
    public void onNewIntent(Intent intent)
    {
    	Bundle extras = intent.getExtras();
    	Log.i("mio","onNewIntent");
    	if(extras.containsKey("value")){
    		Log.i("mio","my distance: "+extras.getInt("value"));
    	}
    	
    }
*/
    
	//on pause-gestisce la notifica quando l'app va in background
    public void onPause() {
    	
    	Log.i("mio", "ONpause maps");
    	saveState();  //quando va onPause(chiamata,schermo spento, notifica) salva lo stato (per la connection)
    	String ns = Context.NOTIFICATION_SERVICE;
    	mNotificationManager = (NotificationManager) getSystemService(ns);
  	       
    	int icon = R.drawable.van;
    	CharSequence tickerText = "Conta Chilometri";
    	long when = System.currentTimeMillis();
  	      

    	notification1 = new Notification(icon, tickerText, when);
  	       
    	Context context = getApplicationContext();
  	    CharSequence contentTitle = "Servizio Conta Chilometri";
  	    CharSequence contentText = "In esecuzione";
  	    Intent notificationIntent = new Intent(this, Maps.class);
  	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
  	       
  	  	notification1.flags |= Notification.FLAG_AUTO_CANCEL;   
  	  	notification1.flags |= Notification.FLAG_NO_CLEAR;//non si cancella dalla notification bar

  	  	
  	    notification1.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
  	    mNotificationManager.notify(1, notification1);
  	  
  	    super.onPause();
  	    
  	    connection.disconnect();
  	  
  	    Log.i("connection disconnect","connection disconnect");
    }
    
     
       

    LocationListener locationListener = new LocationListener() {
    	
        @Override
        public void onLocationChanged(Location location) {
                // Aggiorna il marker della mappa
                mapView.invalidate();

                // Aggiorna la location
                Double geoLat = location.getLatitude()*1E6;
                Double geoLng = location.getLongitude()*1E6;
                GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue()); 

                mapController.animateTo(point);
                
                             

                tvLatitudine.setText(Double.toString(getRound(location.getLatitude(), 5	)));
                tvLongitudine.setText(Double.toString(getRound(location.getLongitude(), 5)));
        
                // mando la mia posizione solo se ho un amico a cui mandarla
                if( !amico.equals("") )
                	sendMsg("POS:"+geoLat+";"+geoLng);
                
                
                
        }	

        
        @Override
        public void onProviderDisabled(String provider) {
               // Toast.makeText(Maps.this,
                          //      "onProviderDisabled "+provider, Toast.LENGTH_SHORT).show();
        }
        

        @Override
        public void onProviderEnabled(String provider) {
               // Toast.makeText(Maps.this,
               //    "onProviderEnabled "+provider, Toast.LENGTH_SHORT).show();
        }
        

        @Override
        public void onStatusChanged(String provider, int status,Bundle extras) {
               // Toast.makeText(Maps.this,
               //        "onStatusChanged "+provider+" status: "+status, Toast.LENGTH_SHORT).show();
        }
        
     
    };
	
    //getisce il tasto menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("Aggiungi amico");
    	return true;
    	    	
    }
			
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
    	
    		case 0:
    			//crea un alertdialog con un edittext perl'inserimento dell'utente da cercare
		    	AlertDialog.Builder alert = new AlertDialog.Builder(this);

		    	  
		    	alert.setTitle("Cerca utente"); 
		        alert.setMessage("Inserire username"); 
		          
		        // Setta un EditText view per prendere l'input dell'user
		         
		        
		        final EditText input = new EditText(this); 
		        alert.setView(input);

		        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        	
		        	public void onClick(DialogInterface dialog, int whichButton) {  
		        		amico = input.getText().toString();  
		        		
		        		saveStateFriend();
		        	
		        	
		        		
		        		return;
		        	}  
		        });  

		        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

		        	public void onClick(DialogInterface dialog, int which) {
		        		// TODO Auto-generated method stub
		        		return;   
		        	}
		        }); 
		        
		        alert.show();
		        
		        
		    	//Toast.makeText(getApplicationContext(), "non ci sono amici online", Toast.LENGTH_SHORT).show();
		        return true;
		        
    		default:
    			return super.onOptionsItemSelected(item);
    	}
		      
    }
			
			

    public static double getRound(double x, int digits){
    	double powerOfTen = Math.pow(10, digits);  							//pow(x,y)= x^y
    	return ((double)Math.round(x * powerOfTen) / powerOfTen);			//approssimazione
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	// invio dei messaggi
	public void sendMsg(String s){
		Message msg = new Message();
		
		
		SharedPreferences settings = this.getSharedPreferences(PREFS, 0);
    	friend = settings.getString("FRD", null);
		msg.setTo(friend+"@ppl.eln.uniroma2.it");
		msg.setBody(s);		
		((XMPPConnection) connection).sendPacket(msg);
		
		 Log.i("mio", "Invio MSG"+friend);
	}
}
        
        
