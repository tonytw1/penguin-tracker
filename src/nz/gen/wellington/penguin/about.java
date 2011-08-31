package nz.gen.wellington.penguin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class about extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.about);
        
        TextView about = (TextView) findViewById(R.id.about);
        about.setText("About - tracks the happy feet penguin");
    }
		
}
