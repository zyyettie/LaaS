insert into USER(id,name) values(1,'admin')

insert into PRODUCT(ID, NAME) values(1, 'Service Manager')

insert into CATEGORY(ID, NAME) values(1, 'Performance')
insert into CATEGORY(ID, NAME) values(2, 'Debug')

insert into FILE_TYPE(ID, TYPE) values(1, 'RTE Log')
insert into FILE_TYPE(ID, TYPE) values(2, 'SM Config')

insert into TASK(ID, NAME, CLASSNAME) values(1, 'Task - Top N', 'org.g6.laas.sm.task.TopNQueryTask')
insert into TASK(ID, NAME, CLASSNAME) values(2, 'Task - RAD Show', 'org.g6.laas.sm.task.RadShowTask')
insert into TASK(ID, NAME, CLASSNAME) values(3, 'Task - Login Time', 'org.g6.laas.sm.task.LoginTimeInfo')
insert into TASK(ID, NAME, CLASSNAME) values(4, 'Task - Split Process/Thread', 'org.g6.laas.sm.task.SplitProcessAndThreadTask')
insert into TASK(ID, NAME, CLASSNAME) values(5, 'Task - SM-OMi Performance', 'org.g6.laas.sm.task.SMOMiPerformanceTask')
insert into TASK(ID, NAME, CLASSNAME) values(6, 'Task - SM RTE', 'org.g6.laas.sm.task.SMRTETask')

insert into SCENARIO(ID, NAME) values(1, 'Scenario - Top N')
insert into SCENARIO(ID, NAME) values(2, 'Scenario - RAD Show')
insert into SCENARIO(ID, NAME) values(3, 'Scenario - Login Time')
insert into SCENARIO(ID, NAME) values(4, 'Scenario - Split Process/Thread')
insert into SCENARIO(ID, NAME) values(5, 'Scenario - SM-OMi Performance')
insert into SCENARIO(ID, NAME) values(6, 'Scenario - SM RTE')

insert into SCENARIO_TASK(SCENARIO_ID, TASK_ID) values(1, 1)
insert into SCENARIO_TASK(SCENARIO_ID, TASK_ID) values(2, 2)
insert into SCENARIO_TASK(SCENARIO_ID, TASK_ID) values(3, 3)
insert into SCENARIO_TASK(SCENARIO_ID, TASK_ID) values(4, 4)
insert into SCENARIO_TASK(SCENARIO_ID, TASK_ID) values(5, 5)
insert into SCENARIO_TASK(SCENARIO_ID, TASK_ID) values(6, 6)



