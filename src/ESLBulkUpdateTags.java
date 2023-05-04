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

//                                                .build();
//        //        String authToken = System.getenv("ESL_TOKEN");
//        String authToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ilg1ZVhrNHh5b2pORnVtMWtsMll0djhkbE5QNC1jNTdkTzZRR1RWQndhTmsiLCJ0eXAiOiJKV1QifQ.eyJpZHAiOiJMb2NhbEFjY291bnQiLCJvaWQiOiI5MzhlNmE0Ni0xMmRlLTQ5NjUtYTQ3ZC0yYTBkMWM0ZTViNzAiLCJzdWIiOiI5MzhlNmE0Ni0xMmRlLTQ5NjUtYTQ3ZC0yYTBkMWM0ZTViNzAiLCJuYW1lIjoiRGFueU9zdGEiLCJuZXdVc2VyIjpmYWxzZSwiZXh0ZW5zaW9uX0FkbWluQXBwcm92ZWQiOnRydWUsImV4dGVuc2lvbl9DdXN0b21lckNvZGUiOiJDREgiLCJleHRlbnNpb25fQ3VzdG9tZXJMZXZlbCI6IjEiLCJlbWFpbHMiOlsiZGFueS5vc3RhQGNhcmRpbmFsaGVhbHRoLmNvbSJdLCJ0ZnAiOiJCMkNfMV9ST1BDX0F1dGgiLCJhenAiOiI5N2E5Mzg0NC03YjU3LTQzODMtYjZlNi0zMDNjYjZjNWY0MjQiLCJ2ZXIiOiIxLjAiLCJpYXQiOjE2ODE5OTAwMzMsImF1ZCI6Ijk3YTkzODQ0LTdiNTctNDM4My1iNmU2LTMwM2NiNmM1ZjQyNCIsImV4cCI6MTY4MTk5MzYzMywiaXNzIjoiaHR0cHM6Ly9zb2x1bWIyY3NhaS5iMmNsb2dpbi5jb20vNjM3NDVkYjItNDYyYi00Y2IzLWEyNTctMGU1OTljZTY4Y2Y5L3YyLjAvIiwibmJmIjoxNjgxOTkwMDMzfQ" +
//                           ".ECVx42OMP5TeTs1aXGXPTdEPhnj_fPfw4Xq3Uay0CBKrIheHg7pUdBl9jgOC_ytuJR_SpmhpDqr5X94UqFnV39n9yW63qwy_Ld2K9qCpnj4PysqWr8l1ESpDXcZ5-LB4ghRhS5eVnJii4Eh6_tXgAFTrXqVNXPCslyc-qDj7brdnjr2PA6mMZQ-wcJZG0awZHev57wWfCnaWqdnWBKnFvGSnWqMLcyJg4ORcWZOddgVx62gPfmoVtYSEejujAzVsN9ovZ8DMsmO15-WrI7oRdOav86LVX-iHkuMEq4cKUMCgjrmZt7Wl_ERp9A665txnrKHPqNLLkA371dTRb4DP6Q";
//        System.out.println(authToken);
//        Map<String, String> data = new HashMap<>();
//        data.put("ITEM_NUM", "phisherman");
//        data.put("STORAGE_NUM", "123456");
//        data.put("RACK_NUM", "05");
//        data.put("SHELF_NUM", "03");
//        data.put("EXPIRATION_DATE", "4/12/2024");
//
//        Product product = new Product("DAN0000001", "A short item name", "", data);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = "[" + mapper.writeValueAsString(product) + "]";
//        RequestBody body = RequestBody.create(mediaType, jsonString);
//        Request request = new Request.Builder()
//                              .url("https://eastus.common.solumesl.com/common/api/v2/common/articles?company=CDH&store=WM001")
//                              .method("POST", body)
//                              .addHeader("Authorization", "Bearer " + authToken)
//                              .addHeader("Content-Type", "application/json")
//                              .build();
//        Response response = client.newCall(request).execute();
public class ESLBulkUpdateTags {
    
    static OkHttpClient client = new OkHttpClient().newBuilder().build();
    static String authToken = "";
    public static void main(String[] args) throws IOException {
//                String tokenForSolum = getSolumAccessToken("cyril.saad@cardinalhealth.com", "Dwia#22!!7");
        AppProduct[] bins = getEslBins("Apr 28, 2023 9:44 AM GMT+3");
        ESLProduct[] eslProducts = EntityTranslator.translateFromAppToESL(bins);

        updateSolumProducts(eslProducts);
//        System.out.println(getSolumAccessToken("cyril.saad@cardinalhealth.com", "Dwia#22!!7"));
        System.out.println("Done");
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
        Response response = client.newCall(request).execute();
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(response.body().string());
        return actualObj.get("responseMessage").get("access_token").textValue();
    }
    
    public static void updateSolumProducts(ESLProduct[] products) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        String authToken = getSolumAccessToken("cyril.saad@cardinalhealth.com", "Dwia#22!!7");
        
        for (ESLProduct product : products) {
            String jsonString = EntityTranslator.translateESLProductToJsonString(product);
            RequestBody body = RequestBody.create(mediaType, jsonString);
            Request request = new Request.Builder()
                                  .url("https://eastus.common.solumesl.com/common/api/v2/common/articles?company=CDH&store=WM001")
                                  .method("POST", body)
                                  .addHeader("Authorization", "Bearer " + authToken)
                                  .addHeader("Content-Type", "application/json")
                                  .build();
            client.newCall(request).execute();
        }
    }
}
