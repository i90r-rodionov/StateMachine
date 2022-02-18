package org.example.core.statemachine.action;

import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.util.FsmHelper;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class OnCheckAction extends BaseAction {

    private final CheckService service;

    public OnCheckAction(StateHolder stateHolder, CheckService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {


        if (isSetSla(context)) {
            sendEvent(context, FsmEvent.SLA_EVENT);
            log.debug("   ### Action stop execute {}", this.getClass().getSimpleName());
            return;
        }

        super.execute(context);

        sendEvent(context, FsmEvent.NEXT_EVENT);
    }

    @Override
    protected boolean isSetSla(StateContext<FsmState, FsmEvent> context) {
        return (boolean) context.getExtendedState().getVariables().getOrDefault(FsmHelper.SLA_FLAG, false);
    }

}
