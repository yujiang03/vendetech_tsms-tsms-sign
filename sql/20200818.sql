create procedure insertUpdateDepatmentProcess()
  BEGIN

    declare ddDepartmentIdTemp BIGINT(20);
    declare ddDepartmentFullname VARCHAR(45);
    declare ddDepartmentCode VARCHAR(45);
    declare ddDepartmentParentId BIGINT(20);
    declare ddDepartmentParentCode VARCHAR(45);
    declare ddDepartmentOwnerId VARCHAR(225);
    declare createTime datetime;

    declare departmentId BIGINT(20);
    declare departmentNum VARCHAR(45);
    declare departmentName VARCHAR(45);
    declare ddDepartmentId VARCHAR(45);
    declare managerNum VARCHAR(99);
    declare parentDepartmentNum VARCHAR(45);
    declare existFlag int(1);
    declare empExistFlag int(1);
    declare userId bigint(20);

    declare deptStatus int(1);

    declare flag int(1) default 0;

    declare deptTempCursor cursor for(
                                     SELECT dd_department_id,
                                            dd_department_fullname,
                                            dd_department_code,
                                            dd_department_parent_id,
                                            dd_department_parent_code,
                                            dd_department_owner_id,
                                            create_time
                                     FROM dd_department_tmp
                                     where job_status = 0);

    declare deptCursor cursor for(
                                 SELECT dd_department_id
                                 FROM department
                                 where `status` = 1);

    declare continue handler for not found set flag = 1;

    open deptCursor;

    FETCH deptCursor
    INTO ddDepartmentId;

    while flag <> 1 do

      select count(*)
          into existFlag
      from dd_department_tmp
      where dd_department_id = ddDepartmentId
        and job_status = 0;

      if existFlag = 0 then

        UPDATE department
        SET status = 0,
            modify_time = now()
        WHERE dd_department_id = ddDepartmentId;

      end if;

      set flag = 0;

      # 重新 fetch数据
      FETCH deptCursor
      INTO ddDepartmentId;

    end while;

    close deptCursor;

    set flag = 0;

    open deptTempCursor;

    FETCH deptTempCursor
    INTO ddDepartmentIdTemp,
      ddDepartmentFullname,
      ddDepartmentCode,
      ddDepartmentParentId,
      ddDepartmentParentCode,
      ddDepartmentOwnerId,
      createTime;

    while flag <> 1 do

      select count(*)
          into existFlag
      from department
      where dd_department_id = ddDepartmentIdTemp;

      SELECT count(*), GROUP_CONCAT(employee_num)
          into empExistFlag,managerNum
      from employee
      where FIND_IN_SET(dd_userid, ddDepartmentOwnerId)
        AND STATUS != 3;

      --			if empExistFlag > 0 then

      --				select count(*),GROUP_CONCAT(user_id)
      --				  into empExistFlag,userId
      --				  from sys_user
      --				 where FIND_IN_SET(login_name, managerNum);

      --			end if;

      if existFlag = 0 then
        INSERT INTO department
            (department_num,
             department_name,
             dd_department_id,
             manager_num,
             parent_department_id,
             create_time)
        VALUES
               (ddDepartmentCode,
                ddDepartmentFullname,
                ddDepartmentIdTemp,
                managerNum,
                ddDepartmentParentId,
                NOW());

      else
        set deptStatus = 1;
        if (ddDepartmentFullname = ddDepartmentCode) THEN
          set deptStatus = 0;
        end if;
        UPDATE department
        SET department_name       = ddDepartmentFullname,
            department_num        = ddDepartmentCode,
            dd_department_id      = ddDepartmentIdTemp,
            manager_num           = managerNum,
            parent_department_id  = ddDepartmentParentId,
            status                = deptStatus,
            modify_time           = NOW()
        WHERE dd_department_id = ddDepartmentIdTemp;
      end if;

      update dd_department_tmp
      set job_status = 1
      where dd_department_id = ddDepartmentIdTemp
        and create_time = createTime;

      set flag = 0;

      # 重新 fetch数据
      FETCH deptTempCursor
      INTO ddDepartmentIdTemp,
        ddDepartmentFullname,
        ddDepartmentCode,
        ddDepartmentParentId,
        ddDepartmentParentCode,
        ddDepartmentOwnerId,
        createTime;

    end while;
    close deptTempCursor;

  END;

