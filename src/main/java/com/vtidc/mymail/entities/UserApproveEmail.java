package com.vtidc.mymail.entities;

import com.vtidc.mymail.entities.auditing.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_approve_email")
@NoArgsConstructor
@AllArgsConstructor
public class UserApproveOrganization extends AbstractAuditingEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_user_approve", nullable = false)
    private Integer idUserApprove;

    @Column(name = "id_user_send", nullable = false)
    private Integer idUserSend;

}