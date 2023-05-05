package main.java.wavemark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;

public class ESLBulkUpdateTags {
    
    static OkHttpClient client = new OkHttpClient().newBuilder().build();
    static String authToken = "";
    
    public static void main(String[] args) throws IOException {
        System.out.println(ESLBulkUpdateTags.class.getCanonicalName());
        AppProduct[] bins = getEslBins("Apr 28, 2023 9:44 AM GMT+3");
        ESLProduct[] eslProducts = EntityTranslator.translateFromAppToESL(bins);
        
        updateSolumProducts(eslProducts);
        System.out.println("Done");
    }
    
    public static String getSolumAccessToken(String username, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        
        String[] credentials = new String[2];
        credentials[0] = password;
        credentials[1] = username;
        RequestBody body = RequestBody.create(mediaType, String.format("password=%s&username=%s", credentials));
        Request request = new Request.Builder()
                              .url("https://eastus.common.solumesl.com/common/api/v2/token")
                              .method("POST", body)
                              .addHeader("Content-Type", "application/x-www-form-urlencoded")
                              .build();
        
        ObjectMapper mapper = new ObjectMapper();
        Response response;
        JsonNode actualObj;
        String token = "";
        while (token.contains("MESSAGE_EXPIRED") || StringUtils.isBlank(token)) {
            response = client.newCall(request).execute();
            actualObj = mapper.readTree(response.body().string());
            
            token = actualObj.get("responseMessage").toString();
            
            if (actualObj.get("responseMessage").toString().contains("access_token")) {
                token = actualObj.get("responseMessage").get("access_token").textValue();
            }
        }
        return token;
    }
    
    public static AppProduct[] getEslBins(String date) throws IOException {
        client = new OkHttpClient().newBuilder()
                                   .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                              .url("https://testonline.wavemark.net/kanban/esl/binsets?fromDate=" + date)
                              .method("GET", null)
                              .addHeader("Authorization", "Bearer tPE3uSdka_obJXY6OF2d6Utzrgo")
                              .build();
        ObjectMapper objectMapper = new ObjectMapper();
        ResponseBody responseBody = client.newCall(request).execute().body();
        
        //        return objectMapper.readValue(responseBody.string(), AppProduct[].class);
        //just for mocking a response
        return objectMapper.readValue(new File("C:\\Development\\Wavemark\\EslResearchAndDevelopment\\src\\mock"), AppProduct[].class);
    }
    
    
    public static void updateSolumProducts(ESLProduct[] products) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String authToken = getSolumAccessToken("cyril.saad@cardinalhealth.com", "Dwia#22!!7");
        StringBuilder jsonString = new StringBuilder("[");
        for (ESLProduct product : products) {
            jsonString.append(EntityTranslator.translateESLProductToJsonString(product));
            
        }
        RequestBody body = RequestBody.create(mediaType, String.valueOf(jsonString));
        System.out.println("Updated the following product: " + jsonString);
        //TODO: Reduce to single webservice
        Request request = new Request.Builder()
                              .url("https://eastus.common.solumesl.com/common/api/v2/common/articles?company=CDH&store=WM001")
                              .method("POST", body)
                              .addHeader("Authorization", "Bearer " + authToken)
                              .addHeader("Content-Type", "application/json")
                              .build();
        client.newCall(request).execute();
    }
}
