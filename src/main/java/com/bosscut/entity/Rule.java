package com.bosscut.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Rule.
 */
@Entity
@Table(name = "rule")
public class Rule extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ruleId;

    @Column(name = "rule_name")
    private String ruleName;

    @Column(name = "description")
    private String description;

    public Rule() {
    }

    public Rule(Long ruleId, String ruleName, String description) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.description = description;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
