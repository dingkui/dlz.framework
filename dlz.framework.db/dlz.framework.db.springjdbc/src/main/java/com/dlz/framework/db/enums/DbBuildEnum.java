package com.dlz.framework.db.enums;

import com.dlz.comm.exception.SystemException;
import com.dlz.comm.json.JSONMap;
import com.dlz.comm.util.StringUtils;
import com.dlz.framework.db.util.SqlUtil;
import com.dlz.framework.db.modal.condition.Condition;
import com.dlz.framework.db.util.KeyUtil;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public enum DbBuildEnum {
    and("and #s"),//and多条件语句,
    or("or #s"),//or多条件语句,
    muOr("#s"),//多条件语句 or 拼接
    muAnd("#s"),//多条件语句 and 拼接,
    sql("(#s)"),//自定义sql,
    apply("(#s)"),//自定义sql,
    where("where #s");//自定义sql,
    private final String _sql;

    private static Pattern PATTERN_INDEX = Pattern.compile("\\{(\\d+)\\}");

    public Condition build(String sql, JSONMap paras) {
        switch (this) {
            case sql:
                if(StringUtils.isEmpty(sql)){
                    return null;
                }
                sql = sql.replaceAll("\\$\\.", "\\\\\\$\\\\\\.");
                sql = SqlUtil.getConditionStr(sql, paras);
                sql = SqlUtil.replaceSql(sql, paras, 0);
                Condition condition = new Condition(this);
                condition.setRunsql(buildSql(sql));
                if (paras != null && !paras.isEmpty()) {
                    condition.addParas(paras);
                }
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }
    public Condition build(String sql, Object... paras) {
        switch (this) {
            case apply:
                if(StringUtils.isEmpty(sql)){
                    return null;
                }
                Condition condition = new Condition(this);
                JSONMap paraM=new JSONMap();

                Matcher mat = PATTERN_INDEX.matcher(sql);
                int start = 0;
                StringBuffer sb = new StringBuffer();
                while (mat.find()) {
                    String index = mat.group(1);
                    sb.append(sql, start, mat.start());
                    String key = KeyUtil.getKeyName("apply_");
                    sb.append("#{"+key+"}");
                    paraM.put(key, paras[Integer.parseInt(index)]);
                    start = mat.end();
                }

                if (start > 0) {
                    sb.append(sql, start, sql.length());
                    sql = sb.toString();
                }

                sql = sql.replaceAll("\\$\\.", "\\\\\\$\\\\\\.");
                sql = SqlUtil.getConditionStr(sql, paraM);
                sql = SqlUtil.replaceSql(sql, paraM, 0);
                condition.setRunsql(buildSql(sql));
                if (!paraM.isEmpty()) {
                    condition.addParas(paraM);
                }
                return condition;
        }
        throw new SystemException("匹配符有误：" + this);
    }

    public Condition build() {
        switch (this) {
            case where:
            case and:
            case or:
            case muOr:
            case muAnd:
                return new Condition(this).setRunsql(_sql);
        }
        throw new SystemException("匹配符有误：" + this);
    }
    public String buildSql(String sql) {
        if (sql.length() == 0) {
            return "";
        }
        return _sql.replace("#s", sql);
    }

}
