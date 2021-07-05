package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;

public class AssignmentNameDTO {
    private String assignmentName;

    private Long tsAsgnId;

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public Long getTsAsgnId() {
        return tsAsgnId;
    }

    public void setTsAsgnId(Long tsAsgnId) {
        this.tsAsgnId = tsAsgnId;
    }
}
