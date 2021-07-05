package com.vendetech.project.tsms.timesheet.mapper;

import com.vendetech.project.tsms.domain.Calendars;

public interface CalendarsMapper {

	int deleteByPrimaryKey(Long id);

	int insert(Calendars record);

	int insertCalendars(Calendars record);

	int insertSelective(Calendars record);

	Calendars selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Calendars record);

	int updateByPrimaryKey(Calendars record);
}