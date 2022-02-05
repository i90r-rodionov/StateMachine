package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;


public class TaskChoice implements Guard<FsmState, FsmEvent> {

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = false;

        System.out.println(String.format("### Guard [%s] exit=[%s]", this.getClass().getSimpleName(), flag));
        return flag;
    }
}
