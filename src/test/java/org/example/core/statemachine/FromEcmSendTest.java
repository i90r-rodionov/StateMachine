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

import static org.example.core.statemachine.event.FsmEvent.ECM_CALLBACK;
import static org.example.core.statemachine.event.FsmEvent.TIMER_EVENT;

/**
 *
 */
@Slf4j
public class FromEcmSendTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @Test
    void checkSlaTrueTest() throws Exception {

        // ECM_SEND: sla(+)? SLA_ERROR

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkSla()).thenReturn(true);
        //
        Message<FsmEvent> fsmEventMessage =
                MessageBuilder
                        .withPayload(TIMER_EVENT)
                        .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
                        .build();

        // from ECM_SEND
        machine = getStateMachine(FsmState.ECM_SEND);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(TIMER_EVENT)
                        .expectState(FsmState.SLA_ERROR)
                        .expectStateChanged(1)
                        .expectTransition(2)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }

    @Test
    void checkDeliveredFilesTrueTest() throws Exception {

        // ECM_SEND -> sla(-)? : deliveredFiles(+) ? ECM_RECEIVE

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkSla()).thenReturn(false);
        Mockito.when(mockService.checkDeliveredFiles()).thenReturn(true);

        machine = getStateMachine(FsmState.ECM_SEND);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(ECM_CALLBACK)
                        .expectState(FsmState.ECM_RECEIVE)
                        .expectStateChanged(1)
                        .expectTransition(1)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);
    }

    @Test
    void checkResendFilesTrueTest() throws Exception {

        // ECM_SEND -> sla(-), deliveredFiles(-), resendFiles(+) ? ECM_FOLDER_CREATED

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);

        Mockito.when(mockService.checkSla()).thenReturn(false);
        Mockito.when(mockService.checkDeliveredFiles()).thenReturn(false);
        Mockito.when(mockService.checkResendFiles()).thenReturn(true);

        machine = getStateMachine(FsmState.ECM_SEND);

        StateMachineTestPlan<FsmState, FsmEvent> plan =
                StateMachineTestPlanBuilder.<FsmState, FsmEvent>builder()
                        .defaultAwaitTime(1)
                        .stateMachine(machine)
                        .step()
                        .sendEvent(ECM_CALLBACK)
                        .expectState(FsmState.ECM_FOLDER_CREATED)
                        .expectStateChanged(1)
                        .expectTransition(1)

                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }


}
