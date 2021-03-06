package nz.gen.wellington.penguin.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

public class HttpFetcher {
	
	private static final String TAG = "HttpFetcher";
	
	private static final int HTTP_TIMEOUT = 15000;
	
    private HttpRequestBase active;
	private HttpClient client;
	
	public HttpFetcher(Context context) {
		client = new DefaultHttpClient();
		
		((AbstractHttpClient) client)
		.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(final HttpRequest request,
					final HttpContext context)
					throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}
		});
    			  
		((AbstractHttpClient) client)
		.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(final HttpResponse response,
					final HttpContext context)
					throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				Header ceheader = entity.getContentEncoding();
				if (ceheader != null) {
					HeaderElement[] codecs = ceheader.getElements();
					for (int i = 0; i < codecs.length; i++) {
						if (codecs[i].getName().equalsIgnoreCase("gzip")) {
							response.setEntity(new GzipDecompressingEntity(
									response.getEntity()));
							return;
						}
					}
				}
			}
		});
		
		client.getParams().setParameter("http.socket.timeout", new Integer(HTTP_TIMEOUT));
		client.getParams().setParameter("http.connection.timeout", new Integer(HTTP_TIMEOUT));
	}
	
	public InputStream httpFetch(String uri) {
		try {
			Log.i(TAG, "Making http fetch of: " + uri);						
			HttpGet get = new HttpGet(uri);	
			
			get.addHeader(new BasicHeader("User-agent", "gzip"));
			get.addHeader(new BasicHeader("Accept-Encoding", "gzip"));
						
			HttpResponse execute = executeRequest(get);
			final int statusCode = execute.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				return execute.getEntity().getContent();
			}
			
			Log.w(TAG, "Fetch of '" + uri + "' failed: " + statusCode);
			return null;
			
		} catch (Exception e) {
			Log.e(TAG, "Http exception: " + e.getMessage());
		}
		return null;
	}
	
	public String httpEtag(String url) {
		try {
			Log.i(TAG, "Making http etag head fetch of url: " + url);
			HttpHead head = new HttpHead(url);			
			HttpResponse execute = executeRequest(head);
			if (execute.getStatusLine().getStatusCode() == 200) {
				Header[] etags = execute.getHeaders("Etag");
				if (etags.length == 1) {
					final String result = etags[0].getValue();				
					return result;
				}
			}
			
		} catch (Exception e) {
			Log.e(TAG, "Http exception: " + e.getMessage());
		}		
		return null;
	}
	
	public void stopLoading() {
		Log.i(TAG, "Stopping loading");
		if (active != null) {
			active.abort();
		}
	}
	
	private HttpResponse executeRequest(HttpRequestBase request) throws IOException, ClientProtocolException {
		active = request;
		return client.execute(request);
	}
	
	static class GzipDecompressingEntity extends HttpEntityWrapper {

        public GzipDecompressingEntity(final HttpEntity entity) {
            super(entity);
        }
    
        @Override
        public InputStream getContent()
            throws IOException, IllegalStateException {
            InputStream wrappedin = wrappedEntity.getContent();
            return new GZIPInputStream(wrappedin);
        }

        @Override
        public long getContentLength() {
            return this.wrappedEntity.getContentLength();
        }
    }
	
}
