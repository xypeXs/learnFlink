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

    public void saveOrUpdate(String sql) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query<?> query = session.createQuery(sql);
        query.executeUpdate();

        transaction.commit();
        session.close();
    }

    @Override
    public abstract void invoke(T value, Context context) throws Exception;
}
