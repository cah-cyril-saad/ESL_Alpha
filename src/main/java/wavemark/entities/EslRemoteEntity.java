package main.java.wavemark.entities;

import java.util.Map;

public class EslRemoteEntity {
    
    private String articleId;
    private String articleName;
    private String nfcUrl;
    private Map<String, String> data;
    
    public EslRemoteEntity(String articleId, String articleName, String nfcUrl, Map<String, String> data) {
        this.articleId = articleId;
        this.articleName = articleName;
        this.nfcUrl = nfcUrl;
        this.data = data;
    }
    
    public String getArticleId() {
        return articleId;
    }
    
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    
    public String getArticleName() {
        return articleName;
    }
    
    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
    
    public String getNfcUrl() {
        return nfcUrl;
    }
    
    public void setNfcUrl(String nfcUrl) {
        this.nfcUrl = nfcUrl;
    }
    
    
    public Map<String, String> getData() {
        return data;
    }
    
    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
