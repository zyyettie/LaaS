package org.g6.laas.sm.task;

import org.g6.laas.core.engine.task.ChainTask;
import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.sm.log.login.Info;
import org.g6.laas.sm.vo.LoginInfo;

import java.util.List;
import java.util.Map;

public class LoginUserInfoTask extends ChainTask<LoginInfo> {

    @Override
    protected LoginInfo process() {
        return null;
    }

    @Override
    public void doTask(Map request, Map response, TaskChain chain) {
         LoginInfo info = process();
    }
}
