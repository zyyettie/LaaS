package org.g6.laas.core.field;

public class TextField extends AbstractField<String> {

    public TextField(String content) {
        super(content);
    }
    public TextField(String name, String content) {
        super(name, content);
    }
    public String getValue() {
        return this.getContent();
    }

    public int compareTo(Field o) {
        return this.getValue().compareTo((String) o.getValue());
    }
}
