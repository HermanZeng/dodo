package util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Book;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by fan on 7/1/2016.
 */
public final class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String objectToJson(Object object) {
        String ret = "";
        try {
            StringWriter result = new StringWriter();
            mapper.writeValue(result, object);
            ret = result.toString();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static <T> T jsonToObject(String str, Class<T> cl) {
        T t = null;
        try {
            t = mapper.readValue(str, cl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }
//
//    public static void main(String[] args) {
//        String s = "{\"id\":\"asdf-823f-jafi\",\"wid\":\"asdf-823f-jafi\",\"title\":\"红楼梦\",\"introduction\":\"门泊东吴万里船...\",\"publisher\":\"青年出版社\",\"pages\":\"3210\",\"isbn13\":\"9564287109387\",\"isbn10\":\"7365120923\",\"image\":\"www.baidu.com?id=sadf6\",\"rate\":8.7,\"category\":[{\"id\":3,\"description\":\"文学\",\"reference\":3}],\"authors\":[{\"id\":\"asu9823hfuh2\",\"introduction\":null,\"name\":\"刘慈欣\",\"nationality\":\"中\",\"image\":null}],\"translators\":null}\n";
//        Book book = jsonToObject(s, Book.class);
//        System.out.println(book);
//    }

}
