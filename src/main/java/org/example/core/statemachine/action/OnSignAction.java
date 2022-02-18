package org.example.core.statemachine.action;

import lombok.extern.slf4j.Slf4j;
import org.example.core.service.mock.CheckService;
import org.example.core.statemachine.event.FsmEvent;
import org.example.core.statemachine.persist.StateHolder;
import org.example.core.statemachine.state.FsmState;
import org.example.core.statemachine.util.FsmHelper;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

/**
 * Реализация действий при переходе READY_TO_SIGN (?) -> SIGNED
 *
 */
@Component
@Slf4j
public class OnSignAction extends BaseAction {

    private final CheckService service;

    public OnSignAction(StateHolder stateHolder, CheckService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        // TODO: отправить сообщение "создай каталог в ЕСМ"
        boolean success = service.defaultAction();

        if (!success) {
            generateError();
        }
        super.execute(context);
    }

}
