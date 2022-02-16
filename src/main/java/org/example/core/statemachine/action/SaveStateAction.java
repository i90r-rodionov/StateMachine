package org.example.core.statemachine.action;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component("saveStateAction")
public class SaveStateAction extends BaseAction {

    public SaveStateAction(StateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        super.execute(context);
    }
}
