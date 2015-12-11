insert into ROLE(ID, NAME) values (1, 'ROLE_ADMIN')
insert into ROLE(ID, NAME) values (2, 'ROLE_USER')
insert into INBOX(ID) values (1)
insert into INBOX(ID) values (2)

insert into QUOTA(ID, MAX_FILE_SIZE, SPACE_QUOTA, USED_SPACE) values(1, 104857600, 209715200, 0)
insert into QUOTA(ID, MAX_FILE_SIZE, SPACE_QUOTA, USED_SPACE) values(2, 104857600, 209715200, 0)
insert into USERS(ID,NAME,password,ROLE_ID,inbox_id, quota_id) values(1,'admin','password',1,1,1)
insert into USERS(ID,NAME,password,ROLE_ID,inbox_id, quota_id) values(2,'user1','password',2,2,2)

insert into PRODUCT(ID, NAME) values(1, 'Service Manager')

insert into CATEGORY(ID, NAME) values(1, 'Performance')
insert into CATEGORY(ID, NAME) values(2, 'Debug')

insert into FILE_TYPE(ID, TYPE) values(1, 'RTE Log')
insert into FILE_TYPE(ID, TYPE) values(2, 'SM Config')
insert into FILE_TYPE(ID, TYPE) values(3, 'Result')

insert into SCENARIO(ID, NAME, product_id) values(1, 'Scenario - Top N', 1)
insert into SCENARIO(ID, NAME, product_id) values(2, 'Scenario - RAD Show', 1)
insert into SCENARIO(ID, NAME, product_id) values(3, 'Scenario - SM ENV Info', 1)
insert into SCENARIO(ID, NAME, product_id) values(4, 'Scenario - Split Process/Thread', 1)
insert into SCENARIO(ID, NAME, product_id) values(5, 'Scenario - SM-OMi Performance', 1)
insert into SCENARIO(ID, NAME, product_id) values(6, 'Scenario - SM Testing', 1)

insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(1, 'Task - Top N', 'org.g6.laas.sm.task.TopNQueryTask', 1, 1, 0)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(2, 'Task - RAD Show', 'org.g6.laas.sm.task.RadShowTask', 1, 1, 0)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(4, 'Task - Split Process/Thread', 'org.g6.laas.sm.task.SplitProcessAndThreadTask', 1, 1, 0)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(5, 'Task - SM-OMi Performance', 'org.g6.laas.sm.task.SMOMiPerformanceTask', 1, 1, 0)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(6, 'Task - SM RTE', 'org.g6.laas.sm.task.SMRTETask', 1, 1, 0)


insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(7, 'Get Process Id and Thread Id by login name', 'org.g6.laas.sm.task.ProcessAndThreadOfUserTask', 1, 1, 0)
insert into TASK(ID, NAME, CLASS_NAME, PRODUCT_ID, FILE_TYPE_ID, TYPE) values(8, 'Get SM RTE evn info', 'org.g6.laas.sm.task.SMRTEInfoTask', 1, 1, 0)

insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(1, 1,1,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(2, 2,2,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(4, 4,4,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(5, 5,5,1)

insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(7, 3,7,1)
insert into ORDERED_TASK(ID, SCENARIO_ID,TASK_ID,TASK_ORDER) values(8, 3,8,2)

insert into scenario_file_types(scenario_id, file_types_id) values(1, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(2, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(3, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(4, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(5, 1)
insert into scenario_file_types(scenario_id, file_types_id) values(6, 1)

insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(1, 'sm.log', 'e:/', '1', 'sm.log',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(3, 'sm.log.1', 'e:/', '1', 'sm.log.1',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(4, 'sm.log.2', 'e:/', '1', 'sm.log.2',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(2, 'sm_dbquery.log', 'c:/temp/', '1', 'sm_dbquery.log',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(5, 'sm_dbquery.log.1', 'c:/temp/', '1', 'sm_dbquery.log.1',1,'N')
insert into FILE(ID, FILE_NAME, PATH, FILE_TYPE_ID, ORIGINAL_NAME,created_by_id,is_removed) values(6, 'sm_scripttrace.log', 'c:/temp/', '1', 'sm_scripttrace.log',1,'N')

insert into INPUT_PARAMETER_DEF(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied, DEFAULT_VALUE, task_id) values(1, 'N', 'N', 'text', 1, 1, false, '50', 1)
insert into INPUT_PARAMETER_DEF(ID, NAME, DISPLAY_INFO, TYPE, VALUE_LIST, WIDTH, HEIGHT, line_occupied, DEFAULT_VALUE, task_id) values(2, 'order', 'Order', 'dropdown', 'ASC|DESC', 1, 1, false, 'DESC', 1)
insert into INPUT_PARAMETER_DEF(ID, NAME, DISPLAY_INFO, TYPE, VALUE_LIST, WIDTH, HEIGHT, line_occupied, DEFAULT_VALUE, task_id) values(3, 'category', 'Category', 'dropdown', 'DBQUERY|SCRIPTTRACE', 1, 1, false, 'DBQUERY', 1)
insert into INPUT_PARAMETER_DEF(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied, task_id) values(4, 'processId', 'Process ID', 'text', 1, 1, false, 4)
insert into INPUT_PARAMETER_DEF(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied, task_id) values(5, 'threadId', 'Thread ID', 'text', 1, 1, false, 4)
insert into INPUT_PARAMETER_DEF(ID, NAME, DISPLAY_INFO, TYPE, WIDTH, HEIGHT, line_occupied, task_id) values(6, 'userName', 'User Name', 'text', 1, 1, true, 7)

insert into scenario_input_parameter_defs(scenario_id, input_parameter_defs_id) values(1, 1)
insert into scenario_input_parameter_defs(scenario_id, input_parameter_defs_id) values(1, 2)
insert into scenario_input_parameter_defs(scenario_id, input_parameter_defs_id) values(1, 3)
insert into scenario_input_parameter_defs(scenario_id, input_parameter_defs_id) values(4, 4)
insert into scenario_input_parameter_defs(scenario_id, input_parameter_defs_id) values(4, 5)
insert into scenario_input_parameter_defs(scenario_id, input_parameter_defs_id) values(3, 6)
