package org.example.core.statemachine.guard;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import java.time.LocalTime;
import java.time.temporal.ChronoField;

@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public abstract class BaseGuard implements Guard<FsmState, FsmEvent> {

    private static long lastTime;

    private final CheckService checkService;

    private boolean flag;

    @Override
    public boolean evaluate(StateContext<FsmState, FsmEvent> stateContext) {
        LocalTime currentTime = LocalTime.now();
        long duration = currentTime.getLong(ChronoField.SECOND_OF_DAY) - lastTime;
        if (lastTime == 0) {
            lastTime = currentTime.getLong(ChronoField.SECOND_OF_DAY);
        }

        log.debug("   ??? Guard [{}] return [{}] : SM=[{}][{}] time=[{}] duration=[{}]",
                this.getClass().getSimpleName(), flag,
                stateContext.getStateMachine().getUuid(), stateContext.getStateMachine().getState().getId(),
                currentTime, duration
        );
        return flag;
    }

    protected boolean checkSla(StateContext<FsmState, FsmEvent> context) {
        return false;
    }

}
