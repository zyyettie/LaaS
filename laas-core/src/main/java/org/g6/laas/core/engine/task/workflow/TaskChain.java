package org.g6.laas.core.engine.task.workflow;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.task.ChainTask;

import java.util.LinkedList;

@Slf4j
@Data
public class TaskChain{
    LinkedList<ChainTask> linkedList = new LinkedList<>();

    public void addFirst(ChainTask task){
          linkedList.addFirst(task);
    }

    public void addLast(ChainTask task){
         linkedList.addLast(task);
    }

    public void add(int index, ChainTask task){
          linkedList.add(index, task);
    }

    public void add(ChainTask task){
        linkedList.add(task);
    }

    public void remove(int index){
         linkedList.remove(index);
    }

    public int size(){
        return linkedList.size();
    }

}
