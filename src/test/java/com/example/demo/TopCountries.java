package com.example.demo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TopCountries {

    @Test
    public void TopCountriesShowInOrder()
    {
        //Arrange:
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Anna", "Canada"));
        customers.add(new Customer("Max", "Germany"));
        customers.add(new Customer("Pierre", "France"));
        customers.add(new Customer("Bridgette", "France"));
        customers.add(new Customer("Julian", "France"));
        customers.add(new Customer("Geraldine", "France"));
        customers.add(new Customer("Michael", "Belgium"));
        customers.add(new Customer("Paulus", "Germany"));
        customers.add(new Customer("Dieter", "Germany"));
        customers.add(new Customer("Tony", "Belgium"));

        //Act:
        Map<String, List<Customer>> customersOrderedByCountry = CustomerGrouper.groupByCountry(customers);
        List<String> topCountries = CustomerGrouper.topCountries(customersOrderedByCountry,3);

        //Assert:
        assertEquals("France", topCountries.get(0));
        assertEquals("Germany", topCountries.get(1));
        assertEquals("Belgium", topCountries.get(2));
    }
}
