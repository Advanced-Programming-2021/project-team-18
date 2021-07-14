package debug;


import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.HttpParamsNames;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;

public class DebugConsole {

    @SneakyThrows
    public static void main(String[] args) {
        String location = "http://localhost:8080";
        HashMap hashMap = new HashMap();
        hashMap.put("username" , "a");
        System.out.println(getRequest(hashMap , location + "/api/user"));

    }
    @SneakyThrows
    public static String getRequest(HashMap<String,String> params , String location) {
        URIBuilder builder = new URIBuilder(location);
        for(String param : params.keySet())
            builder = builder.setParameter(param , params.get(param));
        HttpGet getRequest = new HttpGet(builder.build());
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(getRequest);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        return responseString;
    }
}
