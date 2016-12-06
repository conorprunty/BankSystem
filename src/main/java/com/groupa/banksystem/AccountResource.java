/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.groupa.banksystem;

import com.google.gson.Gson;
import java.util.List;
import java.util.Random;
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
@Path("/account")
public class AccountResource {

    Gson gson;

    AccountService acc = new AccountService();

    public AccountResource() {
        gson = new Gson();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Account> listCustomer() {
        return acc.retrieveAccounts();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/user/{id}")
    public Account getCustomer(@PathParam("id") int id) {
        return acc.retrieveAccount(id);
    }

    @POST
    @Path("/user/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Account createCurrAccount(String input) {
        String sortCode = "AIBKIE2D";
        Random rand = new Random();
        int accNum = rand.nextInt(10);
        Double currBal = 500.0;

        int custId = Integer.parseInt(input);
        Account account = new Account(sortCode, accNum, currBal, custId);

        return acc.addAccount(account);
    }

}
