package org.g6.laas.core.field;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class AbstractField<T> implements Field<T> {
    private String content;
    private String name;
    private boolean sortable;
    private int priority;

    public AbstractField(String content) {
        this.content = content;
    }

    public AbstractField(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
