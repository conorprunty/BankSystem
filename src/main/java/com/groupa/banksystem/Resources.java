/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.groupa.banksystem;

import java.util.List;
import java.util.Random;
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

/**
 *
 * @author conorprunty
 */
@Path("/customers")
public class Resources {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bank");
    private final EntityManager em = emf.createEntityManager();
    private final EntityTransaction tx = em.getTransaction();

    //get all customers
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Customer> getCustomers() {
        return allEntries();
    }

    public List<Customer> allEntries() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> rootEntry = cq.from(Customer.class);
        CriteriaQuery<Customer> all = cq.select(rootEntry);
        TypedQuery<Customer> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    //get one customer by ID
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("{id}")
    public Customer getCustomer(@PathParam("id") int id) {
        Customer bank = em.find(Customer.class, id);
        return bank;
    }

    //add a new customer
    @POST
    @Path("/addCustomer")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Customer addCustomer(Customer c) {
        Customer bank = em.find(Customer.class, c.getId());
        if (bank == null) {
            tx.begin();
            em.persist(c);
            tx.commit();
        }
        return c;
    }

    //get account by ID
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/account/{id}")
    public Account getAccount(@PathParam("id") int accId) {
        Account bank = em.find(Account.class, accId);
        return bank;
    }

    //get balance by account ID
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/account/{id}/balance")
    public int getBalance(@PathParam("id") int accId) {
        Account bank = em.find(Account.class, accId);
        return bank.getBalance();
    }

    //add a lodgement
    @POST
    @Path("/{amount}/addLodgment")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account addLodgment(@PathParam("amount") int amount, Account a) {
        Account bank = em.find(Account.class, a.getAccountId());
        if (bank == null) {

            Transactions tr = new Transactions();
            tr.setType("Debit");
            tr.setDate("23-12-2016");
            tr.setDescription("Amount updated");
            tr.setBalance(a.getBalance() + amount);

            tx.begin();
            em.persist(tr);
            a.getTransactions().add(tr);
            em.persist(a);
            tx.commit();
        }
        return a;
    }

    //transfer to existing account
    @POST
    @Path("/{amount}/{accountID}/transfer")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account transfer(@PathParam("amount") int amount, @PathParam("accountID") int accID, int accID2) {
        Account bank = em.find(Account.class, accID);
        Account bank2 = em.find(Account.class, accID2);

        if (bank == null & bank2 == null) {

            if (bank.getBalance() > amount) {
                bank.setBalance(bank.getBalance() - amount);
                bank2.setBalance(bank2.getBalance() + amount);
            }

            tx.begin();
            em.persist(bank);
            em.persist(bank2);
            tx.commit();
        }
        return bank2;
    }

    //withdrawal
    @POST
    @Path("/{amount}/withdrawal")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account withdrawal(@PathParam("amount") int amount, Account a) {
        Account bank = em.find(Account.class, a.getAccountId());
        if (bank == null) {

            Transactions tr = new Transactions();
            tr.setType("Debit");
            tr.setDate("23-12-2016");
            tr.setDescription("Amount updated");
            if (bank.getBalance() > amount) {
                tr.setBalance(a.getBalance() - amount);
            }

            tx.begin();
            em.persist(tr);
            a.getTransactions().add(tr);
            em.persist(a);
            tx.commit();
        }
        return a;
    }

    //add an account
    @POST
    @Path("/addAccount")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Customer addAccount(Customer c) {
        Customer bank = em.find(Customer.class, c.getId());
        if (bank == null) {

            Account a = new Account();
            Random ran = new Random();
            int accNum = ran.nextInt(90000000) + 10000000;
            a.setAccountNumber(String.valueOf(accNum));
            a.setSortcode("BOFIIE2D");
            a.setBalance(0);

            tx.begin();
            em.persist(a);
            c.getAccount().add(a);
            em.persist(c);
            tx.commit();
        }
        return c;
    }

}
