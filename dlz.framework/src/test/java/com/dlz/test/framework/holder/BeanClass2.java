package com.dlz.test.framework.holder;

import org.springframework.beans.factory.annotation.Autowired;

public class BeanClass2 {
    public BeanClass1 getBeanClass1() {
        return beanClass1;
    }


    @Autowired
    BeanClass1 beanClass1;
}
