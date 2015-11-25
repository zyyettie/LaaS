insert into ROLE(ID, NAME) values (1, 'ROLE_ADMIN')
insert into ROLE(ID, NAME) values (2, 'ROLE_USER')
insert into INBOX(ID) values (1)
insert into INBOX(ID) values (2)

insert into USER(ID,NAME,password,ROLE_ID,inbox_id) values(1,'admin','password',1,1)
insert into USER(ID,NAME,password,ROLE_ID,inbox_id) values(2,'user1','password',2,2)

insert into PRODUCT(ID, NAME) values(1, 'Service Manager')

insert into CATEGORY(ID, NAME) values(1, 'Performance')
insert into CATEGORY(ID, NAME) values(2, 'Debug')

insert into FILE_TYPE(ID, TYPE) values(1, 'RTE Log')
insert into FILE_TYPE(ID, TYPE) values(2, 'SM Config')
insert into FILE_TYPE(ID, TYPE) values(3, 'Result')

insert into SCENARIO(ID, NAME, product_id) values(1, 'Scenario - Top N', 1)
insert into SCENARIO(ID, NAME, product_id) values(2, 'Scenario - RAD Show', 1)
insert into SCENARIO(ID, NAME, product_id) values(3, 'Scenario - Login Info', 1)
insert into SCENARIO(ID, NAME, product_id) values(4, 'Scenario - Split Process/Thread', 1)
insert into SCENARIO(ID, NAME, product_id) values(5, 'Scenario - SM-OMi Performance', 1)
insert into SCENARIO(ID, NAME, product_id) values(6, 'Scenario - SM RTE', 1)

insert into JOB(ID, NAME,CATEGORY_ID,PRODUCT_ID,parameters) values(1, 'Test Job',1,1,'{"N":"50","order":"desc"}')

insert into JOB_SCENARIOS(JOBS_ID,SCENARIOS_ID) values(1, 1)

insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID) values(1, 'Task - Top N', 'org.g6.laas.sm.task.TopNQueryTask', 1, 1)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID) values(2, 'Task - RAD Show', 'org.g6.laas.sm.task.RadShowTask', 1, 1)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID) values(3, 'Task - Login Time', 'org.g6.laas.sm.task.LoginTimeInfo', 1, 1)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID) values(4, 'Task - Split Process/Thread', 'org.g6.laas.sm.task.SplitProcessAndThreadTask', 1, 1)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID) values(5, 'Task - SM-OMi Performance', 'org.g6.laas.sm.task.SMOMiPerformanceTask', 1, 1)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID) values(6, 'Task - SM RTE', 'org.g6.laas.sm.task.SMRTETask', 1, 1)

insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(1, 1,1,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(2, 2,2,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(3, 3,3,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(4, 4,4,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(5, 5,5,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(6, 6,6,1)

insert into scenario_file_types(scenario_id, file_types_id) values(1, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(2, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(3, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(4, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(5, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(6, 1)


insert into PARAMETER_DEFINE(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied, DEFAULT_VALUE) values(1, 'N', 'N', 'text', 1, 1, false, '50')
insert into PARAMETER_DEFINE(ID, NAME, DISPLAY_INFO, TYPE, VALUE_LIST, WIDTH, HEIGHT, line_occupied, DEFAULT_VALUE) values(2, 'order', 'Order', 'dropdown', 'ASC|DESC', 1, 1, false, 'DESC')
insert into PARAMETER_DEFINE(ID, NAME, DISPLAY_INFO, TYPE, VALUE_LIST, WIDTH, HEIGHT, line_occupied, DEFAULT_VALUE) values(3, 'category', 'Category', 'dropdown', 'DBQUERY|SCRIPTTRACE', 1, 1, false, 'DBQUERY')
insert into PARAMETER_DEFINE(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied) values(4, 'user', 'User', 'text', 1, 1, true)
insert into PARAMETER_DEFINE(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied) values(5, 'startTime', 'From', 'time', 1, 1, false)
insert into PARAMETER_DEFINE(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied) values(6, 'endTime', 'To', 'time', 1, 1, false)

insert into task_parameter_defines(task_id, parameter_defines_id) values(1, 1)
insert into task_parameter_defines(task_id, parameter_defines_id) values(1, 2)
insert into task_parameter_defines(task_id, parameter_defines_id) values(1, 3)
insert into task_parameter_defines(task_id, parameter_defines_id) values(3, 4)
insert into task_parameter_defines(task_id, parameter_defines_id) values(3, 5)
insert into task_parameter_defines(task_id, parameter_defines_id) values(3, 6)

insert into scenario_parameter_defines(scenario_id, parameter_defines_id) values(1, 1)
insert into scenario_parameter_defines(scenario_id, parameter_defines_id) values(1, 2)
insert into scenario_parameter_defines(scenario_id, parameter_defines_id) values(1, 3)
insert into scenario_parameter_defines(scenario_id, parameter_defines_id) values(3, 4)
insert into scenario_parameter_defines(scenario_id, parameter_defines_id) values(3, 5)
insert into scenario_parameter_defines(scenario_id, parameter_defines_id) values(3, 6)

insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(1, 'sm.log', 'e:/', '1', 'sm.log',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(3, 'sm.log.1', 'e:/', '1', 'sm.log.1',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(4, 'sm.log.2', 'e:/', '1', 'sm.log.2',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(2, 'sm_dbquery.log', 'c:/', '1', 'sm_dbquery.log',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(5, 'sm_dbquery.log.1', 'e:/', '1', 'sm_dbquery.log.1',1,'N')

insert into JOB_FILES(JOBS_ID, FILES_ID) values(1, 2)