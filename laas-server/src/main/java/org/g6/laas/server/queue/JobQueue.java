package org.g6.laas.server.queue;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

@Service
public class JobQueue {
    BlockingQueue<QueueJob> queue = new LinkedBlockingDeque<>();

    public void addJob(QueueJob job){
        queue.add(job);
    }

    public QueueJob get() throws InterruptedException {
       //return queue.poll(10000, TimeUnit.MICROSECONDS);
        return queue.take();
    }

    public void remove(){
        queue.remove();
    }

    public boolean isEmpty(){
       return queue.isEmpty();
    }

    public int size(){
        return queue.size();
    }
}
