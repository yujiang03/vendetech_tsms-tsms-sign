package com.vendetech.project.tsms.project.mapper;

import com.vendetech.project.tsms.domain.Company;

import java.util.List;

public interface CompanyMapper {

    Company selectByPrimaryKey(Long companyId);

    List<Company> selectSelective(Company record);

    List<Company> getCompanyNameList();

    Company getCompanyId(String companyName);

    int updateByPrimaryKey(Company record);

    int updateByPrimaryKeySelective(Company record);

    int insert(Company record);

    int insertSelective(Company record);

    int deleteByPrimaryKey(Long companyId);
}