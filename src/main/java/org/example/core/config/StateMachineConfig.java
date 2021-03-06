package org.example.core.config;

import lombok.RequiredArgsConstructor;
import org.example.core.statemachine.StateMachineScanMarker;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.listener.FsmApplicationListener;
import org.example.core.statemachine.persist.FsmPersister;
import org.example.core.statemachine.state.FsmState;
import org.example.domain.DomainScanMarker;
import org.example.domain.repository.LoanApplicationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

import static org.example.core.statemachine.event.FsmEvent.*;
import static org.example.core.statemachine.state.FsmState.*;

@Configuration
@ComponentScan(basePackageClasses = {StateMachineScanMarker.class, DomainScanMarker.class})
@EnableStateMachineFactory(name = "stateMachineFactory")
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<FsmState, FsmEvent> {

    // Задержка для потворяющихся действий
    public static final long RETRY_DELAY = 3000L;

    private final LoanApplicationRepository repository;

    private final Guard<FsmState, FsmEvent> checkReadyToPrint;
    private final Guard<FsmState, FsmEvent> checkReadyToSign;
    private final Guard<FsmState, FsmEvent> checkSignedFiles;
    private final Guard<FsmState, FsmEvent> checkCreatedFolderOrSla;
    private final Guard<FsmState, FsmEvent> checkMovedFilesOrSla;
    private final Guard<FsmState, FsmEvent> checkEcmSendSla;


    private final Guard<FsmState, FsmEvent> checkDeliveredFiles;
    private final Guard<FsmState, FsmEvent> checkResendFiles;
    private final Guard<FsmState, FsmEvent> checkCreatedTaskInPega;
    private final Guard<FsmState, FsmEvent> checkCreatedTask;
    private final Guard<FsmState, FsmEvent> checkProcessedApplication;
    private final Guard<FsmState, FsmEvent> checkSignedBank;
    private final Guard<FsmState, FsmEvent> checkSla;


    private final Action<FsmState, FsmEvent> onCheckAction;
    private final Action<FsmState, FsmEvent> onCheckErrorAction;

    private final Action<FsmState, FsmEvent> saveStateAction;
    private final Action<FsmState, FsmEvent> slaDefaultAction;
    private final Action<FsmState, FsmEvent> slaErrorAction;

    private final Action<FsmState, FsmEvent> onSignAction;
    private final Action<FsmState, FsmEvent> onCreateFolderAction;
    private final Action<FsmState, FsmEvent> onCreateFolderErrorAction;
    private final Action<FsmState, FsmEvent> onEcmSendAction;
    private final Action<FsmState, FsmEvent> onEcmSendErrorAction;

    private final Action<FsmState, FsmEvent> onDeliveredAction;

    private final Action<FsmState, FsmEvent> createTaskInPegaErrorAction;
    private final Action<FsmState, FsmEvent> exitAction;

    @Override
    public void configure(StateMachineConfigurationConfigurer<FsmState, FsmEvent> config) throws Exception {
        config
                .withConfiguration()
                .machineId("machineId")
                .autoStartup(true)
                .listener(new FsmApplicationListener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<FsmState, FsmEvent> states) throws Exception {
        states
                .withStates()
                .initial(CREATED)
                .end(FsmState.EXIT)
                .state(FsmState.CREATED)
                .state(FsmState.READY_TO_PRINT)
                .state(FsmState.READY_TO_SIGN)
                .choice(FsmState.CHECK_FILES_SIGNED)
                .state(FsmState.SIGNED)
                .state(FsmState.ECM_FOLDER_CREATED)
                .state(ECM_SEND)
                .choice(CHECK_FILES_DELIVERED)
                .choice(CHECK_FILES_RESEND)
                .state(FsmState.ECM_RECEIVE)
//                .state(FsmState.PEGA_SEND)
//                .choice(FsmState.CHECK_TASK_CREATED)
//                .state(FsmState.PEGA_RECEIVE)
//                .choice(FsmState.CHECK_PROCESSED)
//                .state(FsmState.WAIT_BANK_SIGN)
//                .choice(FsmState.CHECK_BANK_SIGN)
//                .state(FsmState.BANK_SIGNED)
                .state(FsmState.BUSINESS_ERROR)
                .state(SLA_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<FsmState, FsmEvent> transitions) throws Exception {

        // to READY_TO_PRINT
        transitions
                .withExternal()
                .source(CREATED).target(READY_TO_PRINT)
                .event(CHECK_READY_TO_PRINT)
                .guard(checkReadyToPrint)
                .action(saveStateAction);

        // to READY_TO_SIGN
        transitions
                .withExternal()
                .source(READY_TO_PRINT).target(READY_TO_SIGN)
                .event(CHECK_READY_TO_SIGN)
                .guard(checkReadyToSign)
                .action(saveStateAction);

        // from READY_TO_SIGN
        transitions
                .withExternal()
                .source(READY_TO_SIGN).target(CHECK_FILES_SIGNED)
                .event(SIGN)
                .action(saveStateAction)
                .and()

                // - to error
                // - to SIGNED
                .withChoice()
                .source(FsmState.CHECK_FILES_SIGNED)
                .first(SIGNED, checkSignedFiles, onSignAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and();

        // from SIGNED
        // - -(sla)-> SLA_ERROR
        // - папка создана ? --> ECM_FOLDER : создание папки в ECM (retry)
        transitions
                // SIGNED(?retry:?sla) -> (next:sla_error)
                .withInternal()
                .name("checkOnSigned")
                .source(SIGNED)
                .timer(RETRY_DELAY)
                .event(TIMER_EVENT)
                .guard(checkCreatedFolderOrSla)
                .action(onCheckAction, onCheckErrorAction)
                .and()

                // next: SIGNED -> ECM_FOLDER
                .withLocal()
                .name("toEcmFolder")
                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER_CREATED)
                .event(NEXT_EVENT)
                .action(onCreateFolderAction, onCreateFolderErrorAction)
                .and()

                // to SLA_ERROR on time
                .withLocal()
                .name("signToSla")
                .source(FsmState.SIGNED).target(SLA_ERROR)
                .event(SLA_EVENT)
                .action(slaDefaultAction, slaErrorAction);


        // from ECM_FOLDER_CREATED
        transitions
                // ECM_FOLDER_CREATED (?retry:?sla) -> (next:sla_error)
                .withInternal()
                .name("checkOnEcmFolder")
                .source(ECM_FOLDER_CREATED)
                .timer(RETRY_DELAY)
                .event(TIMER_EVENT)
                .guard(checkMovedFilesOrSla)
                .action(onCheckAction, onCheckErrorAction)
                .and()

                // next: ECM_FOLDER_CREATED -> ECM_SEND
                .withLocal()
                .name("toEcmSend")
                .source(ECM_FOLDER_CREATED).target(ECM_SEND)
                .event(NEXT_EVENT)
                .action(onEcmSendAction, onEcmSendErrorAction)
                .and()

                // to SLA_ERROR on time
                .withLocal()
                .name("signToSla")
                .source(ECM_FOLDER_CREATED).target(SLA_ERROR)
                .event(SLA_EVENT)
                .guard(checkSla)
                .action(slaDefaultAction, slaErrorAction);

        // from ECM_SEND
        transitions
                // ECM_FOLDER (?sla) -> (sla_error)
                .withInternal()
                .name("checkOnEcmSend")
                .source(ECM_SEND)
                .timer(RETRY_DELAY)
                .event(TIMER_EVENT)
                .guard(checkEcmSendSla)
                .action(onCheckAction, onCheckErrorAction)
                .and()

                .withExternal()
                .source(ECM_SEND).target(CHECK_FILES_DELIVERED)
                .event(ECM_CALLBACK)
                //.action(saveStateAction)
                .and()

                // - to ECM_RECEIVE
                // - to CHECK_FILES_RESEND
                .withChoice()
                .source(CHECK_FILES_DELIVERED)
                .first(ECM_RECEIVE, checkDeliveredFiles, onDeliveredAction, onCreateFolderErrorAction)
                .last(CHECK_FILES_RESEND)
                .and()

                // - to ECM_FOLDER_CREATED
                // - to BUSINESS_ERROR
                .withChoice()
                .source(CHECK_FILES_RESEND)
                .first(ECM_FOLDER_CREATED, checkResendFiles, onCreateFolderAction, onCreateFolderErrorAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and()

                // to SLA_ERROR on time
                .withLocal()
                .name("ecmSendToSla")
                .source(ECM_SEND).target(SLA_ERROR)
                .event(SLA_EVENT)
                .guard(checkSla)
                .action(slaDefaultAction, slaErrorAction);



        // to EXIT
        transitions
/*
                .withLocal()
                .source(FsmState.CREATED).target(EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withLocal()
                .source(FsmState.READY_TO_PRINT).target(EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withLocal()
                .source(FsmState.READY_TO_SIGN).target(EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withLocal()
                .source(FsmState.ECM_FOLDER).target(EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(null)
                .action(saveStateAction)
                .and()
*/
                // from ERROR to EXIT
                .withLocal()
                .source(BUSINESS_ERROR).target(EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .action(saveStateAction)
                .and()

                .withLocal()
                .source(SLA_ERROR).target(EXIT)
                .timer(180000L)
                .action(saveStateAction);


    }


    @Bean
    public StateMachinePersister<FsmState, FsmEvent, String> persister() {
        return new DefaultStateMachinePersister<>(new FsmPersister());
    }

    private long getSlaForState(FsmState signed) {
        return 180_000L;
    }


}
