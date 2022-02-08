package org.example.fsm.statemachine.guard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseGuard implements Guard<FsmState, FsmEvent> {

    private boolean flag;

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> stateContext) {
        System.out.printf("%n   ??? Guard [%s] return [%s]%n", this.getClass().getSimpleName(), flag);
        return flag;
    }
}
