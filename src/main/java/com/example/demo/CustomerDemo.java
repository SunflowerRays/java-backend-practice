package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerDemo {

    public static void main(String[] args) {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("Anna", "Canada"));
        customers.add(new Customer("Max", "Germany"));
        customers.add(new Customer("Pierre", "France"));
        customers.add(new Customer("Bridgette", "France"));
        customers.add(new Customer("Geraldine", "France"));
        customers.add(new Customer("Michael", "Belgium"));
        customers.add(new Customer("Paulus", "Germany"));
        customers.add(new Customer("Dieter", "Germany"));
        customers.add(new Customer("Tony", "Belgium"));

        //customers.forEach(customer -> System.out.println(customer.toString()));

        Map<String, List<Customer>> customersOrderedByCountry = CustomerGrouper.groupByCountry(customers);
        customersOrderedByCountry.forEach((k, v) -> {
            System.out.println(k + " " + v.size());
        });

        List<String> topCountries = CustomerGrouper.topCountries(customersOrderedByCountry,3);

        topCountries.forEach(country -> {System.out.println(country);});
    }
}
