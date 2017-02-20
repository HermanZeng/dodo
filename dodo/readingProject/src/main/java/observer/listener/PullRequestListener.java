package observer.listener;

import entity.PullRequest;

/**
 * Created by heming on 9/11/2016.
 */
public interface PullRequestListener {
    void doAction(PullRequest pullRequest);
}
