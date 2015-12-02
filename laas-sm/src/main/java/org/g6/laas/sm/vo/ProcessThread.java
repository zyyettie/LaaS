package org.g6.laas.sm.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class ProcessThread implements Serializable, Comparable<ProcessThread> {
    private int processId;
    private int threadId;

    public boolean equals(Object o) {
        if (!(o instanceof ProcessThread))
            return false;
        ProcessThread pt = (ProcessThread) o;
        if (this.processId == pt.processId && this.threadId == pt.threadId) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Note the reason why 0 is not considered to as the return value is
     * this object will be put Set collection and as the equals method
     * is overridden in this class, the duplicated records will not happen.
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(ProcessThread o) {
        if (this.processId < o.processId && this.threadId < o.threadId) {
            return 1;
        } else {
            return -1;
        }
    }
}
