package org.g6.laas.core.field;

public class DoubleField extends AbstractField<Double> {
    @Override
    public Double getValue() {
        return Double.valueOf(getContent());
    }

    public DoubleField(String content) {
        super(content);
    }
}
