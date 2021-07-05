package com.vendetech.hr.mapper;

import com.vendetech.hr.vo.SignCompanySignature;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SignCompanySignatureMapper {

    List<SignCompanySignature> selectSelective(SignCompanySignature record);

    SignCompanySignature selectByPrimaryKey(Long signatureId);

    int deleteByPrimaryKey(Long signatureId);

    int insert(SignCompanySignature record);

    int insertSelective(SignCompanySignature record);

    int updateByPrimaryKeySelective(SignCompanySignature record);

    int updateByPrimaryKeyWithBLOBs(SignCompanySignature record);

    int updateByPrimaryKey(SignCompanySignature record);

    int updateSelective(SignCompanySignature updateParams);
}