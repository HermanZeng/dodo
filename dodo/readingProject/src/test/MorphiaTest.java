import com.alibaba.fastjson.JSON;
import com.mongodb.MongoClient;
import dao.TrackDAO;
import dao.impl.MorphiaTrackDAO;
import entity.Track;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by fan on 7/21/2016.
 */
public class MorphiaTest {
    public static void main(String[] args) {
        testDAO();

    }

    public static void testDAO() {
        TrackDAO trackDAO = new MorphiaTrackDAO();

//        List<Track> list = trackDAO.findAll(0,10);
//        System.out.println(list);

        List<Track> searchRes = trackDAO.search("的", 0, 10);
        System.out.println(searchRes);

//        String json = String.format("{   \"title\": \"踏上佛学这条道儿\",\n    \"image\": \"https://img3.doubanio.com/mpic/s1239642.jpg\",\n    \"initiator\": \"34478f8c-024a-40a1-9ec8-0328acab9034\",\n    \"modifier\": \"34478f8c-024a-40a1-9ec8-0328acab9034\",\n    \"create_date\": \"2016-07-14 10:57:48\",\n    \"fork_cnt\": 2349,\n    \"start_cnt\": 65231,\n    \"category\": [\n      1,\n      3\n    ],\n    \"stage\": [\n      {\n        \"honor_title\": \"初探佛门\",\n        \"seq\": 1,\n        \"books\": [\n          \"20ef09c3-f5f4-4b2a-88dc-49cf81cd7839\",\n          \"fji2193s-f5f4-4b2a-88dc-49cf81cd7839\"\n        ]\n      },\n      {\n        \"honor_title\": \"登堂入室\",\n        \"seq\": 2,\n        \"books\": [\n          \"c8f7dc24-2e81-427c-80da-b422f48cda73\"\n        ]\n      }\n    ]\n  }");
//        Track track = JSON.parseObject(json, Track.class);
//
//        track.setTitle("啦啦啦啦");
//        track.setForkCnt(0);
//
//        trackDAO.save(track);
//        System.out.println(track);

//        trackDAO.delete("57917afc799dbb512bc5cfa9");

//        String json2 = String.format("{   \"title\": \"踏上佛学这条道儿\",\n    \"image\": \"https://img3.doubanio.com/mpic/s1239642.jpg\",\n    \"initiator\": \"34478f8c-024a-40a1-9ec8-0328acab9034\",\n    \"modifier\": \"34478f8c-024a-40a1-9ec8-0328acab9034\",\n    \"create_date\": \"2016-07-14 10:57:48\",\n    \"fork_cnt\": 2349,\n    \"start_cnt\": 65231,\n    \"category\": [\n      1,\n      3\n    ],\n    \"stage\": [\n      {\n        \"honor_title\": \"初探佛门\",\n        \"seq\": 1,\n        \"books\": [\n          \"20ef09c3-f5f4-4b2a-88dc-49cf81cd7839\",\n          \"fji2193s-f5f4-4b2a-88dc-49cf81cd7839\"\n        ]\n      },\n      {\n        \"honor_title\": \"登堂入室\",\n        \"seq\": 2,\n        \"books\": [\n          \"c8f7dc24-2e81-427c-80da-b422f48cda73\"\n        ]\n      }\n    ]\n  }");
//        Track up = JSON.parseObject(json2, Track.class);
//        up.setId(new ObjectId("5791841c799de5e4a4891ea1"));
//        up.setTitle(" 美丽的剑桥");
//        up.setStarCnt(200);
//        up = trackDAO.update("5791841c799de5e4a4891ea4", up);

//        Track findRes = trackDAO.find("5791841c799de5e4a4891ea1");
//        System.out.println(findRes);

//        list = trackDAO.findAll(5,100);
//        System.out.println(list.size());

    }

    public static void rawTest() throws UnknownHostException {
        final Morphia morphia = new Morphia();

// tell Morphia where to find your classes
// can be called multiple times with different packages or classes
        morphia.mapPackage("entity.Track");

// create the Datastore connecting to the default port on the local host
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "test");
        datastore.ensureIndexes();

        String json = String.format("{   \"title\": \"踏上佛学这条道儿\",\n    \"image\": \"https://img3.doubanio.com/mpic/s1239642.jpg\",\n    \"initiator\": \"34478f8c-024a-40a1-9ec8-0328acab9034\",\n    \"modifier\": \"34478f8c-024a-40a1-9ec8-0328acab9034\",\n    \"create_date\": \"2016-07-14 10:57:48\",\n    \"fork_cnt\": 2349,\n    \"start_cnt\": 65231,\n    \"category\": [\n      1,\n      3\n    ],\n    \"stage\": [\n      {\n        \"honor_title\": \"初探佛门\",\n        \"seq\": 1,\n        \"books\": [\n          \"20ef09c3-f5f4-4b2a-88dc-49cf81cd7839\",\n          \"fji2193s-f5f4-4b2a-88dc-49cf81cd7839\"\n        ]\n      },\n      {\n        \"honor_title\": \"登堂入室\",\n        \"seq\": 2,\n        \"books\": [\n          \"c8f7dc24-2e81-427c-80da-b422f48cda73\"\n        ]\n      }\n    ]\n  }");
        Track track = JSON.parseObject(json, Track.class);

        track.setTitle("啦啦啦啦");
        track.setForkCnt(0);

        datastore.save(track);
        System.out.println(track);

        final Query<Track> query = datastore.createQuery(Track.class);
        final List<Track> tracks = query.asList();
        System.out.println(tracks);
    }
}
