package gatech.cs6300.project2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewCustomerActivity extends Activity implements IHttpPostAsyncResponse
{
    private HttpPostAsyncTask postTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvip);
        
        EditText phoneText = (EditText)findViewById(R.id.phoneEditText);
        phoneText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        
        EditText dateText = (EditText)findViewById(R.id.dateEditText);
        dateText.addTextChangedListener(new DateFormattingTextWatcher());
        
        if (android.os.Build.VERSION.SDK_INT > 9) 
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    
    // Handles button press
    public void handleButtonClick(View view) throws JSONException
    {
        EditText nameText = (EditText)findViewById(R.id.nameEditText);
        EditText phoneText = (EditText)findViewById(R.id.phoneEditText);
        EditText dateText = (EditText)findViewById(R.id.dateEditText);
        
        JSONObject json = new JSONObject();
        json.put("name", nameText.getText().toString());
        json.put("dob", dateText.getText().toString());
        json.put("phone", PhoneNumberUtils.formatNumber(phoneText.getText().toString()));
        System.out.println(json.toString());
        String url_string = "http://gatechteam4proj2.appspot.com/create.json";
        //String url_string = "http://10.0.3.2:9080/create.json";
        postTask = (HttpPostAsyncTask) new HttpPostAsyncTask(json).execute(url_string);
        postTask.delegate = this;
    }


    @Override
    public void processFinish(HttpResponse response)
    {
        if(response != null)
        {
            try 
            {
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
            
                while ((line = reader.readLine()) != null) 
                {
                    sb.append(line + "\n");
                }
                System.out.println(sb.toString());
                
                String error_message = "";
                try 
                {
                    JSONObject resp_json = new JSONObject(sb.toString());
                    if(resp_json.has("card_number"))
                    {
                        error_message = "card number: " + resp_json.getLong("card_number");
                    }
                    else if(resp_json.has("error_name"))
                    {
                        error_message = resp_json.getString("error_name");
                    }
                    else if(resp_json.has("error_dob"))
                    {
                        error_message = resp_json.getString("error_dob");
                    }
                    else if(resp_json.has("error_phone"))
                    {
                        error_message = resp_json.getString("error_phone");
                    }
                    else if(resp_json.has("error"))
                    {
                        error_message = resp_json.getString("error");
                    }
                }
                catch(Exception e)
                {
                    error_message = "Invalid response from server.";
                }
                 
                TextView errorText = (TextView)findViewById(R.id.errorText);
                errorText.setText(error_message);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }       
    }
}
