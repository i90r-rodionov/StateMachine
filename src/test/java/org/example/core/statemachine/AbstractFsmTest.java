package org.example.core.statemachine;

import lombok.extern.slf4j.Slf4j;
import org.example.core.config.CoreConfig;
import org.example.core.config.StateMachineConfig;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.state.FsmState;
import org.example.domain.config.DomainConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.support.DefaultStateMachineContext;

@EnableAutoConfiguration
@SpringBootTest(classes = {CoreConfig.class, StateMachineConfig.class, DomainConfig.class})
@Slf4j
public abstract class AbstractFsmTest {

    @Autowired
    protected StateMachineFactory<FsmState, FsmEvent> stateMachineFactory;

    @MockBean(name = "checkReadyToPrint")
    protected Guard<FsmState, FsmEvent> checkReadyToPrint;

    @MockBean(name = "checkReadyToSign")
    protected Guard<FsmState, FsmEvent> checkReadyToSign;

    @MockBean( name = "checkSignedFiles")
    protected Guard<FsmState, FsmEvent> checkSignedFiles;

    @MockBean( name = "checkCreatedFolder")
    protected Guard<FsmState, FsmEvent> checkCreatedFolder;

    @MockBean( name = "checkMovedFiles")
    protected Guard<FsmState, FsmEvent> checkMovedFiles;

    @MockBean( name = "checkDeliveredFiles")
    protected Guard<FsmState, FsmEvent> checkDeliveredFiles;

    @MockBean( name = "checkResendFiles")
    protected Guard<FsmState, FsmEvent> checkResendFiles;

    @MockBean( name = "checkCreatedTaskInPega")
    protected Guard<FsmState, FsmEvent> checkCreatedTaskInPega;

    @MockBean( name = "checkCreatedTask")
    protected Guard<FsmState, FsmEvent> checkCreatedTask;

    @MockBean( name = "checkProcessedApplication")
    protected Guard<FsmState, FsmEvent> checkProcessedApplication;

    @MockBean( name = "checkSignedBank")
    protected Guard<FsmState, FsmEvent> checkSignedBank;

    @MockBean( name = "checkSla")
    protected Guard<FsmState, FsmEvent> checkSla;

    @MockBean(name = "saveStateAction")
    protected Action<FsmState, FsmEvent> saveStateAction;



    protected StateMachine<FsmState, FsmEvent> getStateMachine(FsmState fsmState) {
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