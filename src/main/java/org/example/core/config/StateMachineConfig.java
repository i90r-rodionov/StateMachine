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
    private final Guard<FsmState, FsmEvent> checkDeliveredFiles;
    private final Guard<FsmState, FsmEvent> checkResendFiles;
    private final Guard<FsmState, FsmEvent> checkCreatedTaskInPega;
    private final Guard<FsmState, FsmEvent> checkCreatedTask;
    private final Guard<FsmState, FsmEvent> checkProcessedApplication;
    private final Guard<FsmState, FsmEvent> checkSignedBank;
    private final Guard<FsmState, FsmEvent> checkSla;

    private final Action<FsmState, FsmEvent> saveStateAction;
    private final Action<FsmState, FsmEvent> slaDefaultAction;
    private final Action<FsmState, FsmEvent> slaErrorAction;
    private final Action<FsmState, FsmEvent> createEcmFolderAction;
    private final Action<FsmState, FsmEvent> createEcmFolderErrorAction;
    private final Action<FsmState, FsmEvent> moveFilesToEcmFolderAction;
    private final Action<FsmState, FsmEvent> moveFilesToEcmFolderErrorAction;
    private final Action<FsmState, FsmEvent> createTaskInPegaAction;
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
                .state(FsmState.ECM_FOLDER)
                .state(ECM_SEND)
//                .choice(CHECK_FILES_ECM_SENT)
//                .state(FsmState.ECM_RECEIVE)
//                .state(FsmState.PEGA_SEND)
//                .choice(FsmState.CHECK_TASK_CREATED)
//                .state(FsmState.PEGA_RECEIVE)
//                .choice(FsmState.CHECK_PROCESSED)
//                .state(FsmState.WAIT_BANK_SIGN)
//                .choice(FsmState.CHECK_BANK_SIGN)
//                .state(FsmState.BANK_SIGNED)
                .state(FsmState.BUSINESS_ERROR)
                .state(SLA_ERROR)
        ;
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
                .guard(null)
                .action(saveStateAction)
                .and()

                // - to error
                // - to SIGNED
                .withChoice()
                .source(FsmState.CHECK_FILES_SIGNED)
                .first(SIGNED, checkSignedFiles, saveStateAction)
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
                .guard(checkCreatedFolderOrSla)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and()

                // next: SIGNED -> ECM_FOLDER
                .withLocal()
                .name("toEcmFolder")
                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER)
                //.timer(RETRY_DELAY)
                //.guard(checkCreatedFolder)
                .event(NEXT_EVENT)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and()

                // to SLA_ERROR on time
                .withLocal()
                .name("signToSla")
                .source(FsmState.SIGNED).target(SLA_ERROR)
                //.timer(getSlaForState(FsmState.SIGNED))
                .event(SLA_EVENT)
                //.guard(checkSla)
                .action(slaDefaultAction, slaErrorAction)

        ;


        // from ECM_FOLDER
        transitions
                // - to ECM_FOLDER (retry)
//                .withLocal()
//                .name("AAA")
//                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER)
//                .timerOnce(RETRY_START_DELAY)
//                .guard(checkCreatedFolder)
//                .action(createEcmFolderAction, createEcmFolderErrorAction)
//                .and()

                // ECM_FOLDER (?retry:?sla) -> (next:sla_error)
                .withInternal()
                .name("checkOnEcmFolder")
                .source(ECM_FOLDER)
                .timer(RETRY_DELAY)
                .guard(checkMovedFilesOrSla)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and()

                // next: SIGNED -> ECM_FOLDER
                .withLocal()
                .name("toEcmFolder")
                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER)
                //.timer(RETRY_DELAY)
                //.guard(checkCreatedFolder)
                .event(NEXT_EVENT)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and()

                // to SLA_ERROR on time
                .withLocal()
                .name("signToSla")
                .source(FsmState.SIGNED).target(SLA_ERROR)
                //.timer(getSlaForState(FsmState.SIGNED))
                .event(SLA_EVENT)
                .guard(checkSla)
                .action(slaDefaultAction, slaErrorAction)

        ;



        // - -(sla)-> SLA_ERROR
        // файлы переложены ? --> ECM_SEND : передлжить файлы в ECM (retry)
