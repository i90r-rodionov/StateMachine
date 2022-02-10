package org.example.fsm.statemachine.config;

import lombok.RequiredArgsConstructor;
import org.example.domain.repository.LoanApplicationRepository;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.listener.FsmApplicationListener;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import static org.example.fsm.statemachine.event.FsmEvent.*;
import static org.example.fsm.statemachine.state.FsmState.*;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<FsmState, FsmEvent> {

    private LoanApplicationRepository repository;

    // Задержка для потворяющихся действий
    public static final long RETRY_DELAY = 15000L;
    // Задержка для первой попытки повтора
    public static final long RETRY_START_DELAY = 1000L;

    private final Action<FsmState, FsmEvent> createAction;
    private final Guard<FsmState, FsmEvent> checkReadyToPrint;
    private final Action<FsmState, FsmEvent> saveStateAction;
    private final Guard<FsmState, FsmEvent> checkReadyToSign;
    private final Action<FsmState, FsmEvent> slaErrorAction;
    private final Guard<FsmState, FsmEvent> checkFilesSigned;
    private final Guard<FsmState, FsmEvent> checkCreateEcmFolder;
    private final Action<FsmState, FsmEvent> createEcmFolderAction;
    private final Action<FsmState, FsmEvent> createEcmFolderErrorAction;
    private final Guard<FsmState, FsmEvent> checkMoveFilesToEcmFolder;
    private final Action<FsmState, FsmEvent> moveFilesToEcmFolderAction;
    private final Action<FsmState, FsmEvent> moveFilesToEcmFolderErrorAction;
    private final Guard<FsmState, FsmEvent> checkFilesDelivered;
    private final Guard<FsmState, FsmEvent> checkFilesResend;
    private final Guard<FsmState, FsmEvent> checkCreateTaskInPega;
    private final Action<FsmState, FsmEvent> createTaskInPegaAction;
    private final Action<FsmState, FsmEvent> createTaskInPegaErrorAction;

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
                .initial(INIT)
                .end(EXIT)
                .state(CREATED)
                .state(READY_TO_PRINT)
                .state(READY_TO_SIGN)
                .choice(CHECK_FILES_SIGNED)
                .state(SIGNED)
                .state(ECM_FOLDER)
                .state(ECM_SEND)
                .choice(CHECK_FILES_ECM_SENT)
                .state(ECM_RECEIVE)
                .state(PEGA_SEND)
                .state(PEGA_RECEIVE)
                .state(BUSINESS_ERROR)
                .state(SLA_ERROR);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<FsmState, FsmEvent> transitions) throws Exception {
        // to CREATE
        transitions
                .withLocal()
                .source(INIT).target(CREATED)
                .event(FsmEvent.CREATE)
                .guard(null)
                .action(createAction);
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
                .first(SIGNED, checkFilesSigned, saveStateAction)
                .last(BUSINESS_ERROR, null, saveStateAction)
                .and();

        // from SIGNED
        // - -(sla)-> SLA_ERROR
        // - папка создана ? --> ECM_FOLDER : создание папки в ECM (retry)
        transitions
                // to SLA_ERROR on time
                .withLocal()
                .source(FsmState.SIGNED).target(SLA_ERROR)
                .action(slaErrorAction)
                .timer(getSlaForState(FsmState.SIGNED))
                .and()

                // - to ECM_FOLDER (retry)
                .withLocal()
                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER)
                .timerOnce(RETRY_START_DELAY)
                .guard(checkCreateEcmFolder)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and()

                .withLocal()
                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER)
                .timer(RETRY_DELAY)
                .guard(checkCreateEcmFolder)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and();

        // from ECM_FOLDER
        // - -(sla)-> SLA_ERROR
        // файлы переложены ? --> ECM_SEND : передлжить файлы в ECM (retry)
        transitions
                // to SLA_ERROR on time
                .withLocal()
                .source(ECM_FOLDER).target(SLA_ERROR)
                .action(slaErrorAction)
                .timer(getSlaForState(ECM_FOLDER))
                .and()

                // - to ECM_SEND (retry)
                .withLocal()
                .source(ECM_FOLDER).target(FsmState.ECM_SEND)
                .timerOnce(RETRY_START_DELAY)
                .guard(checkMoveFilesToEcmFolder)
                .action(moveFilesToEcmFolderAction, moveFilesToEcmFolderErrorAction)
                .and()

                .withLocal()
                .source(ECM_FOLDER).target(FsmState.ECM_SEND)
                .timer(RETRY_DELAY)
                .guard(checkMoveFilesToEcmFolder)
                .action(moveFilesToEcmFolderAction, moveFilesToEcmFolderErrorAction)
                .and();

        // from ECM_SEND
        // - -(sla)-> SLA_ERROR
        // (ecm_callback) :
        // (check all files delivered) ? --> ECM_RECEIVE : (check reconsideration)
        // (check reconsideration) ? ECM_FOLDER : BUSINESS_ERROR
        transitions
                // to SLA_ERROR on time
                .withLocal()
                .source(ECM_SEND).target(SLA_ERROR)
                .action(slaErrorAction)
                .timer(getSlaForState(ECM_SEND))
                .and()

                .withExternal()
                .source(ECM_SEND).target(CHECK_FILES_ECM_SENT)
                .event(ECM_CALLBACK)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withChoice()
                .source(CHECK_FILES_ECM_SENT)
                .first(ECM_RECEIVE, checkFilesDelivered, saveStateAction)
                .then(ECM_FOLDER, checkFilesResend, saveStateAction)
                .last(BUSINESS_ERROR, saveStateAction)
                .and();

        // from ECM_RECEIVE
        // - -(sla)-> SLA_ERROR
        // задача создана ? --> PEGA_SEND : создать задачу в PEGA (retry)
        transitions
                // to SLA_ERROR on time
                .withLocal()
                .source(ECM_RECEIVE).target(SLA_ERROR)
                .action(slaErrorAction)
                .timer(getSlaForState(ECM_RECEIVE))
                .and()

                // - to PEGA_SEND (retry)
                .withLocal()
                .source(ECM_RECEIVE).target(PEGA_SEND)
                .timerOnce(RETRY_START_DELAY)
                .guard(checkCreateTaskInPega)
                .action(createTaskInPegaAction, createTaskInPegaErrorAction)
                .and()

                .withLocal()
                .source(ECM_RECEIVE).target(FsmState.PEGA_SEND)
                .timer(RETRY_DELAY)
                .guard(checkCreateTaskInPega)
                .action(createTaskInPegaAction, createTaskInPegaErrorAction)
                .and();



        // TODO: from ... & etc

        transitions
                .withLocal()
                .source(ECM_RECEIVE).target(PEGA_SEND)
                .guard(null)
                .action(null, null)
                .and();

        // to EXIT
        transitions
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

                // from ERROR to EXIT
                .withLocal()
                .source(BUSINESS_ERROR).target(EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(null)
                .action(saveStateAction)
                .and()

                .withLocal()
                .source(SLA_ERROR).target(EXIT)
                .action(saveStateAction)
                .timer(180000L);
    }

    private long getSlaForState(FsmState signed) {
        return 180_000L;
    }




}
