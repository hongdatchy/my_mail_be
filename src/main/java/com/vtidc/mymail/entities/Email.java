package com.vtidc.mymail.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "email")
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 50)
    @Column(name = "zimbra_id", length = 50)
    private String zimbraId;

    @Size(max = 120)
    @Column(name = "display_name", length = 120)
    private String displayName;

    @Size(max = 120)
    @Column(name = "mail", length = 120)
    private String mail;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "org_id")
    private Integer orgId;

}