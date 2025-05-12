package com.vtidc.mymail.entities;

import com.vtidc.mymail.entities.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "user_config")
@NoArgsConstructor
@AllArgsConstructor
public class UserConfig extends AbstractAuditingEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "action_id")
    private Integer actionId;

    @Column(name = "config_data")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> configData;

}