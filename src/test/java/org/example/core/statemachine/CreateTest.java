package org.example.core.statemachine;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.Test;
import org.springframework.statemachine.StateMachine;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateTest extends AbstractFsmTest {

    @Test
    void initTest() {
        StateMachine<FsmState, FsmEvent> machine = getStateMachine(null);
        assertEquals(FsmState.CREATED, machine.getState().getId());
    }

    @Test
    void resetMachineTest() {
        StateMachine<FsmState, FsmEvent> machine = getStateMachine(FsmState.READY_TO_SIGN);
        assertEquals(FsmState.READY_TO_SIGN, machine.getState().getId());
    }

}
