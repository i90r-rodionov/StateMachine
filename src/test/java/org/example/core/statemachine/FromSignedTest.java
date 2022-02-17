package org.example.core.statemachine;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.mockito.ArgumentMatchers.any;

public class FromSignedTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @BeforeEach
    void setUp() {

    }

    @Test
    void retryFalseTest() throws Exception {

        // SIGNED -> CHECK_FILES_SIGNED(-)/SLA(+) -> SLA_ERROR -> EXIT

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.checkCreatedFolder()).thenReturn(false);
        Mockito.when(mockService.checkSla()).thenReturn(true);

        machine = getStateMachine(null);

        int i = 0;



        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(10)
                        .stateMachine(machine)
                        .step()
                        .expectState(FsmState.CREATED)
                        .expectStateChanged(0)
                        .and()
                        .step()
                        .sendEvent(FsmEvent.CHECK_READY_TO_PRINT)
                        .expectState(FsmState.READY_TO_PRINT)
                        .expectStateChanged(1)

                        .and()
                        .build();
        plan.test();

    }

}
