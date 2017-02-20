package observer.listener.impl;

import dao.HonorDAO;
import dao.impl.HibernateHonorDAO;
import dao.impl.HibernateReadingProgressDAO;
import dao.impl.HibernateUserTrackDAO;
import dao.impl.MorphiaTrackDAO;
import entity.*;
import exception.dao.ExecuteException;
import exception.dao.IllegalInputException;
import observer.event.BookFinishedEvent;
import observer.listener.BookFinishedListener;
import service.MessageService;
import utilities.SpringIocUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heming on 9/8/2016.
 */
public class BookFinishedAction implements BookFinishedListener {
    private HibernateUserTrackDAO userTrackDAO = new HibernateUserTrackDAO();
    private MorphiaTrackDAO trackDAO = new MorphiaTrackDAO();
    private HibernateReadingProgressDAO readingProgressDAO = new HibernateReadingProgressDAO();
    private MessageService messageService = SpringIocUtil.getBean("messageService", MessageService.class);
    private HonorDAO honorDAO = new HibernateHonorDAO();

    @Override

    public void doAction(BookFinishedEvent event) {
        List<String> tracks = null;
        List<Track> trackList = new ArrayList<Track>();

        try {
            tracks = userTrackDAO.findAll(event.getUserId(), 0, Integer.MAX_VALUE);
            for (String trackId : tracks
                    ) {
                Track track = trackDAO.find(trackId);
                trackList.add(track);
            }

            if (trackList.isEmpty()) {
                return;
            }

            Loop:
            for (Track track : trackList
                    ) {
                for (Stage stage : track.getStages()
                        ) {
                    if (stage.getBooks() == null) {
                        break;
                    }
                    if (stage.getBooks().contains(event.getBookId())) {
                        for (String bookId : stage.getBooks()
                                ) {
                            ReadingProgress progress = readingProgressDAO.find(event.getUserId(), bookId);
                            if (!progress.getCurrent().equals(progress.getTotal())) {
                                break Loop;
                            }
                        }

                        String msg = "Congratulations! You unlock honor in track" + track.getTitle();
                        messageService.addMessage(event.getUserId(), msg, Global.Honor, track.getId());
                        Honor honor = new Honor();
                        honor.setBookId(event.getBookId());
                        honor.setUserId(event.getUserId());
                        honor.setTrackId(track.getId());
                        honor.setStageSeq(stage.getSeq());
                        honor.setTitle(stage.getHonorTitle());
                        honorDAO.save(honor);
                    }
                }
            }
        } catch (ExecuteException ee) {
            ee.printStackTrace();
            throw new ExecuteException("HibernateReadingProgressDAO error: BookFinishedListener.doAction failed " + ee.getMessage());
        } catch (IllegalInputException iie) {
            iie.printStackTrace();
            throw new IllegalInputException("MessageService error: BookFinishedListener.doAction failed " + iie.getMessage());
        }

    }
}
