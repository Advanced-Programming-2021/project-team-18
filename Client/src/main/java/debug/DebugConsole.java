package debug;


import com.google.gson.Gson;
import game.User;
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
import utility.Utility;

import java.util.HashMap;

public class DebugConsole {

    @SneakyThrows
    public static void main(String[] args) {
        String location = "http://localhost:8080";
        HashMap hashMap = new HashMap();
        hashMap.put("username" , "a");
        hashMap.put("password" , "a");
        String data = Utility.getRequest(hashMap , location + "/api/user");
        Gson gson = new Gson();
//        User user = gson.fromJson(data , User.class);
        System.out.println(data);
    }

}
