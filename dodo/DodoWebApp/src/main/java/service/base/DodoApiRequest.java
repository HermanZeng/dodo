package service.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Created by fan on 7/1/2016.
 */
public class DodoApiRequest<T, R> {
    private static final Logger logger = LogManager.getLogger();
    private String url;
    private HttpMethod method;
    private HttpHeaders header;
    private T jsonBody;
    private Class<R> returnType;
    private ResponseEntity<R> response;
    private RestTemplate restTemplate;
    private Connection connection;

    public DodoApiRequest(Connection conn, String url, HttpMethod method, String path, T jsonBody, Class<R> returnType) {
        this.connection = conn;
        this.url = url + path;
        this.method = method;
        this.header = new HttpHeaders();
        this.jsonBody = jsonBody;
        this.returnType = returnType;
        this.restTemplate = new RestTemplate();

        this.response = null;

        if (connection != null && header.isEmpty()) {
            header.set("X-Auth-Token", conn.getToken());
        }
    }

    public R request() {
        logger.debug("request: url: "+url+" method: "+method.name());
        if (jsonBody == null) {
            logger.debug("body: null");
        } else {
            logger.debug("body: " + jsonBody.toString());
        }
        if (connection == null) {
            logger.debug("connection: null");
        } else {
            logger.debug("connection: " + connection);
        }
        if (header.get("X-Auth-Token") == null) {
            logger.debug("header: null");
        } else {
            logger.debug("header: " + (header.get("X-Auth-Token")).toString());

        }
        HttpEntity<?> entity = new HttpEntity<Object>(jsonBody, header);
        response = restTemplate.exchange(url, method, entity, returnType);
        logger.debug("response: " + response.getBody());

        return response.getBody();
    }
}
