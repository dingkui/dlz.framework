package com.dlz.framework.db.helper.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Condition {
	String column;
	String operation;
	Object value;
}
