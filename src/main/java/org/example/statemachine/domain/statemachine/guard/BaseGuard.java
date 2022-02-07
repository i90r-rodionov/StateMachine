package org.example.statemachine.domain.statemachine.guard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseGuard implements Guard<FsmState, FsmEvent> {

    private boolean flag;

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> stateContext) {
        System.out.println(String.format("   ??? Guard [%s] return [%s]", this.getClass().getSimpleName(), flag));
        return flag;
    }
}
