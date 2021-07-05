
-- ----------------------------
-- 运营管理新权限
-- ----------------------------
INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`,
                        `is_frame`, `is_react`, `menu_type`, `visible`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (12, '人事管理', 0, 4, '/OperateHR', '',
        1, 0, 'M', '0', null, null, 'zjp', null, '', null, '人事管理')
    ,  (134, '协议管理', 12, 1, '/OperateHR/AgreementManagement', '../pages/OperateHR/AgreementManagement',
        1, 0, 'C', '0', null, null, 'zjp', null, '', null, '协议管理')
    ,  (135, '协议查看', 12, 2, '/OperateHR/AgreementStatistics', '../pages/OperateHR/AgreementStatistics',
        1, 0, 'C', '0', null, null, 'zjp', null, '', null, '协议查看')
    ,  (136, '行政用章', 12, 3, '/OperateHR/AgreementSign', '../pages/OperateHR/AgreementSign',
        1, 0, 'C', '0', null, null, 'zjp', null, '', null, '行政用章');

INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`,
                        `is_frame`, `is_react`, `menu_type`, `visible`, `perms`, `icon`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (13, '企业管理', 0, 5, '/OperateCompany', '',
        1, 0, 'M', '0', null, null, 'zjp', null, '', null, '企业管理')
    ,  (137, '公司管理', 13, 1, '/OperateCompany/CompanyManagement', '../pages/OperateCompany/CompanyManagement',
        1, 0, 'C', '0', null, null, 'zjp', null, '', null, '公司管理')
    ,  (138, '印章管理', 13, 2, '/OperateCompany/SignatureManagement', '../pages/OperateCompany/SignatureManagement',
        1, 0, 'C', '0', null, null, 'zjp', null, '', null, '印章管理');

INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `del_flag`, `create_by`,
                        `create_time`, `update_by`, `update_time`, `remark`)
VALUES (6, '人事经理（人事审核）', 'HR_CHECK', 6, '1', '0', '0', 'zjp', now(), null, null, '人事审核')
     , (7, '人事经理（行政用章）', 'HR_SIGN', 7, '1', '0', '0', 'zjp', now(), null, null, '行政用章')
     , (8, '人事经理', 'HR', 8, '1', '0', '0', 'zjp', now(), null, null, '人事经理');

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
VALUES (6, 12), (6, 134), (6, 135)
      , (7, 12), (7, 135), (7, 136)
      , (8, 12), (8, 134), (8, 135), (8, 136), (8, 13), (8, 137), (8, 138);

select user_id, user_name from sys_user where nick_name = '李晴';
update sys_user_role set role_id = 8 where user_id = (select user_id from sys_user where nick_name = '李晴');
select user_id, user_name from sys_user where nick_name = '华玉娟';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '华玉娟');
select user_id, user_name from sys_user where nick_name = '杨甜';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '杨甜');
select user_id, user_name from sys_user where nick_name = '黄凯丽';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '黄凯丽');
select user_id, user_name from sys_user where nick_name = '刘军';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '刘军');
select user_id, user_name from sys_user where nick_name = '马艾萍';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '马艾萍');
select user_id, user_name from sys_user where nick_name = '李玲';
update sys_user_role set role_id = 8 where user_id = (select user_id from sys_user where nick_name = '李玲');
select user_id, user_name from sys_user where nick_name = '徐海敏';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '徐海敏');
select user_id, user_name from sys_user where nick_name = '印显波';
update sys_user_role set role_id = 6 where user_id = (select user_id from sys_user where nick_name = '印显波');


-- ----------------------------
-- 职员表增加电签字段
-- ----------------------------
ALTER TABLE employee ADD sex               char             null       comment '0:男 1:女 该字段电签合同用到' after ts_excl_flag;
ALTER TABLE employee ADD card_type         int default '1'  not null   comment '证件类型 1:身份证,以后有国外作业员扩展其他证件用' after sex;
ALTER TABLE employee ADD card_num          varchar(32)      null       comment '证件号码' after card_type;
ALTER TABLE employee ADD mobile            varchar(20)      null       comment '手机号码' after card_num;
ALTER TABLE employee ADD bank_name         varchar(32)      null       comment '储蓄卡所属银行名称(根据卡号自动带出)' after mobile;
ALTER TABLE employee ADD bank_card         varchar(32)      null       comment '储蓄卡卡号' after bank_name;

