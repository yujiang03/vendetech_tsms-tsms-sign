-- ----------------------------
-- 公司管理_授权自动签
-- ----------------------------
ALTER TABLE company ADD auth_sign_transaction_id  varchar(50)   NULL  COMMENT '法大大授权自动签交易号' AFTER cert_status;
ALTER TABLE company ADD auth_sign_contract_no     varchar(50)   NULL  COMMENT '法大大授权自动签合同号' AFTER auth_sign_transaction_id;
ALTER TABLE company ADD auth_sign_view_pdf        varchar(512)  NULL  COMMENT '法大大授权自动签授权书查看地址' AFTER auth_sign_contract_no;
ALTER TABLE company ADD auth_sign_result_desc     varchar(256)  NULL  COMMENT '法大大授权自动签签章接口描述' AFTER auth_sign_view_pdf;
ALTER TABLE company ADD auth_sign_status          int DEFAULT '0'     COMMENT '授权自动签状态: 0.未授权 1.已授权' AFTER auth_sign_result_desc;

