package org.example.statemachine.domain.statemachine.action;

import org.example.statemachine.domain.statemachine.guard.Flags;
import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service
public class CreateEcmFolderAction extends SaveStateAction {

    public CreateEcmFolderAction(StateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        boolean error = Flags.isCreateFolderFlag();

        if (error) {
            System.out.println("   ### CreateEcmFolderAction Error");
            throw new RuntimeException("EcmFolderAction Error");
        }
        System.out.println("   ### CreateEcmFolderAction");
        super.execute(context);
    }
}
