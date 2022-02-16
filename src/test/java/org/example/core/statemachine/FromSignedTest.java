package org.example.core.statemachine;

import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.mockito.ArgumentMatchers.any;

public class FromSignedTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @BeforeEach
    void setUp() {
        // from SIGNED
        machine = getStateMachine(FsmState.SIGNED);
    }

    @Test
    void checkCreatedFolderTrueTest() throws Exception {

        // SIGNED -> CHECK_FILES_SIGNED(+) -> ECM_FOLDER

        Mockito.when(checkSla.evaluate(any(StateContext.class))).thenReturn(true);

        int i = 0;

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(10)
                        .stateMachine(machine)
                        .step()
                        //.expectState(FsmState.ECM_FOLDER)
                        //.expectStateChanged(1)

                        .and()
                        .build();
        plan.test();

    }

}
