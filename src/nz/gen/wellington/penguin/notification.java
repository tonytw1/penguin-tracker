package nz.gen.wellington.penguin;

import nz.gen.wellington.penguin.timers.LocationUpdateService;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class notification extends Activity {

	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);		
		 
		 NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		 notificationManager.cancel(LocationUpdateService.UPDATE_COMPLETE_NOTIFICATION_ID);
		 
		 Intent intent = new Intent(this, main.class);
		 this.startActivity(intent);
	}
	
}
