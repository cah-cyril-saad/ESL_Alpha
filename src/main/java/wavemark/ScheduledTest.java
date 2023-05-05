package main.java.wavemark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.StringUtils;

public class ScheduledTest {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        AppProduct[] bins = getEslBins("Apr 28, 2023 9:44 AM GMT+3");
    
    }

    enum State {
        DEACTIVATED,
        ACTIVE,
        NEW
    }
}
