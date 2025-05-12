package com.vtidc.mymail.dto.search;

import com.vtidc.mymail.dto.validate.ValueOfEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserRequest {
    private String keyword;

    private String sortBy;

    @ValueOfEnum(enumClass = Sort.Direction.class)
    private String sortAscending;

    private Integer tagId;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;


}

