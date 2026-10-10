package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerGrouper {

    public static Map<String, List<Customer>> groupByCountry(List<Customer> customers){

        HashMap<String, List<Customer>> map = new HashMap<>();

        for (Customer customer : customers){
            if(map.containsKey(customer.country())){
                map.get(customer.country()).add(customer);
            } else {
                List<Customer> list = new ArrayList<>();
                list.add(customer);
                map.put(customer.country(), list);
            }
        }
        return map;
    }

    public static List<String> topCountries(Map<String, List<Customer>> grouped, int n){
        List<String> list;
        ArrayList countries = new ArrayList<>();
        countries.addAll(grouped.keySet());
        countries.sort((a, b) -> grouped.get(b).size() - grouped.get(a).size());

        return countries.subList(0, n);
    }
}
