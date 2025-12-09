package com.dlz.framework.db.service.impl;

import com.dlz.framework.db.dao.IDlzDao;
import com.dlz.framework.db.service.ICommService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommServiceImpl implements ICommService {
    private IDlzDao dao;

    @Override
    public IDlzDao getDao() {
        return dao;
    }

}
