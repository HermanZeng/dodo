//package utilities;
//
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//
///**
// * Created by heming on 7/18/2016.
// */
//public class MongoUtil {
//    private final static String HOST = "localhost";// database host
//    private final static int PORT = 27017;// port
//    private final static int POOLSIZE = 100;// connections
//    private final static int BLOCKSIZE = 100; // waiting queue length
//    private final static String Database = "test";
//    private static MongoClient mongoClient = null;
//    private static MongoDatabase mongoDatabase = null;
//
//    static {
//        initDBProperties();
//    }
//
//    public static MongoDatabase getDatabase() {
//        return mongoDatabase;
//    }
//
//    public static MongoCollection<Document> getCollection(String collectionName){
//        MongoCollection<Document> collection = mongoDatabase.getCollection(collectionName);
//        return collection;
//    }
//
//    /**
//     * Initialize connection pool
//     */
//    private static void initDBProperties() {
//        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
//        builder.connectionsPerHost(POOLSIZE);
//        builder.threadsAllowedToBlockForConnectionMultiplier(BLOCKSIZE);
//        MongoClientOptions options = builder.build();
//        ServerAddress serverAddress = new ServerAddress(HOST, PORT);
//        mongoClient = new MongoClient(serverAddress, options);
//        mongoDatabase = mongoClient.getDatabase(Database);
//    }
//}