ALTER TABLE employee ADD customer_id       varchar(50)      null       comment '成员在法大大上的账号' after bank_card;
ALTER TABLE employee ADD auth_status       int default '1'  null       comment '0：未激活；1：未认证；2：审核通过；3：已提交待审核；4：审核不通过' after customer_id;
ALTER TABLE employee ADD auth_date         datetime         null       comment '认证时间，法大大认证通过后回调时更新' after auth_status;

ALTER TABLE employee ADD status_desc     varchar(256)  NULL COMMENT '法大大不通过原因描述' AFTER auth_date;
ALTER TABLE employee ADD verify_url      varchar(512)  NULL COMMENT '法大大实名认证地址' AFTER status_desc;
ALTER TABLE employee ADD transaction_no  varchar(50)   NULL COMMENT '法大大实名认证交易号（获取url时返回）' AFTER verify_url;
ALTER TABLE employee ADD serial_no       varchar(50)   NULL COMMENT '法大大异步回调认证序列号' AFTER transaction_no;
ALTER TABLE employee ADD cert_status     int           NULL COMMENT '法大大申请证书状态：0.没有申请证书或者申请证书失败，1.成功申请证书' AFTER status_desc;


# ALTER TABLE employee ADD isauth         char         null       comment '认证时间，法大大认证通过后回调时更新' after auth_status;
# ALTER TABLE employee ADD authdate      datetime         null       comment '认证时间，法大大认证通过后回调时更新' after auth_status;

-- ----------------------------
-- 系统用户表，移动号码字段长度扩展
-- ----------------------------
ALTER TABLE sys_user MODIFY phonenumber varchar(20) DEFAULT '' COMMENT '手机号码';

-- ----------------------------
-- 协议模板
-- ----------------------------
drop table if exists sign_template;
create table sign_template
(
  id                int auto_increment          comment '主键,自增' primary key,
  tpl_code          varchar(64)       null      comment '协议编号',
  tpl_name          varchar(64)       null      comment '协议名称',
  tpl_path          varchar(256)      null      comment '协议模版PDF文件路径',
  company_id        bigint            null      comment '模板所属公司id',
  fdd_tpl_id        varchar(64)       null      comment '上传到fdd的模板id',
  is_upload         char default 'N'  not null  comment '模版文件是否已经上传第三方签名平台 Y：上传 N：未上传',
  tpl_expire_day    int               null      comment '电签协议可以签署的有效天数，比如15天内没有电签就签署不了',
  tpl_expire_year   int default '1'   not null  comment '电签协议年份，就是本次合同签几年的意思 ',
  status            int               null      comment '状态( -1:已作废 0:草稿 1：已发布 2:已到期  )',
  create_time       datetime          null      comment '创建时间',
  create_by_user_id bigint            null      comment '创建用户ID',
  modify_time       datetime          null      comment '修改时间',
  modify_by_user_id bigint            null      comment '修改用户ID'
) comment '电签模版表';

-- ----------------------------
-- 协议关联表
-- ----------------------------
drop table if exists sign_employee_rel;
create table sign_employee_rel
(
  id                int auto_increment  comment '主键,自增' primary key,
  template_id       int      null       comment '电签模版ID',
  employee_id       bigint   null       comment '成员ID',
  create_time       datetime null       comment '创建时间',
  create_by_user_id bigint   null       comment '创建用户ID',
  modify_time       datetime null       comment '修改时间',
  modify_by_user_id bigint   null       comment '修改用户ID'
)
  comment '电签成员配置表';

-- ----------------------------
-- 协议签署记录
-- ----------------------------
drop table if exists sign_record;
create table sign_record
(
  id                  bigint auto_increment   comment '主键,自增' primary key,
  template_id         int          null   comment '电签模版ID',
  employee_id         bigint       null   comment '签署者，成员ID',
  contract_no         varchar(64)  null   comment '传给法大大的合同编号',
  notify_date         datetime     null   comment '通知签名时间',
  notify_expire_date  datetime     null   comment '通知过期时间（通知签名时间+通知有效期天数）',
  contract_file_path  varchar(256) null   comment '合同文件存储路径',
  sign_date           datetime     null   comment '成员签名时间',
  expire_date         datetime     null   comment '合同过期时间（根据签名时间+协议配置表的年份字段）',
  status              int          null   comment '状态( -1:已过期 0:员工待签署 1：公司待签署 2：已生效 3:已到期  )',
  create_time         datetime     null   comment '创建时间',
  create_by_user_id   bigint       null   comment '创建用户ID',
  modify_time         datetime     null   comment '修改时间',
  modify_by_user_id   bigint       null   comment '修改用户ID'
) comment '电签记录表';

