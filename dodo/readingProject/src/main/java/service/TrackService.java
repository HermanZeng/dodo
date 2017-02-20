package service;

import dao.TrackDAO;
import dao.impl.*;
import entity.PullRequest;
import entity.Stage;
import entity.Track;
import exception.dao.ConflictException;
import exception.service.ExecuteException;
import exception.service.ForbiddenException;
import exception.service.IllegalInputException;
import exception.service.NotFoundException;
import observer.listener.PullRequestListener;
import observer.listener.impl.PullRequestAction;
import security.token.TokenManager;
import utilities.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by fan on 7/19/2016.
 */
public class TrackService {
    private TokenManager tokenManager;
    private TrackDAO trackDAO;
    private HibernateTrackCategoryDAO trackCategoryDAO = new HibernateTrackCategoryDAO();
    private HibernateUserTrackDAO userTrackDAO = new HibernateUserTrackDAO();
    private HibernateUserTrackStarDAO userTrackStarDAO = new HibernateUserTrackStarDAO();
    private HibernatePullRequestDAO pullRequestDAO = new HibernatePullRequestDAO();
    private HibernateBookshelfDAO bookshelfDAO = new HibernateBookshelfDAO();

    public Track addTrack(String token, Track track) throws IllegalInputException, ExecuteException {
        String userId = tokenManager.getUserId(token);
        /*
        *  1. this.init(userId, track);
        *  2. insert into mongo
        *  3. set originId
        *  4. insert into category table
        *  5. add into bookshelf
        *  6. insert into usertrack
        *
        * */
        Track newTrack = this.initTrack(userId, track);

        try {
            newTrack = trackDAO.save(newTrack);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("addTrack save track error: " + e.getMessage());
        }

        newTrack.setOriginId(newTrack.getId());

        this.saveCategories(newTrack);

        addToBookShelf(userId, newTrack);

        try {
            userTrackDAO.save(userId, newTrack.getId(), 1);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("addTrack save user track error: " + e.getMessage());
        }


        return newTrack;
    }

