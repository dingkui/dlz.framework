package com.dlz.framework.db.modal.items;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@Data
public class TableItem {
    private String name;
    private String id;
    private List<Field> fields;
    private HashMap<String, Integer> columns;
}