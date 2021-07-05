package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;

public class TimesheetAssignmentDetailByAssignTypeDTO
{
    private String keyword;

    private String assignType;

    private Long tsAsgnId;

    public Long getTsAsgnId() {
        return tsAsgnId;
    }

    public void setTsAsgnId(Long tsAsgnId) {
        this.tsAsgnId = tsAsgnId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAssignType() {
        return assignType;
    }

    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }
}
