package org.example.core.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.example.core.config.CoreConfig;
import org.example.core.config.StateMachineConfig;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.example.domain.config.DomainConfig;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineMessageHeaders;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import static org.example.core.statemachine.event.FsmEvent.TIMER_EVENT;

@EnableAutoConfiguration
@SpringBootTest(classes = {CoreConfig.class, StateMachineConfig.class, DomainConfig.class})
@Slf4j
public abstract class AbstractFsmTest {

    protected AnnotationConfigApplicationContext context;

    @Autowired
    protected StateMachineFactory<FsmState, FsmEvent> stateMachineFactory;

    @MockBean
    protected CheckService mockService;

    @MockBean(name = "saveStateAction")
    protected Action<FsmState, FsmEvent> saveStateAction;

    protected Message<FsmEvent> timerFsmEventMessage = MessageBuilder
            .withPayload(TIMER_EVENT)
            .setHeader(StateMachineMessageHeaders.HEADER_DO_ACTION_TIMEOUT, 5000)
            .build();


    @BeforeEach
    void setUp() {
        context = buildContext();

        Mockito.when(mockService.defaultAction()).thenReturn(true);
        Mockito.when(mockService.defaultErrorAction()).thenReturn(true);
    }

    protected AnnotationConfigApplicationContext buildContext() {
        return null;
    }


    protected StateMachine<FsmState, FsmEvent> getStateMachine(FsmState fsmState) {
        log.debug("getStateMachine with state[{}]", fsmState);
        final StateMachine<FsmState, FsmEvent> stateMachine = stateMachineFactory.getStateMachine();
        final StateMachineContext<FsmState, FsmEvent> context = getCxt(fsmState);

        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor().doWithAllRegions(access -> access.resetStateMachine(context));
        stateMachine.startReactively().block();
        log.debug("Reset machine [{}] to [{}]", stateMachine.getUuid(), stateMachine.getState().getId());
        return stateMachine;
    }

    private StateMachineContext<FsmState, FsmEvent> getCxt(FsmState inState) {
        StateMachineContext<FsmState, FsmEvent> context = new DefaultStateMachineContext<>(inState, null, null, null);
        return context;
    }

}