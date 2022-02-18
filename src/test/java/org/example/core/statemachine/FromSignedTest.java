package org.example.core.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

/**
 *
 */
@Slf4j
public class FromSignedTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @Test
    void checkCreatedFolderTrueTest() throws Exception {

        // common:
        // SIGNED (onTimer)-> createdFolder ? ECM_FOLDER_CREATED : (sla ? SLA_ERROR : SIGNED(to retry))
        // test:
        // SIGNED (onTimer)-> createdFolder(+)/sla(-) -> ECM_FOLDER_CREATED

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkCreatedFolder()).thenReturn(true);
        Mockito.when(mockService.checkSla()).thenReturn(false);

        // from SIGNED
        machine = getStateMachine(FsmState.SIGNED);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(timerFsmEventMessage)
                        .expectState(FsmState.ECM_FOLDER_CREATED)
                        .expectStateChanged(1)
                        .expectTransition(2)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }

    @Test
    void checkCreatedFolderFalseTest() throws Exception {

        // common:
        // SIGNED (onTimer)-> createdFolder ? ECM_FOLDER_CREATED : (sla ? SLA_ERROR : SIGNED(to retry))
        // test:
        // SIGNED (onTimer)-> createdFolder(-)/sla(-) -> SIGNED

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkCreatedFolder()).thenReturn(false);
        Mockito.when(mockService.checkSla()).thenReturn(false);

        // from SIGNED
        machine = getStateMachine(FsmState.SIGNED);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(timerFsmEventMessage)
                        .expectState(FsmState.SIGNED)
                        .expectStateChanged(0)
                        .expectTransition(0)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }

    @Test
    void checkCreatedFolderFalseSlaTrueTest() throws Exception {

        // common:
        // SIGNED (onTimer)-> createdFolder ? ECM_FOLDER_CREATED : (sla ? SLA_ERROR : SIGNED(to retry))
        // test:
        // SIGNED (onTimer)-> createdFolder(-)/sla(+) -> SLA_ERROR

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkCreatedFolder()).thenReturn(false);
        Mockito.when(mockService.checkSla()).thenReturn(true);

        // from SIGNED
        machine = getStateMachine(FsmState.SIGNED);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(timerFsmEventMessage)
                        .expectState(FsmState.SLA_ERROR)
                        .expectStateChanged(1)
                        .expectTransition(2)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }

}
