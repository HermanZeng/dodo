package service;

import entity.Author;
import entity.Translator;
import org.springframework.http.HttpMethod;
import service.base.Connection;
import service.base.DodoApiRequest;

/**
 * Created by fan on 7/1/2016.
 */
public class TranslatorsService {
    private Connection connection;
    private String url;

    public TranslatorsService(String url, Connection connection) {
        this.url = url;
        this.connection = connection;
    }

    public List list(String q, int start, int count) {
        return new List(q, start, count);
    }


    public class List extends DodoApiRequest<Void, Translator[]> {
        public List(String q, int start, int count) {
            super(connection, url, HttpMethod.GET, "?q=" + q + "&start=" + start + "&count=" + count , null, Translator[].class);
        }
    }

}
