package org.example.core.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineMessageHeaders;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;

import static org.example.core.statemachine.event.FsmEvent.NEXT_EVENT;
import static org.example.core.statemachine.event.FsmEvent.TIMER_EVENT;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
public class FromSignedTest extends AbstractFsmTest {

    private StateMachine<FsmState, FsmEvent> machine;

    @BeforeEach
    void setUp() {

    }

    @Test
    void retryFalseTest() throws Exception {

        // READY_TO_SIGN -> SIGNED -> CHECK_FILES_SIGNED(-)/SLA(+) -> SLA_ERROR -> EXIT

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.checkSignedFiles()).thenReturn(true);

        Mockito.when(mockService.checkCreatedFolder()).thenReturn(false);
        Mockito.when(mockService.checkSla()).thenReturn(false);


        Message<FsmEvent> fsmEventMessage =
                MessageBuilder
                        .withPayload(NEXT_EVENT)
                        .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
                        .build();


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
                        .step()
                        .sendEvent(fsmEventMessage)


                        .and()
                        .build();
        plan.test();

        log.debug( "SM = {}", machine);

    }

}
