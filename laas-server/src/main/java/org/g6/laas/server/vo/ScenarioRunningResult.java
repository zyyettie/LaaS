package org.g6.laas.server.vo;

import lombok.Data;

import java.util.concurrent.Future;

@Data
public class ScenarioRunningResult {
    Future future;
    boolean isTimeout = false;
    Object result;
    boolean report;
}
