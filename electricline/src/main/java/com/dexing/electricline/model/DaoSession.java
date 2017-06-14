package com.dexing.electricline.model;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.dexing.electricline.model.BoxUser;
import com.dexing.electricline.model.EPoint;

import com.dexing.electricline.model.BoxUserDao;
import com.dexing.electricline.model.EPointDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig boxUserDaoConfig;
    private final DaoConfig ePointDaoConfig;

    private final BoxUserDao boxUserDao;
    private final EPointDao ePointDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        boxUserDaoConfig = daoConfigMap.get(BoxUserDao.class).clone();
        boxUserDaoConfig.initIdentityScope(type);

        ePointDaoConfig = daoConfigMap.get(EPointDao.class).clone();
        ePointDaoConfig.initIdentityScope(type);

        boxUserDao = new BoxUserDao(boxUserDaoConfig, this);
        ePointDao = new EPointDao(ePointDaoConfig, this);

        registerDao(BoxUser.class, boxUserDao);
        registerDao(EPoint.class, ePointDao);
    }
    
    public void clear() {
        boxUserDaoConfig.getIdentityScope().clear();
        ePointDaoConfig.getIdentityScope().clear();
    }

    public BoxUserDao getBoxUserDao() {
        return boxUserDao;
    }

    public EPointDao getEPointDao() {
        return ePointDao;
    }

}
