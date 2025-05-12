package com.vtidc.mymail.dto;

import com.vtidc.mymail.dto.validate.OnUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveDistributionListDto {

    @NotNull(groups = {OnUpdate.class})
    private Integer id;

    @NotNull(groups = {OnUpdate.class})
    private String zimbraId;

    private List<Integer> tagIdList;

    @NotEmpty(message = "không được trống")
    private String displayName;

    @NotEmpty(message = "không được trống")
    private String mail;

    private List<EmailDto> emailDtoList;
}
