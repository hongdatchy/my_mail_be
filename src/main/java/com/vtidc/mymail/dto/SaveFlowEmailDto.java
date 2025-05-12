package com.vtidc.mymail.dto;

import com.vtidc.mymail.dto.validate.OnUpdate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveFlowEmailDto {

    @NotNull(groups = {OnUpdate.class})
    private Integer id;

    @NotEmpty(message = "không được trống")
    private String name;

    private List<Integer> tagIdList;

    private Instant startDate;

    @NotNull(message = "không được trống")
    private Boolean startNow;

    @NotNull(message = "không được trống")
    private String content;


    private String status;

    @NotEmpty(message = "không được trống")
    private List<FlowEmailEntityDto> flowEmailFromDtoList;

    @NotEmpty(message = "không được trống")
    private List<FlowEmailEntityDto> flowEmailToDtoList;

    @NotEmpty(message = "không được trống")
    private List<FlowEmailEntityDto> flowEmailapproveDtoList;
}
