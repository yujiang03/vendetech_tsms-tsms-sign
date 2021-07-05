-- ----------------------------
-- 字段扩容, 注释逻辑变更
-- ----------------------------
ALTER TABLE dd_hrm_data_sign MODIFY dd_bank_account_no varchar(50) COMMENT '扩展字段，银行卡号，field_code：sys04-bankAccountNo';
ALTER TABLE dd_hrm_data_sign MODIFY dd_account_bank varchar(50) COMMENT '扩展字段，开户行，field_code：sys04-accountBank';
ALTER TABLE dd_hrm_data_sign MODIFY dd_probation_period_type varchar(10) COMMENT '合同字段, 员工试用期, 自定义字段field_name：员工试用期; 因为是钉钉选项字段, 故值应取label;'