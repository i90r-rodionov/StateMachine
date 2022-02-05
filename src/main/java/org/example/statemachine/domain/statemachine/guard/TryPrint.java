package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;


public class TryPrint implements Guard<FsmState, FsmEvent> {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {

        boolean flag = !false;

        if (!flag) {
//            context.getExtendedState().getVariables().put("EXIT", true);
            context.getStateMachine().sendEvent(FsmEvent.EXIT_EVENT);
        }

        System.out.println(String.format("### Guard TryPrint=[%s] exit=[%s]", flag, !flag));

        return flag;
    }
}
