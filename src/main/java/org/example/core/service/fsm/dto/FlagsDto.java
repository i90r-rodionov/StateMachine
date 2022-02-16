package org.example.core.service.fsm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.core.statemachine.state.FsmState;

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
        state = FsmState.CREATED;
        this.noSignFlag = noSignFlag;
        this.createdFolderFlag = createdFolderFlag;
        this.movedFilesFlag = sendFileFlag;
    }
}