//        transitions
//                // to SLA_ERROR on time
//                .withLocal()
//                .source(ECM_FOLDER).target(SLA_ERROR)
//                .timer(getSlaForState(ECM_FOLDER))
//                .action(slaDefaultAction, slaErrorAction)
//                .and()
//
//                // - to ECM_SEND (retry)
//                .withLocal()
//                .source(ECM_FOLDER).target(ECM_SEND)
//                .timerOnce(RETRY_START_DELAY)
//                .guard(checkMovedFiles)
//                .action(moveFilesToEcmFolderAction, moveFilesToEcmFolderErrorAction)
//                .and()
//
//                .withLocal()
//                .source(ECM_FOLDER).target(ECM_SEND)
//                .timer(RETRY_DELAY)
//                .guard(checkMovedFiles)
//                .action(moveFilesToEcmFolderAction, moveFilesToEcmFolderErrorAction)
//                .and();

        /*
        // from ECM_SEND
        // - -(sla)-> SLA_ERROR
        // (ecm_callback) :
        // (check all files delivered) ? --> ECM_RECEIVE : (check reconsideration)
        // (check reconsideration) ? ECM_FOLDER : BUSINESS_ERROR
        transitions
                // to SLA_ERROR on time
                .withLocal()
                .source(ECM_SEND).target(SLA_ERROR)
                .timer(getSlaForState(ECM_SEND))
                .guard(checkSla)
                .action(slaDefaultAction, slaErrorAction)
                .and()

                .withExternal()
                .source(ECM_SEND).target(CHECK_FILES_ECM_SENT)
                .event(ECM_CALLBACK)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withChoice()
                .source(CHECK_FILES_ECM_SENT)
                .first(ECM_RECEIVE, checkDeliveredFiles, saveStateAction)
                .then(ECM_FOLDER, checkResendFiles, saveStateAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and();

        // from ECM_RECEIVE
        // - -(sla)-> SLA_ERROR
        // задача создана ? --> PEGA_SEND : создать задачу в PEGA (retry)
        transitions
                // to SLA_ERROR on time
                .withLocal()
                .source(ECM_RECEIVE).target(SLA_ERROR)
                .timer(getSlaForState(ECM_RECEIVE))
                .action(slaDefaultAction, slaErrorAction)
                .and()

                // - to PEGA_SEND (retry)
                .withLocal()
                .source(ECM_RECEIVE).target(PEGA_SEND)
                .timerOnce(RETRY_START_DELAY)
                .guard(checkCreatedTaskInPega)
                .action(createTaskInPegaAction, createTaskInPegaErrorAction)
                .and()

                .withLocal()
                .source(ECM_RECEIVE).target(FsmState.PEGA_SEND)
                .timer(RETRY_DELAY)
                .guard(checkCreatedTaskInPega)
                .action(createTaskInPegaAction, createTaskInPegaErrorAction)
                .and();

        // from PEGA_SEND
        // - -(sla)-> SLA_ERROR
        // - (pega_callback):
        // (task created) ? --> PEGA_RECEIVE : BUSINESS_ERROR
        transitions
                .withLocal()
                .source(PEGA_SEND).target(SLA_ERROR)
                .timer(getSlaForState(PEGA_SEND))
                .action(slaDefaultAction, slaErrorAction)
                .and()

                .withExternal()
                .source(PEGA_SEND).target(CHECK_TASK_CREATED)
                .event(PEGA_CALLBACK)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withChoice()
                .source(CHECK_TASK_CREATED)
                .first(PEGA_RECEIVE, checkCreatedTask, saveStateAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and();

        // from PEGA_RECEIVE
        // - -(sla)-> SLA_ERROR
        // - (pega_status):
        // (LA is processed) ? WAIT_BANK_SIGN : BUSINESS_ERROR
        transitions
                .withLocal()
                .source(PEGA_RECEIVE).target(SLA_ERROR)
                .timer(getSlaForState(PEGA_RECEIVE))
                .action(slaDefaultAction, slaErrorAction)
                .and()

                .withExternal()
                .source(PEGA_RECEIVE).target(CHECK_PROCESSED)
                .event(PEGA_STATUS)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withChoice()
                .source(CHECK_PROCESSED)
                .first(WAIT_BANK_SIGN, checkProcessedApplication, saveStateAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and();

        // from WAIT_BANK_SIGN
        // - -(sla)-> SLA_ERROR
        // - (pega_status):
        // (LA is signed) ? BANK_SIGNED : BUSINESS_ERROR
        transitions
                .withLocal()
                .source(WAIT_BANK_SIGN).target(SLA_ERROR)
                .timer(getSlaForState(WAIT_BANK_SIGN))
                .action(slaDefaultAction, slaErrorAction)
                .and()

                .withExternal()
                .source(WAIT_BANK_SIGN).target(CHECK_BANK_SIGN)
                .event(PEGA_STATUS)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withChoice()
                .source(CHECK_BANK_SIGN)
                .first(BANK_SIGNED, checkSignedBank, saveStateAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and();

        // from BANK_SIGNED
        // () --> EXIT
        transitions
                .withLocal()
                .source(BANK_SIGNED).target(EXIT)
                .action(exitAction);
*/

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
