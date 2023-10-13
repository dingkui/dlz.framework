package com.dlz.framework.db.warpper;

import com.dlz.comm.json.JSONMap;
import com.dlz.framework.db.modal.BaseParaMap;
import com.dlz.framework.util.system.MFunction;
import com.dlz.framework.util.system.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dlz.framework.db.enums.DbOprateEnum.*;

public class Condition {
    boolean isMake=false;
    String runsql;
    JSONMap paras = new JSONMap();
    List<Condition> children=new ArrayList<>();
    private Condition parent;
    private void make(BaseParaMap pm){
        if(isMake){
            return;
        }
        if(children.size()>0){
            String subSq = children.stream().map(item -> item.getRunsql(pm)).collect(Collectors.joining(" and "));
            runsql = runsql.replace("sql",subSq.replaceAll("and and","and").replaceAll("and or","or"));
            isMake = true;
            return;
        }
        pm.addParas(paras);
        isMake = true;
    }

    public String getRunsql(BaseParaMap pm) {
        make(pm);
        return runsql;
    }

    public void setRunsql(String runsql) {
        isMake=false;
        this.runsql = runsql;
    }

    public void addPara(String key, Object value) {
        isMake=false;
        paras.put(key,value);
    }
    public void addParas(JSONMap paras) {
        isMake=false;
        this.paras.putAll(paras);
    }

    private Condition addChildren(Condition child){
        children.add(child);
        child.parent=this;
        return child;
    }


    public <T> Condition bt(MFunction<T, ?> column, Object value1, Object value2){
        addChildren(bt.mk(Reflections.getFieldName(column), value1,value2));
        return this;
    }

    public <T> Condition nb(MFunction<T, ?> column, Object value1, Object value2){
        addChildren(nb.mk(Reflections.getFieldName(column), value1,value2));
        return this;
    }

    public <T> Condition isnn(MFunction<T, ?> column){
        addChildren(isnn.mk(Reflections.getFieldName(column)));
        return this;
    }
    public <T> Condition isn(MFunction<T, ?> column){
        addChildren(isn.mk(Reflections.getFieldName(column)));
        return this;
    }
    public <T>  Condition eq(MFunction<T, ?> column, Object value){
        addChildren(eq.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition lt(MFunction<T, ?> column, Object value){
        addChildren(lt.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition nl(MFunction<T, ?> column, Object value){
        addChildren(nl.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition lr(MFunction<T, ?> column, Object value){
        addChildren(lr.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition ll(MFunction<T, ?> column, Object value){
        addChildren(ll.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition lk(MFunction<T, ?> column, Object value){
        addChildren(lk.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition in(MFunction<T, ?> column, Object value){
        addChildren(in.mkin(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition ne(MFunction<T, ?> column, Object value){
        addChildren(ne.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition ge(MFunction<T, ?> column, Object value){
        addChildren(ge.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition gt(MFunction<T, ?> column, Object value){
        addChildren(gt.mk(Reflections.getFieldName(column), value));
        return this;
    }
    public <T> Condition le(MFunction<T, ?> column, Object value){
        addChildren(le.mk(Reflections.getFieldName(column), value));
        return this;
    }






    public Condition bt(String clumnName, Object value1, Object value2){
        addChildren(bt.mk(clumnName, value1,value2));
        return this;
    }

    public Condition nb(String clumnName, Object value1, Object value2){
        addChildren(nb.mk(clumnName, value1,value2));
        return this;
    }
    public Condition eq(String clumnName, Object value){
        addChildren(eq.mk(clumnName, value));
        return this;
    }
    public Condition isnn(String clumnName){
        addChildren(isnn.mk(clumnName));
        return this;
    }
    public Condition isn(String clumnName){
        addChildren(isn.mk(clumnName));
        return this;
    }
    public Condition lt(String clumnName, Object value){
        addChildren(lt.mk(clumnName, value));
        return this;
    }
    public Condition nl(String clumnName, Object value){
        addChildren(nl.mk(clumnName, value));
        return this;
    }
    public Condition lr(String clumnName, Object value){
        addChildren(lr.mk(clumnName, value));
        return this;
    }
    public Condition ll(String clumnName, Object value){
        addChildren(ll.mk(clumnName, value));
        return this;
    }
    public Condition lk(String clumnName, Object value){
        addChildren(lk.mk(clumnName, value));
        return this;
    }
    public Condition in(String clumnName, Object value){
        addChildren(in.mkin(clumnName, value));
        return this;
    }
    public Condition ne(String clumnName, Object value){
        addChildren(ne.mk(clumnName, value));
        return this;
    }
    public Condition ge(String clumnName, Object value){
        addChildren(ge.mk(clumnName, value));
        return this;
    }
    public Condition gt(String clumnName, Object value){
        addChildren(gt.mk(clumnName, value));
        return this;
    }
    public Condition le(String clumnName, Object value){
        addChildren(le.mk(clumnName, value));
        return this;
    }
    public Condition sql(String _sql, JSONMap paras){
        addChildren(sql.mk(_sql,paras));
        return this;
    }
    public Condition and(){
        return addChildren(and.mk());
    }
    public Condition end(){
        return parent;
    }
    public Condition or(){
        return addChildren(or.mk());
    }

}
