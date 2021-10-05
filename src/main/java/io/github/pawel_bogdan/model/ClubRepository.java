package io.github.pawel_bogdan.model;

import io.github.pawel_bogdan.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class ClubRepository {
    public List<Club> findAll() {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var clubs = session.createQuery("select c from Club c").list();
        transaction.commit();
        session.close();
        return clubs;
    }

    public Optional<Club> findById(String shortName) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var result = session.get(Club.class, shortName);
        transaction.commit();
        session.close();
        return Optional.ofNullable(result);
    }

    public boolean contains(String shortName) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var result = true;
        if (session.get(Club.class, shortName) == null)
            result = false;
        transaction.commit();
        session.close();
        return result;
    }

    public void add(Club club) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        session.persist("Club", club);
        transaction.commit();
        session.close();
    }

    public void update(Club newClub) {
        var session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        session.update("Club", newClub);
        transaction.commit();
        session.close();
    }
}
