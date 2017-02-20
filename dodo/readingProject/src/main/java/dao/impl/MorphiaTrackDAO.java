package dao.impl;


import com.mongodb.MongoClient;
import dao.TrackDAO;
import entity.Track;
import exception.dao.ExecuteException;
import exception.dao.NotFoundException;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by heming on 7/18/2016.
 */
public class MorphiaTrackDAO implements TrackDAO {

    private static final String dbName = "dodo";
    private static final String mapPackage = "entity.Track";
    private static Morphia morphia = new Morphia();
    private static Datastore datastore;

    static {

        morphia.mapPackage(mapPackage);

        try {
            datastore = morphia.createDatastore(new MongoClient(), dbName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new ExecuteException("init Morphia error: " + e.getMessage());
        }
        datastore.ensureIndexes();
    }


    @Override
    public Track save(Track entity) throws ExecuteException {

        datastore.save(entity);

        return entity;
    }

    @Override
    public void delete(String id) throws ExecuteException {
        datastore.delete(Track.class, new ObjectId(id));
    }

    @Override
    public Track update(String id, Track entity) throws ExecuteException, NotFoundException {

        entity.setId(new ObjectId(id));
        datastore.save(entity);

        return entity;
    }

    @Override
    public Track find(String id) throws ExecuteException, NotFoundException {
        Track track = datastore.get(Track.class, new ObjectId(id));

        if (track == null) {
            throw new NotFoundException("MorphiaTrackDAO track not found by id");
        }

        return track;
    }

    @Override
    public List<Track> findAll(int start, int count) throws ExecuteException {

        return datastore.createQuery(Track.class)
                .offset(start)
                .limit(count)
                .order("-forkCnt,-starCnt")
                .asList();
    }

    @Override
    public List<Track> search(String titleQuery, int start, int count) throws ExecuteException {

        Pattern pattern = Pattern.compile("^.*" + titleQuery + ".*$");

        return datastore.createQuery(Track.class)
                .filter("title", pattern)
                .order("-forkCnt,-starCnt")
                .offset(start)
                .limit(count)
                .asList();
    }

//    public static void main(String[] args) {
//        MorphiaTrackDAO dao = new MorphiaTrackDAO();
//        Track t = dao.find("57d559789e99c2f58cffafe8");
//        System.out.println(t);
//
////        List<Track> tracks = dao.findAll(0, 5);
////        System.out.println(tracks);
//    }
}
