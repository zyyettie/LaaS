insert into USER(id,name) values(1,'admin')

insert into PRODUCT(ID, NAME) values(1, 'Service Manager')

insert into CATEGORY(ID, NAME) values(1, 'Performance')
insert into CATEGORY(ID, NAME) values(2, 'Debug')

insert into FILE_TYPE(ID, TYPE) values(1, 'RTE Log')
insert into FILE_TYPE(ID, TYPE) values(2, 'SM Config')

insert into TASK(ID, NAME, CLASSNAME, USER_ID) values(1, 'Top N', 'org.g6.laas.sm.task.TopNQueryTask', 1)
insert into TASK(ID, NAME, CLASSNAME, USER_ID) values(2, 'RAD Show', 'org.g6.laas.sm.task.RadShowTask', 1)
insert into TASK(ID, NAME, CLASSNAME, USER_ID) values(3, 'Login Time', 'org.g6.laas.sm.task.LoginTimeInfo', 1)
insert into TASK(ID, NAME, CLASSNAME, USER_ID) values(4, 'Split Process/Thread', 'org.g6.laas.sm.task.SplitProcessAndThreadTask', 1)
insert into TASK(ID, NAME, CLASSNAME, USER_ID) values(5, 'SM-OMi Performance', 'org.g6.laas.sm.task.SMOMiPerformanceTask', 1)
insert into TASK(ID, NAME, CLASSNAME, USER_ID) values(6, 'SM RTE', 'org.g6.laas.sm.task.SMRTETask', 1)

insert into WORKFLOW(ID, NAME) values(1, 'Top N')
insert into WORKFLOW(ID, NAME) values(2, 'RAD Show')
insert into WORKFLOW(ID, NAME) values(3, 'Login Time')
insert into WORKFLOW(ID, NAME) values(4, 'Split Process/Thread')
insert into WORKFLOW(ID, NAME) values(5, 'SM-OMi Performance')
insert into WORKFLOW(ID, NAME) values(6, 'SM RTE')

insert into WORKFLOW_TASK(WORKFLOW_ID, TASK_ID) values(1, 1)
insert into WORKFLOW_TASK(WORKFLOW_ID, TASK_ID) values(2, 2)
insert into WORKFLOW_TASK(WORKFLOW_ID, TASK_ID) values(3, 3)
insert into WORKFLOW_TASK(WORKFLOW_ID, TASK_ID) values(4, 4)
insert into WORKFLOW_TASK(WORKFLOW_ID, TASK_ID) values(5, 5)
insert into WORKFLOW_TASK(WORKFLOW_ID, TASK_ID) values(6, 6)

insert into SCENARIO(ID, NAME, PRODUCT_ID, USER_ID) values(1, 'Test Scenario', 1, 1)

insert into SCENARIO_WORKFLOW(SCENARIO_ID, WORKFLOW_ID) values(1, 1)
insert into SCENARIO_WORKFLOW(SCENARIO_ID, WORKFLOW_ID) values(1, 2)
insert into SCENARIO_WORKFLOW(SCENARIO_ID, WORKFLOW_ID) values(1, 3)
insert into SCENARIO_WORKFLOW(SCENARIO_ID, WORKFLOW_ID) values(1, 4)
insert into SCENARIO_WORKFLOW(SCENARIO_ID, WORKFLOW_ID) values(1, 5)
insert into SCENARIO_WORKFLOW(SCENARIO_ID, WORKFLOW_ID) values(1, 6)

