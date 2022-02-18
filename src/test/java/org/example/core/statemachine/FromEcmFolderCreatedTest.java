package org.example.core.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineMessageHeaders;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.example.core.statemachine.event.FsmEvent.TIMER_EVENT;

/**
 *
 */
@Slf4j
public class FromEcmFolderCreatedTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @Test
    void checkMovedFilesTrueTest() throws Exception {

        // common:
        // ECM_FOLDER_CREATED (onTimer)-> movedFiles ? ECM_SEND : (sla ? SLA_ERROR : ECM_FOLDER_CREATED(to retry))
        // test:
        // ECM_FOLDER_CREATED (onTimer)-> movedFiles(+)/sla(-) -> ECM_SEND

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkMovedFiles()).thenReturn(true);
        Mockito.when(mockService.checkSla()).thenReturn(false);


        //
        Message<FsmEvent> fsmEventMessage =
                MessageBuilder
                        .withPayload(TIMER_EVENT)
                        .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
                        .build();

        // from SIGNED
        machine = getStateMachine(FsmState.ECM_FOLDER);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(TIMER_EVENT)
                        .expectState(FsmState.ECM_FOLDER)
                        .expectStateChanged(1)
                        .expectTransition(2)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }


}
