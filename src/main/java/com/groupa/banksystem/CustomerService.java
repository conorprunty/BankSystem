/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.groupa.banksystem;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
@Path("/customers")
public class CustomerService {
    
    EntityManager entityManager;
    
    public CustomerService(){
        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("Customer");
        entityManager = emfactory.createEntityManager();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Customer> getPlanets() {
       return allEntries();
    }
    
     public List<Customer> allEntries() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> rootEntry = cq.from(Customer.class);
        CriteriaQuery<Customer> all = cq.select(rootEntry);
        TypedQuery<Customer> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }
     
    @GET
    @Produces({MediaType.APPLICATION_JSON })
    @Path("{id}")
    public Customer getPlanet(@PathParam("id") int id) {
        Customer test = entityManager.find(Customer.class, id);
        return test;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response addCustomer(Customer c) {

        entityManager.getTransaction().begin();

        entityManager.persist(c);
        entityManager.getTransaction().commit();
        
        //entityManager.close();
        //entityManager.close();

        return Response.status(200).entity(c).build();
    }
}
