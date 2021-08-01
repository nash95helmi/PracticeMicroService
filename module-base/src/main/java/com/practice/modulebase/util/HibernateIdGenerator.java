package com.practice.modulebase.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class HibernateIdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

    public static String UUID() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }

}
