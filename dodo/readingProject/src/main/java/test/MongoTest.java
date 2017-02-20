/*
package test;

import com.alibaba.fastjson.JSON;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import entity.Stage;
import entity.Track;
import org.bson.Document;
import org.bson.types.ObjectId;
import utilities.MongoUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

*/
/**
 * Created by heming on 7/15/2016.
 *//*

public class MongoTest {
    public static void main(String args[]) {
        try {
            // 连接到 mongodb 服务
            //MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            //MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

            MongoDatabase mongoDatabase = MongoUtil.getDatabase();
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            */
/*Document document = new Document("title", "MongoDB").
                    append("description", "database").
                    append("likes", 100).
                    append("by", "Fly");
            List<Document> documents = new ArrayList<Document>();
            documents.add(document);
            collection.insertMany(documents);
            System.out.println("文档插入成功");*//*


            Track track = new Track();
            track.setForkCnt(1);
            Set<Stage> stages = new HashSet<Stage>();
            Stage stage = new Stage();
            stage.setHonorTitle("greenhand");
            stage.setSeq(1);
            stages.add(stage);
            track.setStages(stages);
            String json = JSON.toJSONString(track);

            Document document = Document.parse(json);
            //collection.insertOne(document);

            //FindIterable<Document> findIterable = collection.find();
            BasicDBObject object = new BasicDBObject("_id", new ObjectId("5788a7b248d9ef2eeca136d5"));
            FindIterable<Document> findIterable = collection.find(object);
            MongoCursor<Document> mongoCursor = findIterable.iterator();

            Document document1 = mongoCursor.next();
            */
/*Track track1 = JSON.parseObject(document1.toJson(),Track.class);
            System.out.println(JSON.toJSONString(track1));
            System.out.println(track1.toString());*//*


            */
/*while(mongoCursor.hasNext()){
                //System.out.println(mongoCursor.next());
                Document document1 = mongoCursor.next();
                //System.out.println(document1.toString());
                Track track1 = JSON.parseObject(document1.toJson(),Track.class);
                System.out.println(JSON.toJSONString(track1));
                System.out.println(track1.toString());
            }*//*


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
*/
