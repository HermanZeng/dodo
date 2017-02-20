package observer.listener;

import observer.event.BookFinishedEvent;

/**
 * Created by heming on 9/8/2016.
 */
public interface BookFinishedListener {
    void doAction(BookFinishedEvent event);
}
