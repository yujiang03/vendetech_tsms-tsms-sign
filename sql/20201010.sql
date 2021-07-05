
alter table employee modify employee_num varchar(45) null comment '员工编码';

alter table dd_hrm_data_tmp modify dd_emp_num varchar(45) null;

## 2020.10.28
alter table dd_attendance_data modify employee_num varchar(45) null;

## 2020.10.29
delete from employee where employee_num = '邹伟';

## 2020.10.30
update employee set mobile = '13068010211' where employee_num = 'L00000102';
update employee set mobile = '15110451412' where employee_num = 'L00000137';
update employee set mobile = '13467130420' where employee_num = 'L00000117';
update employee set mobile = '19935945661' where employee_num = 'L00000141';
update employee set mobile = '18235935672' where employee_num = 'L00000108';

## 2020.11.18
ALTER TABLE dd_hrm_data_sign ADD dd_position varchar(60) NULL
COMMENT '合同字段, 职位, field_code: sys00-position' AFTER dd_name;