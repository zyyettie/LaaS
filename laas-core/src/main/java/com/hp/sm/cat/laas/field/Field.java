package com.hp.sm.cat.laas.field;

public interface Field<T> {

  String getContent();

  String getName();

  T getValue();

}
