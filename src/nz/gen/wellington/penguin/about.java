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
        
        StringBuilder text = new StringBuilder();
        text.append("<h4>About</h4>");
        
        text.append("<p>The <a href=\"http://tinyurl.com/3osqgtl/\">lost emperor penguin 'Happy Feet'</a> is currently making his way home after recovering at Wellington Zoo in New Zealand.");
        text.append("<p>Happy Feet is wearing a <a href=\"http://blog.tepapa.govt.nz/2011/08/29/happy-feet-gets-technological/\">Sirtrack KiwiSat 202 Satellite Transmitter</a> which will track his progress after he is released into the Southern Ocean. ");
        text.append("The tracker signal is processed by the <a href=\"http://www.argos-system.org/html/system/how_it_works_en.html\">Argos satellite network</a> before been ");
        text.append("published on the <a href=\"http://www.nzemperor.com/\">@NZEmperor</a> website.</p>");
        
        text.append("<p>This application periodically polls the tracking feed, raising an Android notification when a new position fix is published. " +
        		"For convenience all dates and times are displayed using your phone's local time zone.</p>");
       
        text.append("<p>To conserve battery power, the tracker attached to the penguin is programmed to transmit during 2 periods, totalling 7 hours per day. " +
        		"These periods correspond to times when satellites are most likely to be overhead. " +
        		"Location updates are likely to be clustered  around these transmission windows.</p><p>The current expected state of the transmitter is shown at the top of the map view.</p> " +
        		"<p>Penguins can spent a considerable amount of time underwater. Signals cannot be received while the penguin is submerged. There may be days when no reading are received.");

        		
        text.append("<hr><p>Application developed by Tony McCrae.</p>");        
        text.append("<p>Penguin image obtained from a Creative Commons licensed photograph by <a href=\"http://www.flickr.com/photos/elisfanclub/5955801117\">Eli Duke</a>.");
        text.append("<p>Emperor penguin sound clip extracted with the author's permission from the YouTube video '<a href=\"http://www.youtube.com/watch?v=0Haxy5PvCuk\">Emperor Likes Me</a>'.");
        
        text.append("<p>Full source code is available on Github: <a href=\"https://github.com/tonytw1/penguin-tracker\">https://github.com/tonytw1/penguin-tracker</a></p>");
        text.append("<p>This program is free software: you can redistribute it and/or modify " +
        		"it under the terms of the GNU General Public License as published by " +
        		"the Free Software Foundation, either version 3 of the License, or (at your option) any later version.</p>");
        
        WebView about = (WebView) findViewById(R.id.about);
        about.loadData(text.toString(), "text/html", "UTF-8");
    }
	
}
