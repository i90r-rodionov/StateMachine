package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;


public class AllSign implements Guard<FsmState, FsmEvent> {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = !false;

        if (!flag) {
//            context.getExtendedState().getVariables().put("EXIT", true);
//            context.getStateMachine().sendEvent(PurchaseEvent.EXIT_EVENT);
        }
        System.out.println(String.format("### Guard AllSign=[%s] exit=[%s]", flag, !flag));
        return flag;
    }
}
