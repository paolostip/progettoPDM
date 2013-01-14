package me.pkg.pdm;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;







/*
* PROVA DI CONNESSIONE
* 
* DA ELIMINARE!!!!
*/







public class XmppCon {
	
	
	private Context context;
	
	private Connection connection;
	private String username;
	private String password;
	
	
	public XmppCon(String u, String p, Context c){
		
		username = u;
		password = p;
		context = c;
		
	}
	
	
	
	public void connect()
	{
		
		try{
			ConnectionConfiguration config = new ConnectionConfiguration("ppl.eln.uniroma2.it", 5222);
			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
			Log.i("paolo","congfigurazione effettuata");
			connection = (Connection) new XMPPConnection(config);
			Log.i("paolo","connection creata");
			((XMPPConnection) connection).connect();
			Log.i("paolo","connessione effettuata");
			((org.jivesoftware.smack.Connection) connection).login(username, password);
			Log.i("paolo","logged in");
			
		    Toast.makeText(context, "login effettuato", Toast.LENGTH_LONG).show();

			
		} catch (XMPPException e) {
			Log.e("paolo", "try catch error connection");
			
	       Toast.makeText(context, "accesso al server negato", Toast.LENGTH_LONG).show();

			
			e.printStackTrace();
		}
	}
	
	public void Send(String to, String text) {
		
	Message msg = new Message();
		
		msg.setTo(to+"@ppl.eln.uniroma2.it");
		msg.setBody("MSG:"+text);
		//msg.setBody("MSG:"+et.getText().toString());
		
		connection.sendPacket(msg);
		//((XMPPConnection) connection).sendPacket(msg);
		
		
		
	}
	
	/*public void receive(){
		
		((org.jivesoftware.smack.Connection) connection).addPacketListener(new PacketListener() {
   			@Override
   			public void processPacket(Packet pkt) {
   				Message msg = (Message) pkt;
   				String from = msg.getFrom();
   				String body = msg.getBody();
   				//tv.append(from+" : "+body+"\n");
   			}
   		},new MessageTypeFilter(Message.Type.normal));
   		
   		
   		}
		
		
		
		*/
		
	
}
