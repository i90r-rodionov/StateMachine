package org.example.core.statemachine.action;

import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


/**
 * Реализация перехода ECM_SEND -> ECM_RECEIVE
 *
 */
@Component
public class OnDeliveredAction extends BaseAction {

    private final CheckService service;

    public OnDeliveredAction(StateHolder stateHolder, CheckService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {
        // TODO: отправка сообщения "сщоздать задачу в ПЕГА"
        boolean success = service.defaultAction();

        if (!success) {
            generateError();
        }
        super.execute(context);
    }
}