    public List<Track> listUsersTrack(String token, int option, int start, int count) throws ExecuteException {
        String userId = tokenManager.getUserId(token);


        /*
        *  1. find trackIds in mysql
        *  2. foreach trackId find in mongo
        *
        * */

        List<String> trackIds = null;
        if (option == 0) {
            try {
                trackIds = userTrackDAO.findAll(userId, start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("listUserTrack find trackIds error: " + e.getMessage());
            }
        } else if (option == 1) {
            try {
                trackIds = userTrackDAO.findPartial(userId, true, start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("listUserTrack find trackIds error: " + e.getMessage());
            }
        } else {
            try {
                trackIds = userTrackDAO.findPartial(userId, false, start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("listUserTrack find trackIds error: " + e.getMessage());
            }
        }
        System.out.println(trackIds);

        return this.bulkFind(trackIds);
    }

    public Track forkTrack(String token, String trackId) throws NotFoundException, ForbiddenException, ExecuteException {
        /*
        *  1. find originTrack by trackId
        *  2. check whether yourself
        *  3. save to mysql(user_track), check whether forked.
        *  4. new a track, call this.fork(originTrack, userId);
        *  5. save to mongo
        *  6. save to track_category tables
        *  7. originTrack forkCnt ++;
        *  8. update originTrack in mongo
        *
        * */
        String userId = tokenManager.getUserId(token);

        Track originTrack = null;
        try {
            originTrack = trackDAO.find(trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("forkTrack find track error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("forkTrack track not found: " + e.getMessage());
        }

        if (userId.equals(originTrack.getModifierId())) {
            throw new ForbiddenException("cannot fork yourself");
        }

        Track forkedTrack = this.fork(originTrack, userId);

        try {
            forkedTrack = trackDAO.save(forkedTrack);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("forkTrack save track error: " + e.getMessage());
        }

        try {
            userTrackDAO.save(userId, forkedTrack.getId(), 0);
        } catch (exception.dao.IllegalInputException e) {
            throw new ForbiddenException("already forked" + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            throw new ExecuteException("forkTrack save user track error: " + e.getMessage());
        }

        this.saveCategories(forkedTrack);

        addToBookShelf(userId, forkedTrack);

        originTrack.setForkCnt(originTrack.getForkCnt() + 1);

        try {
            trackDAO.save(originTrack);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("forkTrack update origin track error: " + e.getMessage());
        }


        return forkedTrack;
    }

    public void starTrack(String token, String trackId) throws NotFoundException, ForbiddenException, ExecuteException {
        /*
        *  1. find originTrack by trackId
        *  2. check whether yourself
        *  3. add to mysql(user_track_star table)
        *  4. originTrack.starCnt ++;
        *  5. update originTrack in mongo
        *
        *
        * */
        String userId = tokenManager.getUserId(token);

        Track originTrack = null;
        try {
            originTrack = trackDAO.find(trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("starTrack find track error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("starTrack track not found: " + e.getMessage());
        }

        if (userId.equals(originTrack.getModifierId())) {
            throw new ForbiddenException("cannot star yourself");
        }

        try {
            userTrackStarDAO.save(userId, originTrack.getId());
        } catch (exception.dao.IllegalInputException e) {
            throw new ForbiddenException("already starred: " + e.getMessage());
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("starTrack save user track star error: " + e.getMessage());
        }


        originTrack.setStarCnt(originTrack.getStarCnt() + 1);

        try {
            trackDAO.save(originTrack);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("starTrack update origin track error: " + e.getMessage());
        }

    }

    public void pullRequest(String token, String myTrackId) throws ForbiddenException, NotFoundException, ExecuteException {
        /*
        *  1. find track by myTrackId
        *  2. whether originId == modifierId(cannot pull request to the same person)
        *  3. check whether myTrackId belongs to the request user
        *  4. this.clone(myTrack);
        *  5. save to mongo(don't save user_track in mysql)
        *  6. add clonedTrackId, userId, myTrack.initiator, TimeUtil.getCurrentDate() to mysql pull_request table
        *  7. should not be add in track_category table?
        * */

        String userId = tokenManager.getUserId(token);
        Track myTrack = null;
        try {
            myTrack = trackDAO.find(myTrackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("pullRequest find track error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("pullRequest track not found: " + e.getMessage());
        }

        if (!userId.equals(myTrack.getModifierId())) {
            throw new ForbiddenException("not your track");
        }

        if (myTrack.getInitiatorId().equals(myTrack.getModifierId())) {
            throw new ForbiddenException("the creator is you");
        }

        Track imageTrack = this.clone(myTrack);

        try {
            imageTrack = trackDAO.save(imageTrack);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("pullRequest find track error: " + e.getMessage());
        }

        PullRequest pullRequest = new PullRequest();
        pullRequest.setInitiatorId(myTrack.getInitiatorId());
        pullRequest.setModifierId(userId);
        pullRequest.setTrackId(imageTrack.getId());

        try {
            pullRequestDAO.save(pullRequest);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("pullRequest save pull request table error: " + e.getMessage());
        }

        /*New added lines to implement messsage service*/
        PullRequestListener listener = new PullRequestAction();
        listener.doAction(pullRequest);
    }

    public void deleteTrack(String token, String trackId) throws NotFoundException, ForbiddenException, ExecuteException {
        /*
        *  1. check whether trackId belongs to the request user
        *  2. delete related in MySQL, category table, user_track
        *  3. delete track in mongo
        *
        * */
        String userId = tokenManager.getUserId(token);

        Track t = null;
        try {
            t = trackDAO.find(trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("deleteTrack find track error: " + e.getMessage());

        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("deleteTrack track not found: " + e.getMessage());
        }

        if (!userId.equals(t.getModifierId())) {
            throw new ForbiddenException("can only delete your track");
        }

        for (Integer c :
                t.getCategories()) {
            try {
                trackCategoryDAO.delete(c, trackId);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("deleteTrack delete track categories error: " + e.getMessage());
            }
        }

        try {
            userTrackDAO.delete(userId, trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("deleteTrack delete user track error: " + e.getMessage());
        }

        try {
            trackDAO.delete(trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("deleteTrack delete track error: " + e.getMessage());
        }

    }

    public Track editTrack(String token, String trackId, Track updatedTrack) throws IllegalInputException, ForbiddenException, NotFoundException, IllegalInputException, ExecuteException {
        /*
        *  1. get track in mongo
        *  2. check whether the trackId belongs to the request user
        *  3. set 除了 id, originId, initiator, modifier, createDate, starCnt, forkCnt 以外的attr
        *  4. update track in mongo
        *  5. delete old category table in mysql
        *  6. save new category table in mysql
        *
        * */
        String userId = tokenManager.getUserId(token);

        Track old = null;
        try {
            old = trackDAO.find(trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("editTrack find track error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("editTrack track not found: " + e.getMessage());
        }

        if (!userId.equals(old.getModifierId())) {
            throw new ForbiddenException("can only edit your track");
        }

        for (Integer c :
                old.getCategories()) {
            try {
                trackCategoryDAO.delete(c, trackId);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("editTrack delete old category info error: " + e.getMessage());
            }
        }

        old.setCategories(updatedTrack.getCategories());
        old.setTitle(updatedTrack.getTitle());
        old.setImage(updatedTrack.getImage());
        old.setStages(updatedTrack.getStages());

        try {
            old = trackDAO.save(old);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("editTrack save updated track error: " + e.getMessage());
        }

        this.saveCategories(old);

        addToBookShelf(userId, old);

        return old;
    }

    public List<Track> searchTracks(Integer categoryId, String title, int start, int count) throws ExecuteException {
        /*
        *  1. if categoryId == null, title == null, list tracks in mongo(start, count, order by starCnt)
        *  2. if categoryId == null, title != null, search in mongo(start, count, order by starCnt)
        *  3. if categoryId != null, title == null, list tracks in mysql category_x table(start, count, order by starCnt)
        *  4. if categoryId != null, title != null, search in mysql category_x table(start, count, order by starCnt)
        *
        * */
        List<Track> results = null;

        if (categoryId == null && title == null) {
            try {
                results = trackDAO.findAll(start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("searchTrack error: " + e.getMessage());
            }

        } else if (categoryId == null) {
            try {
                results = trackDAO.search(title, start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("searchTrack error: " + e.getMessage());
            }

        } else if (title == null) {
            List<String> trackIds = null;
            try {
                trackIds = trackCategoryDAO.findAll(categoryId, start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("searchTrack error: " + e.getMessage());
            }

            results = bulkFind(trackIds);

        } else {
            List<String> trackIds = null;
            try {
                trackIds = trackCategoryDAO.findByTitle(categoryId, title, start, count);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("searchTrack error: " + e.getMessage());
            }

            results = bulkFind(trackIds);
        }

        return results;
    }

    public Track showTrack(String trackId) throws NotFoundException, ExecuteException {
        Track track = null;

        try {
            track = trackDAO.find(trackId);
        } catch (exception.dao.ExecuteException e) {
            e.printStackTrace();
            throw new ExecuteException("show track error: " + e.getMessage());
        } catch (exception.dao.NotFoundException e) {
            throw new NotFoundException("track not found: " + e.getMessage());
        }

        return track;
    }

    private Track fork(Track origin, String modifierId) {
        Track newTrack = new Track();
        System.out.println(origin.getOriginId());
        System.out.println(origin.getOriginId());

        newTrack.setOriginId(origin.getOriginId());
        newTrack.setTitle(origin.getTitle());
        newTrack.setImage(origin.getImage());
        newTrack.setCategories(origin.getCategories());
        newTrack.setStages(origin.getStages());

        newTrack.setInitiatorId(origin.getInitiatorId());
        newTrack.setModifierId(modifierId);

        newTrack.setCreateDate(TimeUtil.getCurrentDate());

        newTrack.setForkCnt(0);
        newTrack.setStarCnt(0);

        return newTrack;
    }

    private Track clone(Track origin) {
        Track newTrack = new Track();

        newTrack.setOriginId(origin.getOriginId());
        newTrack.setTitle(origin.getTitle());
        newTrack.setImage(origin.getImage());
        newTrack.setCategories(origin.getCategories());
        newTrack.setStages(origin.getStages());

        newTrack.setInitiatorId(origin.getInitiatorId());
        newTrack.setModifierId(origin.getModifierId());

        newTrack.setCreateDate(origin.getCreateDate());

        newTrack.setForkCnt(origin.getForkCnt());
        newTrack.setStarCnt(origin.getStarCnt());

        newTrack.setCategories(origin.getCategories());
        newTrack.setStages(origin.getStages());

        return newTrack;
    }

    private Track initTrack(String userId, Track newTrack) {

        newTrack.setInitiatorId(userId);
        newTrack.setModifierId(userId);

        newTrack.setStarCnt(0);
        newTrack.setForkCnt(0);

        newTrack.setCreateDate(TimeUtil.getCurrentDate());

        return newTrack;
    }

    private List<Track> bulkFind(List<String> trackIds) {

        List<Track> results = new ArrayList<Track>();

        for (String tId :
                trackIds) {
            try {
                Track t = trackDAO.find(tId);
                results.add(t);
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("searchTrack error: " + e.getMessage());
            } catch (exception.dao.NotFoundException e) {
                // do nothing
            }
        }

        Collections.sort(results);

        return results;
    }

    private void saveCategories(Track newTrack) {
        for (Integer ref :
                newTrack.getCategories()) {
            try {
                trackCategoryDAO.save(ref, newTrack.getId(), newTrack.getTitle());
            } catch (exception.dao.ExecuteException e) {
                e.printStackTrace();
                throw new ExecuteException("save track category error: " + e.getMessage());
            }
        }
    }

    private void addToBookShelf(String userId, Track track) {
        for (Stage stage : track.getStages()
                ) {
            for (String bookId :
                    stage.getBooks()) {

                try {
                    bookshelfDAO.save(userId, bookId);
                } catch (exception.dao.ExecuteException e) {
                    e.printStackTrace();
                    throw new ExecuteException("addToBookShelf error: " + e.getMessage());
                } catch (ConflictException e) {
                    // do nothing
                } catch (exception.dao.NotFoundException e) {
                    // do nothing
                }
            }
        }
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public TrackDAO getTrackDAO() {
        return trackDAO;
    }

    public void setTrackDAO(TrackDAO trackDAO) {
        this.trackDAO = trackDAO;
    }
}
