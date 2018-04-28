package gatech.cs6300.project2;

import org.apache.http.HttpResponse;

public interface IHttpPostAsyncResponse
{
    void processFinish(HttpResponse response);
}
