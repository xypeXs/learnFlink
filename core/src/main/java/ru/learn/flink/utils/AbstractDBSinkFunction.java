package ru.learn.flink.utils;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AbstractDBSinkFunction<T> extends RichSinkFunction<T> {

    protected SessionFactory sessionFactory;

    protected Session session;

    @Override
    public void invoke(T value, Context context) throws Exception {
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            processSql(value, session);

            session.flush();
        } catch (Exception e) {
            throw new HibernateException("Cannot execute script", e);
        } finally {
            if (transaction != null)
                transaction.commit();
        }
    }

    @Override
    public void open(Configuration parameters) {
        sessionFactory = SessionFactoryUtils.getSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public void close() {
        if (session != null)
            session.close();
    }

    public abstract void processSql(T value, Session session);
}
