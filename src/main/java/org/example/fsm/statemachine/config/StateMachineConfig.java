package org.example.fsm.statemachine.config;

import org.example.fsm.statemachine.action.*;
import org.example.fsm.statemachine.event.FsmEvent;
import org.example.fsm.statemachine.guard.*;
import org.example.fsm.statemachine.listener.FsmApplicationListener;
import org.example.fsm.statemachine.state.FsmState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<FsmState, FsmEvent> {

    private static final long SIGNED_SLA = 180000L;
    private static final long SIGNED_RETRY = 15000L;
    private static final long CREATE_ECM_FOLDER_RETRY = 10000L;
    private final SaveStateAction saveStateAction;
    private final SaveStateAndStopAction saveStateAndStopAction;
    private final CreateEcmFolderGuard createEcmFolderGuard;
    private final CreateEcmFolderAction createEcmFolderAction;
    private final CreateEcmFolderErrorAction createEcmFolderErrorAction;

    public StateMachineConfig(
            SaveStateAction saveStateAction,
            SaveStateAndStopAction saveStateAndStopAction,
            CreateEcmFolderGuard ecmFolderGuard,
            CreateEcmFolderAction ecmFolderAction,
            CreateEcmFolderErrorAction ecmFolderErrorAction
    ) {
        this.saveStateAction = saveStateAction;
        this.saveStateAndStopAction = saveStateAndStopAction;
        this.createEcmFolderGuard = ecmFolderGuard;
        this.createEcmFolderAction = ecmFolderAction;
        this.createEcmFolderErrorAction = ecmFolderErrorAction;
    }


    @Override
    public void configure(final StateMachineConfigurationConfigurer<FsmState, FsmEvent> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new FsmApplicationListener())
        ;
    }

    @Override
    public void configure(final StateMachineStateConfigurer<FsmState, FsmEvent> states) throws Exception {
        states
                .withStates()
                .initial(FsmState.CREATE)
                //.states(EnumSet.allOf(FsmState.class))
                .state(FsmState.READY_TO_PRINT)
                .state(FsmState.READY_TO_SIGN)
                .choice(FsmState.CHECK_SIGN)
                .state(FsmState.SIGNED)
                .state(FsmState.ECM_FOLDER)
                .state(FsmState.ERROR)
                .state(FsmState.SLA_ERROR)
                .end(FsmState.EXIT);


    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<FsmState, FsmEvent> transitions) throws Exception {

        // to READY_TO_PRINT
        transitions
                .withExternal()
                .source(FsmState.CREATE).target(FsmState.READY_TO_PRINT)
                .event(FsmEvent.CHECK_READY_TO_PRINT)
                .guard(checkReadyToPrint())
                .action(saveStateAction);

        // to READY_TO_SIGN
        transitions
                .withExternal()
                .source(FsmState.READY_TO_PRINT).target(FsmState.READY_TO_SIGN)
                .event(FsmEvent.CHECK_READY_TO_SIGN)
                .guard(checkReadyToSign())
                .action(saveStateAction);

        // from READY_TO_SIGN
        transitions
                // - to exit
                .withExternal()
                .source(FsmState.READY_TO_SIGN).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .action(saveStateAction)
                .and()

                .withExternal()
                .source(FsmState.READY_TO_SIGN).target(FsmState.CHECK_SIGN)
                .event(FsmEvent.SIGN)
                .guard(null)
                .action(saveStateAction)
                .and()

                // - to error
                // - to SIGNED
                .withChoice()
                .source(FsmState.CHECK_SIGN)
                .first(FsmState.ERROR, checkNoSign(), saveStateAction)
                .then(FsmState.SIGNED, null, saveStateAction)
                .and()
        ;

        // from SIGNED:
        transitions
                // - to SLA_ERROR on time
                .withExternal()
                .source(FsmState.SIGNED).target(FsmState.SLA_ERROR)
                .event(FsmEvent.SLA_EVENT)
                .guard(checkSla())
                .action(saveStateAction)
                .and()

                .withLocal()
                .source(FsmState.SIGNED).target(FsmState.SLA_ERROR)
                .action(slaErrorAction())
                .timer(SIGNED_SLA)
                .and()

                // - to ECM_FOLDER
                .withLocal()
                .source(FsmState.SIGNED).target(FsmState.ECM_FOLDER)
                .timer(CREATE_ECM_FOLDER_RETRY)
                .guard(createEcmFolderGuard)
                .action(createEcmFolderAction, createEcmFolderErrorAction)
                .and()

        ;


        // to EXIT
        transitions
                .withExternal()
                .source(FsmState.CREATE).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(checkExit())
                .action(saveStateAndStopAction)
                .and()

                .withExternal()
                .source(FsmState.READY_TO_PRINT).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(checkExit())
                .action(saveStateAndStopAction)
                .and()

                .withExternal()
                .source(FsmState.READY_TO_SIGN).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(checkExit())
                .action(saveStateAndStopAction)
                .and()

                .withExternal()
                .source(FsmState.ECM_FOLDER).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(checkExit())
                .action(saveStateAndStopAction)
                .and()

                // from ERROR to EXIT
                .withExternal()
                .source(FsmState.ERROR).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(checkExit())
                .action(saveStateAction)
                .and()

                .withInternal()
                .source(FsmState.ERROR)
                .action(timerAction())
                .timer(15000)
                .and()

                // from SLA_ERROR to EXIT
                .withExternal()
                .source(FsmState.SLA_ERROR).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(checkExit())
                .action(saveStateAction)
                .and()

                .withInternal()
                .source(FsmState.SLA_ERROR)
                .action(timerAction())
                .timer(15000)

        ;



    }

    @Bean
    public Action<FsmState, FsmEvent> errorAction() {
        return new ErrorAction();
    }

    @Bean
    public Action<FsmState, FsmEvent> timerAction() {
        return new TimerAction();
    }

    @Bean
    public Action<FsmState, FsmEvent> slaErrorAction() {
        return new SlaErrorAction();
    }

    @Bean
    public Guard<FsmState, FsmEvent> checkReadyToPrint() {
        return new ReadyToPrintGuard();
    }

    @Bean
    public Guard<FsmState, FsmEvent> checkReadyToSign() {
        return new ReadyToSignGuard();
    }

    @Bean
    public Guard<FsmState, FsmEvent> checkNoSign() {
        return new NoSignGuard();
    }

    @Bean
    public Guard<FsmState, FsmEvent> checkSla() {
        return new SlaGuard();
    }


    @Bean
    public Guard<FsmState, FsmEvent> allSign() {
        return new AllSign();
    }

    @Bean
    public Guard<FsmState, FsmEvent> checkExit() {
        return new ExitGuard();
    }

    @Bean
    public Guard<FsmState, FsmEvent> ecmFolderGuard() {
        return new EcmFolderGuard();
    }

//    @Bean
//    public StateMachinePersister<PurchaseState, PurchaseEvent, String> persister() {
//        return new DefaultStateMachinePersister<>(new PurchaseStateMachinePersister());
//    }
}
