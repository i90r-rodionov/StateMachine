package org.example.core.statemachine.guard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

@Getter
@Setter
@NoArgsConstructor
public abstract class BaseGuard implements Guard<FsmState, FsmEvent> {

    private static long lastTime;

    private boolean flag;

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> stateContext) {
        LocalTime currentTime = LocalTime.now();
        long duration = currentTime.getLong(ChronoField.SECOND_OF_DAY) - lastTime;
        if (lastTime == 0) {
            lastTime = currentTime.getLong(ChronoField.SECOND_OF_DAY);
        }

        System.out.printf("%n   ??? Guard [%s] return [%s] : SM=[%s][%s] time=[%tT] duration=[%s]%n",
                this.getClass().getSimpleName(), flag,
                stateContext.getStateMachine().getUuid(), stateContext.getStateMachine().getState().getId(),
                currentTime, duration
        );
        return flag;
    }
}
