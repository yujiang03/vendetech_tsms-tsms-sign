package com.vendetech.job.controller;

import com.vendetech.job.service.ISignScheduleTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling   // 开启定时任务
public class SignScheduleTask {
	@Autowired
	private ISignScheduleTaskService signScheduleTaskService;


	@Scheduled(fixedRate = 60000) // 60秒跑一次
	private void notifyEmployeeSignRecord() {
		// log.info("=========定时任务开始=========");
		// 法大大员工账号注册
		// log.info("任务1：为员工注册法大大账号，获取customer_id");
		signScheduleTaskService.registerFddAccount();
		// 企业合同模板上传
		// log.info("任务2：把已发布的合同模板上传至法大大，更新isupload");
		signScheduleTaskService.uploadContractTpl();
		// 查找所有待签约员工并写入sign_record
		// log.info("任务3：把协议配置的内容写到协议记录（协议查询）里");
		signScheduleTaskService.createSignRecords();
		// 甲方签署
		// log.info("任务4：甲方签署");
		// signScheduleTaskService.signByPlatform();
		// log.info("=========定时任务结束=========");
	}

    @Scheduled(fixedRate = 120000) // 120秒跑一次
	private void downloadPdf() {
		signScheduleTaskService.downloadPDFNotSync();
	}
}
