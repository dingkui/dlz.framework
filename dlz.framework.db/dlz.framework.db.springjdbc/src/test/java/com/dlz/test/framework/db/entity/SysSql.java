package com.dlz.test.framework.db.entity;

import lombok.Data;

@Data
public class SysSql {
    private Long id;
    private String sqlKey;
    private String name;
    private String sqlRole;
}
