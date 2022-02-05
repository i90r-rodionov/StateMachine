package org.example.statemachine.domain.statemachine.action;

import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service
public class EcmFolderAction extends SaveStateAction {

    public EcmFolderAction(StateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        System.out.println("### EcmFolderAction");
        boolean error = true;

        if (error) {
            throw new RuntimeException("EcmFolderAction Error");
        }
        super.execute(context);
    }
}
