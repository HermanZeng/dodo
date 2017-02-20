package observer.listener.impl;

import entity.Global;
import entity.PullRequest;
import observer.listener.PullRequestListener;
import service.MessageService;

/**
 * Created by heming on 9/11/2016.
 */
public class PullRequestAction implements PullRequestListener {
    private MessageService messageService = new MessageService();

    @Override
    public void doAction(PullRequest pullRequest) {
        String msg = "You are pulled request";
        messageService.addMessage(pullRequest.getInitiatorId(), msg, Global.PullRequest, pullRequest.getTrackId());
        return;
    }
}
