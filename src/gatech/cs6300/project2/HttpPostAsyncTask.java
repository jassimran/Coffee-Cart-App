package gatech.cs6300.project2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;

public class HttpPostAsyncTask extends AsyncTask<String, Void, HttpResponse> 
{
    public IHttpPostAsyncResponse delegate=null;
    private JSONObject json;
    private HttpResponse response;
    
    public HttpPostAsyncTask(JSONObject json) 
    {
        super();
        this.json = json;
    }
    
    protected HttpResponse doInBackground(String... url) 
    {
        String jsonString = json.toString();
        String urlString = url[0];
        
        try 
        {
            HttpPost httpPost = new HttpPost(urlString);
            httpPost.setEntity(new StringEntity(jsonString));
            httpPost.setHeader("Content-type", "application/json");
            response = new DefaultHttpClient().execute(httpPost);
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        } 
        catch (ClientProtocolException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        return response;
    }

    protected void onProgressUpdate(Integer... progress) 
    {
        //setProgressPercent(progress[0]);
    }

    protected void onPostExecute(HttpResponse result) 
    {
        delegate.processFinish(result);
    }
}