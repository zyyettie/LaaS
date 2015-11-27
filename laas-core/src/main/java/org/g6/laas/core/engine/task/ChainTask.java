package org.g6.laas.core.engine.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
@NoArgsConstructor
public abstract class ChainTask<T> extends AbstractAnalysisTask<T>{

    public abstract void doTask(Map paramMap,Map output, TaskChain chain);
}
