//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//public class ESLUpdateSingleTag {
//
//    private Map<String, String> data;
//
//    public static void main(String[] args) throws IOException {
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                                                .build();
//        MediaType mediaType = MediaType.parse("application/json");
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
//
//    }
//
//}