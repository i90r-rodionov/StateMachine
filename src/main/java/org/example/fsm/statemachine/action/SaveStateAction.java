package org.example.fsm.statemachine.action;

import org.example.fsm.statemachine.state.FsmState;
import org.example.fsm.statemachine.persist.StateHolder;
import org.example.fsm.statemachine.event.FsmEvent;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Service;


@Service("saveStateAction")
public class SaveStateAction extends BaseAction {

    public SaveStateAction(StateHolder stateHolder) {
        super(stateHolder);
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        super.execute(context);
    }
}
