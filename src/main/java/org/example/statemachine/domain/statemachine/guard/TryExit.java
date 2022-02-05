package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;


public class TryExit implements Guard<FsmState, FsmEvent> {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {

        boolean flag = context.getTransition().getTrigger().getEvent() == FsmEvent.EXIT_EVENT;

        System.out.println(String.format("### Guard [%s] with event [%s] result [%s]",
                this.getClass().getSimpleName(), context.getTransition().getTrigger().getEvent(), flag));

        return flag;
    }
}
