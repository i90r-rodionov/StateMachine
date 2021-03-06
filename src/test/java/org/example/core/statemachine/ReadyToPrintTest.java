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

public class ReadyToPrintTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @BeforeEach
    void setUp() {
        machine = getStateMachine(FsmState.CREATED);
    }

    @Test
    void targetReadyToPrintTrueTest() throws Exception {

        Mockito.when(mockService.checkReadyToPrint()).thenReturn(true);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(FsmEvent.CHECK_READY_TO_PRINT)
                        .expectState(FsmState.READY_TO_PRINT)
                        .expectStateChanged(1)

                        .and()
                        .build();

        plan.test();
    }

    @Test
    void targetReadyToPrintFalseTest() throws Exception {

        Mockito.when(mockService.checkReadyToPrint()).thenReturn(false);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(FsmEvent.CHECK_READY_TO_PRINT)
                        .expectState(FsmState.CREATED)
                        .expectStateChanged(0)

                        .and()
                        .build();

        plan.test();
    }

}
