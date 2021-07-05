package com.vendetech.job.service;

public interface ISignScheduleTaskService {
	void registerFddAccount();
	void uploadContractTpl();
	void createSignRecords();
	// void signByPlatform();

    void downloadPDFNotSync();
}
