package me.pkg.pdm;


import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

public class MyCount extends CountDownTimer{
	
    private Handler mHandler;
    
    public MyCount(long millisInFuture, long countDownInterval, Handler m) {
    	super(millisInFuture, countDownInterval);
     	mHandler = m;
     }
    

     public void onFinish() {
    	 mHandler.sendEmptyMessage(0);
    	 Log.i("mio","ONFINISH mycount");
    }
     
    //tempo che rimae alla fine del countdown
    public void onTick(long arg0) {
    	
                   
    }
            
}
