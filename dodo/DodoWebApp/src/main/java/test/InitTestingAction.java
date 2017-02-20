package test;

import com.opensymphony.xwork2.ActionSupport;
import org.springframework.web.client.RestTemplate;
import util.SpringIocUtil;
import util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fan on 6/30/2016.
 */
public class InitTestingAction extends ActionSupport {
    private MessageStore messageStore;
    private String message;
    private String json;
    private String page;
    @Override
    public String execute() throws Exception {
        messageStore = SpringIocUtil.getBean(MessageStore.class);
        message = messageStore.getMessage();

        Map<String, String> map = new HashMap<String, String>();
        map.put("success","true");
        json = JsonUtil.objectToJson(map);

        RestTemplate restTemplate = new RestTemplate();
        page = restTemplate.getForObject("http://www.baidu.com",String.class);

        return SUCCESS;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public void setMessageStore(MessageStore messageStore) {
        this.messageStore = messageStore;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
