package com.vtidc.mymail.entities;

import com.vtidc.mymail.entities.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "flow_email_from")
@NoArgsConstructor
@AllArgsConstructor
public class FlowEmailFrom extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "flow_email_id")
    private Integer flowEmailId;

    @Size(max = 120)
    @Column(name = "type", length = 120)
    private String type;

}