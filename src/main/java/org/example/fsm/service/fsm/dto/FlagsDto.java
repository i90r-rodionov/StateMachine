package org.example.fsm.service.fsm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.fsm.statemachine.state.FsmState;

@Getter
@Setter
@NoArgsConstructor
public class FlagsDto {
    private FsmState state;
    private boolean noSignFlag;
    private boolean createdFolderFlag;
    private boolean movedFilesFlag;

    public FlagsDto(FsmState state, boolean noSignFlag, boolean createdFolderFlag, boolean movedFilesFlag) {
        this.state = state;
        this.noSignFlag = noSignFlag;
        this.createdFolderFlag = createdFolderFlag;
        this.movedFilesFlag = movedFilesFlag;
    }

    public FlagsDto(boolean noSignFlag, boolean createdFolderFlag, boolean sendFileFlag) {
        state = FsmState.CREATE;
        this.noSignFlag = noSignFlag;
        this.createdFolderFlag = createdFolderFlag;
        this.movedFilesFlag = sendFileFlag;
    }
}
