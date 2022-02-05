package org.example.statemachine.domain.statemachine.guard;

import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.util.Set;


public class TrySign implements Guard<FsmState, FsmEvent> {

    Set<FsmEvent> signEvents = Set.of(FsmEvent.TRY_SIGN, FsmEvent.SIGN_EVENT);

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> context) {
        boolean flag = signEvents.contains(context.getTransition().getTrigger().getEvent());
        System.out.println(String.format("   ### Guard [%s] with event [%s] result [%s]",
                this.getClass().getSimpleName(),
                context.getTransition().getTrigger().getEvent(), flag));

        return flag;
    }
}
