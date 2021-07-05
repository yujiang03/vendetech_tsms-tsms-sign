package com.vendetech.project.tsms.service;

import com.vendetech.project.tsms.domain.DdHrmDataSign;

import java.util.List;

public interface IDdHrmDataSignService {

    int batchInsertHrmDataSign(List<DdHrmDataSign> ddHrmData);

    DdHrmDataSign getHrmDataByJobNumber(String employeeNum);

    int truncateHrmDataSign();
}
