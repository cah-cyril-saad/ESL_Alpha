package main.java.wavemark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wavemark.wmrestlib.caller.HttpRestCaller;
import com.wavemark.wmrestlib.entity.RequestParam;
import java.io.File;
import java.io.IOException;
import java.util.List;
import main.java.wavemark.controller.EntityHandler;
import main.java.wavemark.entities.AppProduct;
import main.java.wavemark.entities.ESLProduct;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

public class SolumWebservices {
    
    static OkHttpClient client = new OkHttpClient().newBuilder().build();
    
    //    static HttpRestCaller client2 = new Htt
    public static void updateSolumProducts(List<ESLProduct> products) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String authToken = getSolumAccessToken("cyril.saad@cardinalhealth.com", "Dwia#22!!7");
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        
        for (ESLProduct product : products) {
            array.add(EntityHandler.translateESLProductToJsonString(product));
            System.out.println("Updated ESL Product the following binset number: " + product.getBinSetNumber());
        }
        RequestBody body = RequestBody.create(mediaType, array.toString());
        Request request = new Request.Builder().url("https://eastus.common.solumesl.com/common/api/v2/common/articles?company=CDH&store=WM001").method("PUT", body).addHeader("Authorization", "Bearer " + authToken).addHeader("Content-Type", "application/json").build();
        client.newCall(request).execute();
    }
    
    public static void updateSolumProductsExpiryData(List<ESLProduct> products) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String authToken = getSolumAccessToken("cyril.saad@cardinalhealth.com", "Dwia#22!!7");
        
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        
        for (ESLProduct product : products) {
            
            array.add(EntityHandler.createExpiryDataNode(product));
        }
        RequestBody body = RequestBody.create(mediaType, array.toString());
        Request request = new Request.Builder().url("https://eastus.common.solumesl.com/common/api/v2/common/articles?company=CDH&store=WM001").method("PUT", body).addHeader("Authorization", "Bearer " + authToken).addHeader("Content-Type", "application/json").build();
        client.newCall(request).execute();
    }
    
    public static AppProduct[] getEslBins(String date) throws Exception {
        String webserviceURI = "https://testonline.wavemark.net/kanban/esl/binsets";
        HttpRestCaller restProxy = new HttpRestCaller(webserviceURI, 100000, 100000, null, 0, null);
        restProxy.printRawResponse(true);
        restProxy.setContentType("application/json");
        RequestParam[] requestParams = new RequestParam[1];
        RequestParam deviceIdParam = new RequestParam("fromDate", date, RequestParam.REQUEST_PARAM_TYPE_QUERY_PARAM);
        requestParams[0] = deviceIdParam;
        //            AppProduct[] bins = SolumWebservices.getEslBins("May 11, 2023 12:02 PM GMT+3");
//        return restProxy.call(requestParams, AppProduct[].class, HttpRestCaller.REQUEST_METHOD_GET, null, null, 3, 1, true);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("C:\\Development\\Wavemark\\EslResearchAndDevelopment\\mock"), AppProduct[].class);
    }
    
    public static String getSolumAccessToken(String username, String password) throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        
        String[] credentials = new String[2];
        credentials[0] = password;
        credentials[1] = username;
        RequestBody body = RequestBody.create(mediaType, String.format("password=%s&username=%s", credentials));
        
        //TODO: Add HttpHandler
        Request request = new Request.Builder().url("https://eastus.common.solumesl.com/common/api/v2/token").method("POST", body).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
        
        ObjectMapper mapper = new ObjectMapper();
        Response response;
        JsonNode actualObj;
        String token = "";
        
        //TODO: replace with event when response is 401
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
}
