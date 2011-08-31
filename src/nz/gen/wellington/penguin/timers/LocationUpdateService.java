package nz.gen.wellington.penguin.timers;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LocationUpdateService extends Service {

	private static final String TAG = "ContentUpdateService";
    
    public static final int UPDATE_COMPLETE_NOTIFICATION_ID = 1;

    public static final String BATCH_COMPLETION = "nz.gen.wellington.penguin.CONTENT_UPDATE_BATCH_COMPLETION";
	
	public static int RUNNING = 1;
	public static final int STOPPED = 2;
	
    private Thread thread;
    private LocationUpdateRunnable internalRunnable;
    
    private final IBinder mBinder = new ContentUpdateServiceBinder();
    
    @Override
    public void onCreate() {
    	super.onCreate();              
    }

    @Override
    public void onStart(Intent intent, int startId) {
    	super.onStart(intent, startId);
    	if (intent != null && intent.getAction() != null && intent.getAction().equals("RUN")) {
    		Log.i(TAG, "Got start command");                   
    		internalRunnable = new LocationUpdateRunnable(this.getApplicationContext(), (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE));
    		thread = new Thread(this.internalRunnable);
    		thread.setDaemon(true);
    		thread.start();
    		internalRunnable.run();
        }
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

}
