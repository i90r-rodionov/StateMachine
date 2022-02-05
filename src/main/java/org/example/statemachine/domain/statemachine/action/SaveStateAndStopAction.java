package org.example.statemachine.domain.statemachine.action;

import org.example.statemachine.domain.statemachine.persist.StateHolder;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service
public class SaveStateAndStopAction extends SaveStateAction {

    public SaveStateAndStopAction(StateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        super.execute(context);
        context.getStateMachine().sendEvent(FsmEvent.EXIT_EVENT);
    }
}
