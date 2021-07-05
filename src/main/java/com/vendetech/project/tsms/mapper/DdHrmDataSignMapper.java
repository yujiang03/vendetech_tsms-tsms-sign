package com.vendetech.project.tsms.mapper;

import com.vendetech.project.tsms.domain.DdHrmDataSign;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface DdHrmDataSignMapper {

    int insertSelective(DdHrmDataSign record);

    int updateSelective(DdHrmDataSign record);

    int batchInsertHrmDataSign(List<DdHrmDataSign> ddHrmData);

    DdHrmDataSign selectByJobNumber(@Param("ddJobNumber") String jobNumber);

    @Update("truncate table dd_hrm_data_sign")
    int truncateHrmDataSign();
}