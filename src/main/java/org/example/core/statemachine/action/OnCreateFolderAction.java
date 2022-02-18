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
 * Реализация действий при переходе SIGNED -> ECM_FOLDER_CREATED
 *
 */
@Component
@Slf4j
public class OnCreateFolderAction extends BaseAction {

    private final CheckService service;

    public OnCreateFolderAction(StateHolder stateHolder, CheckService service) {
        super(stateHolder);
        this.service = service;
    }

    @Override
    public void execute(final StateContext<FsmState, FsmEvent> context) {

        // TODO: отправить сообщение "переложи файлы в ECM"
        boolean success = service.defaultAction();

        if (!success) {
            generateError();
        }
        super.execute(context);
    }

    @Override
    protected boolean isSetSla(StateContext<FsmState, FsmEvent> context) {
        return (boolean) context.getExtendedState().getVariables().getOrDefault(FsmHelper.SLA_FLAG, false);
    }

}
