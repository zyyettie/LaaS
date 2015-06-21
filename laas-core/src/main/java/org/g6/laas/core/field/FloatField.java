package org.g6.laas.core.field;

public class FloatField extends AbstractField<Float> {

    @Override
    public Float getValue() {
        return Float.valueOf(getContent());
    }

    public FloatField(String content) {
        super(content);
    }
}
