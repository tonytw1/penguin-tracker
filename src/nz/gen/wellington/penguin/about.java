package nz.gen.wellington.penguin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;

public class about extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        setContentView(R.layout.about);       
        WebView about = (WebView) findViewById(R.id.about);
        about.loadData("<h2>About</h2>", "text/html", "UTF-8");
    }

	// TODO Credit:
	// Flickr image
	// sirtrack
	// @nzemperor	
	// github link
}
