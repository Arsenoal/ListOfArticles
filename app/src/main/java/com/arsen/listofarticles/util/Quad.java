package com.arsen.listofarticles.util;

public class Quad<First, Second, Third, Fourth> {
    public First first;
    public Second second;
    public Third third;
    public Fourth fourth;

    public Quad(First first, Second second, Third third, Fourth fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }
}
