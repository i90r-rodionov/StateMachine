package org.example.core.statemachine;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.mockito.ArgumentMatchers.any;

public class ReadyToSignTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @BeforeEach
    void setUp() {

    }

    @Test
    void targetReadyToSignTrueTest() throws Exception {

        Mockito.when(mockService.checkReadyToSign()).thenReturn(true);

        machine = getStateMachine(FsmState.READY_TO_PRINT);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(FsmEvent.CHECK_READY_TO_SIGN)
                        .expectState(FsmState.READY_TO_SIGN)
                        .expectStateChanged(1)

                        .and()
                        .build();
        plan.test();
    }

    @Test
    void targetReadyToSignFalseTest() throws Exception {

        Mockito.when(mockService.checkReadyToSign()).thenReturn(false);

        machine = getStateMachine(FsmState.READY_TO_PRINT);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(FsmEvent.CHECK_READY_TO_SIGN)
                        .expectState(FsmState.READY_TO_PRINT)
                        .expectStateChanged(0)

                        .and()
                        .build();
        plan.test();
    }

}
