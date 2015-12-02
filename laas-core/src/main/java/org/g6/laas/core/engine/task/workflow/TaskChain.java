package org.g6.laas.core.engine.task.workflow;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
@Data
public class TaskChain{
    LinkedList<Task> linkedList = new LinkedList<>();

    public void addFirst(Task task){
          linkedList.addFirst(task);
    }

    public void addLast(Task task){
         linkedList.addLast(task);
    }

    public void add(int index, Task task){
          linkedList.add(index, task);
    }

    public void add(Task task){
        linkedList.add(task);
    }

    public void remove(int index){
         linkedList.remove(index);
    }

    public int size(){
        return linkedList.size();
    }

}
