package org.g6.laas.server.queue;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobQueue {
    List<QueueJob> queue = new ArrayList();

    public void addJob(QueueJob job){
        queue.add(job);
    }

    public QueueJob get(){
       return queue.get(0);
    }

    public void remove(){
        queue.remove(0);
    }

    public boolean isEmpty(){
       return queue.isEmpty();
    }

    public int size(){
        return queue.size();
    }
}
