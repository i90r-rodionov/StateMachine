package org.example.core.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.example.core.statemachine.state.FsmState.SLA_ERROR;

/**
 *
 */
@Slf4j
public class FromEcmFolderCreatedTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @Test
    void checkMovedFilesTrueTest() throws Exception {

        // ECM_FOLDER_CREATED (onTimer)-> movedFiles(+)/sla(-) -> ECM_SEND

        Mockito.when(mockService.checkMovedFiles()).thenReturn(true);
        Mockito.when(mockService.checkSla()).thenReturn(false);

        machine = getStateMachine(FsmState.ECM_FOLDER_CREATED);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(timerFsmEventMessage)
                        .expectState(FsmState.ECM_SEND)
                        .expectStateChanged(1)
                        .expectTransition(2)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);
    }

    @Test
    void checkMovedFilesFalseTest() throws Exception {

        // ECM_FOLDER_CREATED (onTimer)-> movedFiles(-)/sla(-) -> ECM_FOLDER_CREATED

        Mockito.when(mockService.checkMovedFiles()).thenReturn(false);
        Mockito.when(mockService.checkSla()).thenReturn(false);

        machine = getStateMachine(FsmState.ECM_FOLDER_CREATED);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(timerFsmEventMessage)
                        .expectState(FsmState.ECM_FOLDER_CREATED)
                        .expectStateChanged(0)
                        .expectTransition(0)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);
    }

    @Test
    void checkSlaTrueTest() throws Exception {

        // ECM_FOLDER_CREATED (onTimer)-> movedFiles(-)/sla(+) -> SLA_ERROR

        Mockito.when(mockService.checkMovedFiles()).thenReturn(false);
        Mockito.when(mockService.checkSla()).thenReturn(true);

        machine = getStateMachine(FsmState.ECM_FOLDER_CREATED);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(timerFsmEventMessage)
                        .expectState(SLA_ERROR)
                        .expectStateChanged(1)
                        .expectTransition(2)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);
    }


}
