package org.example.model;

public enum Position {
    GK(1),
    DL(2), DC(3), DR(4),
    DM(5),
    ML(6), MC(7), MR(8),
    AML(9), AMC(10), AMR(11),
    FC(12),
    ST(13);

    private final int order;

    Position(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
