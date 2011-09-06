package nz.gen.wellington.penguin.timers;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class LocationUpdateService extends Service {

	private static final String TAG = "ContentUpdateService";
    
    public static final int UPDATE_COMPLETE_NOTIFICATION_ID = 1;

    public static final String BATCH_COMPLETION = "nz.gen.wellington.penguin.CONTENT_UPDATE_BATCH_COMPLETION";
	
	public static int RUNNING = 1;
	public static final int STOPPED = 2;
	    
    private final IBinder mBinder = new ContentUpdateServiceBinder();
    
	private PowerManager.WakeLock wl;
	
    @Override
    public void onCreate() {
    	super.onCreate();              
    }
    
    @Override
    public void onStart(Intent intent, int startId) {
    	super.onStart(intent, startId);
    	createWakeLock();
    	if (intent != null && intent.getAction() != null && intent.getAction().equals("RUN")) {
    		Log.i(TAG, "Got start command");                   
    		LocationUpdateRunnable internalRunnable = new LocationUpdateRunnable(this.getApplicationContext(), (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE));
    		internalRunnable.run();
        }
    	releaseWakeLock();
    }
    
    @Override
	public void onDestroy() {
		super.onDestroy();
		releaseWakeLock();
	}
    
	@Override
	public IBinder onBind(Intent arg0) {
        return mBinder;
	}

	public class ContentUpdateServiceBinder extends Binder {
        public LocationUpdateService getService() {
        	return LocationUpdateService.this;
        }
	}

	private void createWakeLock() {
		PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		wl.acquire();
	}

	private void releaseWakeLock() {
		if (wl != null) {
			if (wl.isHeld()) {
				wl.release();
			}
		}
	}
	
}
