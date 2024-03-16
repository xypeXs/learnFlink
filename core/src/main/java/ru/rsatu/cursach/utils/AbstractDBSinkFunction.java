package ru.rsatu.cursach.utils;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public abstract class AbstractDBSinkFunction<T> implements SinkFunction<T> {

    protected final SessionFactory sessionFactory;

    public AbstractDBSinkFunction() {
        sessionFactory = SessionFactoryUtils.getSessionFactory();
    }

    @Override
    public void invoke(T value, Context context) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        processSql(value, session);

        transaction.commit();
        session.close();
    }

    public abstract void processSql(T value, Session session);
}