-- ----------------------------
-- 协议签署记录
-- ----------------------------
drop table if exists dd_hrm_data_sign;
create table dd_hrm_data_sign
(
  dd_user_id                 varchar(45)        null comment '系统字段，钉钉系统di',
  dd_job_number              varchar(20)        null comment '系统字段，工号, field_code: sys00-jobNumber',

  dd_name                    varchar(45)        null comment '合同字段，姓名，field_code：sys00-name',
  dd_sex_type                int(1)             null comment '合同字段，性别，field_code：sys02-sexType',
  dd_birth_time              varchar(20)        null comment '合同字段，出生日期，field_code：sys02-birthTime',
  dd_cert_address            varchar(256)       null comment '合同字段，身份证地址，field_code：sys02-certAddress',
  dd_address                 varchar(256)       null comment '合同字段，住址，field_code：sys02-address',
  dd_cert_no                 varchar(20)        null comment '合同字段，证件号码，field_code：sys02-certNo',
  dd_mobile                  varchar(20)        null comment '合同字段，手机号，field_code：sys00-mobile',
  dd_work_city               varchar(45)        null comment '合同字段，工作城市，自定义字段field_name：工作城市',
  dd_now_contract_start_time varchar(20)        null comment '合同字段，现合同到期日，field_code：sys05-nowContractStartTime',
  dd_now_contract_end_time   varchar(20)        null comment '合同字段，现合同到期日，field_code：sys05-nowContractEndTime',
  dd_probation_period_type   varchar(10)        null comment '合同字段，试用期，field_code：sys01-probationPeriodType',
  dd_regular_time            varchar(20)        null comment '合同字段，转正日期，field_code：sys01-regularTime',
  dd_confirm_join_time       varchar(20)        null comment '合同字段，入职时间，field_code：sys00-confirmJoinTime',

  dd_graduate_school         varchar(20)        null comment '扩展字段，毕业院校，field_code：sys03-graduateSchool',
  dd_email                   varchar(45)        null comment '扩展字段，邮箱，field_code：sys00-email',
  dd_bank_account_no         varchar(20)        null comment '扩展字段，银行卡号，field_code：sys04-bankAccountNo',
  dd_account_bank            varchar(20)        null comment '扩展字段，开户行，field_code：sys04-accountBank',

  dd_main_dept_id            bigint(20)         null comment '保留字段，主部门id，field_code：sys00-mainDeptId',
  dd_main_dept               varchar(45)        null comment '保留字段，主部门，field_code：sys00-mainDept',
  dd_company_name            varchar(45)        null comment '保留字段，所属公司，自定义字段field_name：所属公司',
  dd_ts_excl_flag            int(1)             null comment '保留字段，项目分配排除标识，自定义字段field_name：tsExclFlg',

  job_status                 int(1) default '0' null comment '任务字段，定时任务状态: 0-没处理过, 1-已处理过',
  create_time                datetime           null comment '任务字段，创建时间'
) comment '电签合同字段抓取表';

-- ----------------------------
-- 定时任务更新employee信息
-- ----------------------------
drop procedure if exists updateEmpHrmData;
create procedure updateEmpHrmData()
  BEGIN
    -- updateEmpHrmData
    UPDATE employee e
    JOIN dd_hrm_data_tmp dhd ON dhd.dd_emp_num = e.employee_num
    LEFT JOIN company c ON c.company_name = dhd.dd_company_name
    LEFT JOIN department d ON d.dd_department_id = dhd.dd_main_dept_id
    SET e.company_id = c.company_id,
        e.entry_date = dhd.dd_join_date,
        e.ts_excl_flag = dhd.dd_ts_excl_flag,
        e.main_dept_id = d.department_id,
        e.modify_time = sysdate();

    update employee e
    join dd_hrm_data_sign dhds on dhds.dd_job_number = e.employee_num
    set e.sex = dhds.dd_sex_type,
        e.mobile = dhds.dd_mobile,
        e.card_num = dhds.dd_cert_no,
        e.bank_name = dhds.dd_account_bank,
        e.bank_card = dhds.dd_bank_account_no,
        e.modify_time = sysdate()
    where (e.sex is null and dhds.dd_sex_type is not null)
       or (e.mobile is null and dhds.dd_mobile is not null)
       or (e.card_num is null and dhds.dd_cert_no is not null)
       or (e.bank_name is null and dhds.dd_account_bank is not null)
       or (e.bank_card is null and dhds.dd_bank_account_no is not null)
    ;

    UPDATE dd_attendance_data dad
    JOIN dd_hrm_data_tmp dhd ON dhd.dd_emp_num = dad.employee_num
    SET dad.ts_excl_flag = dhd.dd_ts_excl_flag,
        dhd.job_status = 1
    WHERE date(dad.attendance_date) >= date(sysdate());

    DELETE FROM dd_hrm_data_tmp WHERE job_status = 1;

  END;


