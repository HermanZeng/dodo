package test;

import observer.event.BookFinishedEvent;
import observer.listener.BookFinishedListener;
import observer.listener.impl.BookFinishedAction;

/**
 * Created by heming on 9/12/2016.
 */
public class NewMessageTest {
    public static void main(String args[]) {
        BookFinishedListener listener=new BookFinishedAction();
        BookFinishedEvent event = new BookFinishedEvent();
        event.setUserId("58bc8a7d-469e-11e6-bf08-208984f5a994");
        event.setBookId("00057122-7739-11e6-8b8d-a088698e71d1");
        listener.doAction(event);
    }
}
