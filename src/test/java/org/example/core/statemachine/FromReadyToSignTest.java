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

public class FromReadyToSignTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @BeforeEach
    void setUp() {

    }

    @Test
    void checkSignedFilesTrueTest() throws Exception {

        // READY_TO_SIGN -> CHECK_FILES_SIGNED(+) -> SIGNED

        Mockito.when(mockService.checkSignedFiles()).thenReturn(true);

        // from READY_TO_SIGN
        machine = getStateMachine(FsmState.READY_TO_SIGN);


        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(FsmEvent.SIGN)
                        .expectState(FsmState.SIGNED)
                        .expectStateChanged(1)

                        .and()
                        .build();
        plan.test();
    }

    @Test
    void checkSignedFilesFalseTest() throws Exception {

        // READY_TO_SIGN -> CHECK_FILES_SIGNED(-) -> BUSINESS_ERROR

        Mockito.when(mockService.checkSignedFiles()).thenReturn(false);

        // from READY_TO_SIGN
        machine = getStateMachine(FsmState.READY_TO_SIGN);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(FsmEvent.SIGN)
                        .expectState(FsmState.BUSINESS_ERROR)
                        .expectStateChanged(1)

                        .and()
                        .build();
        plan.test();
    }


}