-- ----------------------------
-- 公司管理
-- ----------------------------
ALTER TABLE company ADD company_type        int NULL COMMENT '0.文德分公司 1.文德子公司 2.外部公司' AFTER company_address;
ALTER TABLE company ADD credit_no           varchar(45) NULL COMMENT '统一社会信用代码' AFTER status;
ALTER TABLE company ADD credit_image_path   varchar(128) NULL COMMENT '统一社会信用代码证件照路径' AFTER credit_no;
ALTER TABLE company ADD bank_name           varchar(45) NULL COMMENT '银行名称' AFTER credit_image_path;
ALTER TABLE company ADD bank_id             varchar(45) NULL COMMENT '银行帐号' AFTER bank_name;
ALTER TABLE company ADD sub_branch_name     varchar(45) NULL COMMENT '开户支行名称' AFTER bank_id;
ALTER TABLE company ADD legal_name          varchar(45) NULL COMMENT '法人姓名' AFTER sub_branch_name;
ALTER TABLE company ADD legal_id            varchar(45) NULL COMMENT '法人证件号' AFTER legal_name;
ALTER TABLE company ADD legal_mobile        varchar(20) NULL COMMENT '法人手机号(仅支持国内运营商)' AFTER legal_id;
ALTER TABLE company ADD legal_id_front_path varchar(128) NULL COMMENT '法人证件正面照下载地址' AFTER legal_mobile;
ALTER TABLE company ADD legal_bank_card_no  varchar(45) NULL COMMENT '法人银行卡号' AFTER legal_id_front_path;

ALTER TABLE company ADD customer_id     varchar(50) NULL COMMENT '法大大客户编号' AFTER legal_bank_card_no;
ALTER TABLE company ADD auth_status     int DEFAULT 0 NULL
  COMMENT '法大大实名认证状态：0：未认证；1：管理员资料已提交；2：企业基本资料(没有申请表)已提交；3：已提交待审核；4：审核通过；5：审核不通过；6 人工初审通过。'
  AFTER customer_id;
ALTER TABLE company ADD auth_date       datetime         null       comment '认证时间，法大大认证通过后回调时更新' after auth_status;

ALTER TABLE company ADD verify_url      varchar(512)  NULL COMMENT '法大大实名认证地址' AFTER auth_date;
ALTER TABLE company ADD transaction_no  varchar(50)   NULL COMMENT '法大大实名认证交易号（获取url时返回）' AFTER verify_url;
ALTER TABLE company ADD serial_no       varchar(50)   NULL COMMENT '法大大异步回调认证序列号' AFTER transaction_no;
ALTER TABLE company ADD status_desc     varchar(256)  NULL COMMENT '法大大不通过原因描述' AFTER serial_no;
ALTER TABLE company ADD cert_status     int           NULL COMMENT '法大大申请证书状态：0.没有申请证书或者申请证书失败，1.成功申请证书' AFTER status_desc;

ALTER TABLE company ADD create_user     bigint        NULL COMMENT '创建者系统用户Id' AFTER cert_status;
ALTER TABLE company ADD modify_user     bigint        NULL COMMENT '修改者系统用户Id' AFTER create_time;

ALTER TABLE company MODIFY status int(1) DEFAULT '1' COMMENT '0.停用 1.正常' AFTER company_type;


-- ----------------------------
-- 公司印章
-- ----------------------------
drop table if exists sign_company_signature;
create table sign_company_signature
(
  signature_id               bigint auto_increment   comment '主键,自增' primary key,
  company_id                 bigint             null comment '企业id',
  signature_name             varchar(50)        null comment '印章名称',

  fdd_signature_id           varchar(50)        null comment '法大大字段，签章图片 ID',
  fdd_signature_type         int                null comment '法大大字段，1.个人 2.企业',
  fdd_signature_img_base64   mediumblob         null comment '法大大字段，签章图片 base64',
  fdd_signature_scope        int                null comment '法大大字段，签章图片作用范围：0 非默认；1：默认章',
  fdd_signature_sub_info     varchar(50)        null comment '法大大字段，扩展信息，目前为空',

  create_time                datetime           null comment '创建时间',
  create_by_user_id          bigint             null comment '创建用户ID',
  modify_time                datetime           null comment '修改时间',
  modify_by_user_id          bigint             null comment '修改用户ID'
) comment '公司印章';


