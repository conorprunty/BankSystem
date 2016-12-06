/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.groupa.banksystem;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author conorprunty
 */
public class CustomerService {

    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("Customer");
    EntityManager emManager = emfactory.createEntityManager();
    EntityTransaction trans = emManager.getTransaction();

    public List<Customer> getCustomers() {
        return allEntries();
    }

    public List<Customer> allEntries() {
        CriteriaBuilder cb = emManager.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> rootEntry = cq.from(Customer.class);
        CriteriaQuery<Customer> all = cq.select(rootEntry);
        TypedQuery<Customer> allQuery = emManager.createQuery(all);
        return allQuery.getResultList();
    }

    public Customer getCustomer(int id) {
        Customer test = emManager.find(Customer.class, id);
        emManager.close();
        return test;
    }

    public Customer addCustomer(Customer c) {

        Customer newCust = emManager.find(Customer.class, c.getId());

        if (newCust == null) {
            trans.begin();
            emManager.persist(c);
            trans.commit();
            emManager.close();
        }
        return c;
    }
}
