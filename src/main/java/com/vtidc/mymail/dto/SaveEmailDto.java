package com.vtidc.mymail.dto;

import com.vtidc.mymail.config.enums.UserType;
import com.vtidc.mymail.dto.validate.OnCreate;
import com.vtidc.mymail.dto.validate.OnUpdate;
import com.vtidc.mymail.dto.validate.ValueOfEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveEmailDto {

    @NotNull(groups = {OnUpdate.class})
    private Integer id;

    @NotEmpty(message = "không được trống")
    private String displayName;

    @NotEmpty(message = "không được trống")
    private String mail;

    private List<Integer> tagIdList;

    @NotNull(message = "không được trống")
    @ValueOfEnum(enumClass = UserType.class)
    private String type;

    private String oldPassword;

    // update nếu có password thì sẽ check oldPassword, nếu có old password thì sẽ tiến hành đổi pass
    @NotEmpty(groups = {OnCreate.class}, message = "không được trống")
    @Length(min = 6, message = "mật khẻu phải nhất 6 ky tự")
    private String password;

    private Integer orgId;

}

