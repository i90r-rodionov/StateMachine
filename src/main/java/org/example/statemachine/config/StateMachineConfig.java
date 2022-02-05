package org.example.statemachine.config;

import org.example.statemachine.domain.statemachine.action.*;
import org.example.statemachine.domain.statemachine.event.FsmEvent;
import org.example.statemachine.domain.statemachine.guard.*;
import org.example.statemachine.domain.statemachine.listener.FsmApplicationListener;
import org.example.statemachine.domain.statemachine.state.FsmState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<FsmState, FsmEvent> {

    private final SaveStateAction saveStateAction;
    private final SaveStateAndStopAction saveStateAndStopAction;
    private final EcmFolderAction ecmFolderAction;

    public StateMachineConfig(
            SaveStateAction saveStateAction,
            SaveStateAndStopAction saveStateAndStopAction,
            EcmFolderAction ecmFolderAction
    ) {
        this.saveStateAction = saveStateAction;
        this.saveStateAndStopAction = saveStateAndStopAction;
        this.ecmFolderAction = ecmFolderAction;
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
                .choice(FsmState.SIGNED)
                .state(FsmState.ECM_FOLDER_CREATE)
                .state(FsmState.ERROR)
                .state(FsmState.SLA_ERROR)
                .end(FsmState.EXIT);


    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<FsmState, FsmEvent> transitions) throws Exception {

        // to EXIT
        transitions
                .withExternal()
                .source(FsmState.CREATE).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(tryExit())
                .action(saveStateAndStopAction)
                .and()

                .withExternal()
                .source(FsmState.READY_TO_PRINT).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(tryExit())
                .action(saveStateAndStopAction)
                .and()

                .withExternal()
                .source(FsmState.READY_TO_SIGN).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(tryExit())
                .action(saveStateAndStopAction)
                .and()

                .withExternal()
                .source(FsmState.ECM_FOLDER_CREATE).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(tryExit())
                .action(saveStateAndStopAction)
                .and()

                .withInternal()
                .source(FsmState.ERROR)
                .action(timerAction())
                .timer(15000)
                .and()

                .withExternal()
                .source(FsmState.ERROR).target(FsmState.EXIT)
                .event(FsmEvent.EXIT_EVENT)
                .guard(tryExit())
                .action(saveStateAction)

        ;

        // from CREATE
        transitions
                .withExternal()
                .source(FsmState.CREATE).target(FsmState.READY_TO_PRINT)
                .event(FsmEvent.TRY_PRINT)
                .guard(tryPrint())
                .action(saveStateAction)
        ;

        // from READY_TO_PRINT
        transitions
                .withExternal()
                .source(FsmState.READY_TO_PRINT).target(FsmState.READY_TO_SIGN)
                .event(FsmEvent.TRY_SIGN)
                .guard(trySign())
                .action(saveStateAction)
        ;

        // from READY_TO_SIGN
        transitions
                .withExternal()
                .source(FsmState.READY_TO_SIGN).target(FsmState.SIGNED)
                .event(FsmEvent.SIGN_EVENT)
                .guard(allSign())
                .action(saveStateAction)
        ;

        // from SIGNED
        transitions
                .withChoice()
                .source(FsmState.SIGNED)
                .first(FsmState.ERROR, taskChoice())
                .last(FsmState.ECM_FOLDER_CREATE, ecmFolderAction)
                .and()

                .withExternal()
                .source(FsmState.SIGNED).target(FsmState.SLA_ERROR)
                .event(FsmEvent.SLA_EVENT)
                .action(saveStateAction)
                .and()


                .withInternal()
                .source(FsmState.SIGNED)
                .action(slaErrorAction())
                .timer(15000)
                .and()

        ;


/*
                .and()
                .withExternal()
                .source(ECM_FOLDER_CREATE)
                .target(ECM_SEND)
                .timer(0)
                .guard(null)
                .action(null)

                .and()
                .withExternal()
                .source(ECM_SEND)
                .event(ECM_CALLBACK)
                .target(ECM_RECEIVE)
                .guard(null)
                .action(null)

                .and()//?
                .withExternal()
                .source(ECM_SEND)
                .target(ECM_RECEIVE)
                .timer(0)
                .guard(null)
                .action(null)

                .and()
                .withExternal()
                .source(ECM_RECEIVE)
                .target(PEGA_SEND)
                .guard(null)
                .action(null)

                .and()
                .withExternal()
                .source(ECM_RECEIVE)
                .target(PEGA_SEND)
                .guard(null)
                .action(null)
*/

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
    public Guard<FsmState, FsmEvent> tryPrint() {
        return new TryPrint();
    }

    @Bean
    public Guard<FsmState, FsmEvent> trySign() {
        return new TrySign();
    }

    @Bean
    public Guard<FsmState, FsmEvent> allSign() {
        return new AllSign();
    }

    @Bean
    public Guard<FsmState, FsmEvent> tryExit() {
        return new TryExit();
    }

    @Bean
    public Guard<FsmState, FsmEvent> taskChoice() {
        return new TaskChoice();
    }

//    @Bean
//    public StateMachinePersister<PurchaseState, PurchaseEvent, String> persister() {
//        return new DefaultStateMachinePersister<>(new PurchaseStateMachinePersister());
//    }
}
