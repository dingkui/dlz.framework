package com.dlz.test.framework.db.entity;

import com.dlz.framework.db.annotation.IdType;
import com.dlz.framework.db.annotation.TableId;
import lombok.Data;

@Data
public class SysSql {
    @TableId(value = "id",type = IdType.INPUT)
    private Long id;
    private String sqlKey;
    private String sqlValue;
    private String name;
    private String sqlRole;
}
