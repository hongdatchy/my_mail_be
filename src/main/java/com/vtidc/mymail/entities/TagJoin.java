package com.vtidc.mymail.entities;

import com.vtidc.mymail.entities.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tag_join")
@NoArgsConstructor
@AllArgsConstructor
public class TagJoin extends AbstractAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "tag_id")
    private Integer tagId;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "type", length = 120)
    private String type;

}