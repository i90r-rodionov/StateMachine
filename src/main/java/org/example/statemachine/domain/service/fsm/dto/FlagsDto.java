package org.example.statemachine.domain.service.fsm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlagsDto {
    private boolean noSignFlag;
    private boolean createFolderFlag;
}
