package com.example.recyclerviewpractice;

public class Variant {

    public String name;
    public int price;

    public Variant(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Qty:  "+" "+name + "\n" + "Rs.  "+" "+price;
    }
}
