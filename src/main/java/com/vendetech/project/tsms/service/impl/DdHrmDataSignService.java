package com.vendetech.project.tsms.service.impl;

import com.vendetech.project.tsms.domain.DdHrmDataSign;
import com.vendetech.project.tsms.mapper.DdHrmDataSignMapper;
import com.vendetech.project.tsms.service.IDdHrmDataSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DdHrmDataSignService implements IDdHrmDataSignService {

    @Autowired
    DdHrmDataSignMapper ddHrmDataSignMapper;

    @Override
    public int batchInsertHrmDataSign(List<DdHrmDataSign> ddHrmData) {
        return ddHrmDataSignMapper.batchInsertHrmDataSign(ddHrmData);
    }

    @Override
    public DdHrmDataSign getHrmDataByJobNumber(String employeeNum) {
        return ddHrmDataSignMapper.selectByJobNumber(employeeNum);
    }

    @Override
    public int truncateHrmDataSign() {
        return ddHrmDataSignMapper.truncateHrmDataSign();
    }


}
