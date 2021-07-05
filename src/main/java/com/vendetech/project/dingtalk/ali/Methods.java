package com.vendetech.project.dingtalk.ali;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetcolumnvalRequest;
import com.dingtalk.api.request.OapiAttendanceGetleavetimebynamesRequest;
import com.dingtalk.api.request.OapiDepartmentListRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeListRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQuerydimissionRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQueryonjobRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQuerypreentryRequest;
import com.dingtalk.api.request.OapiUserListbypageRequest;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse.ColumnValForTopVo;
import com.dingtalk.api.response.OapiAttendanceGetleavetimebynamesResponse;
import com.dingtalk.api.response.OapiDepartmentListResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse.EmpFieldInfoVO;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListResponse.EmpFieldVO;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerydimissionResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQueryonjobResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerypreentryResponse;
import com.dingtalk.api.response.OapiUserListbypageResponse;
import com.dingtalk.api.response.OapiUserListbypageResponse.Userlist;
import com.taobao.api.ApiException;
import com.taobao.api.internal.util.StringUtils;
import com.vendetech.project.tsms.domain.DdHrmDataSign;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Methods {

    /**
     * 获取系统时间 参数 a 1 带时分秒 2 日期 3 年月 0 日期加时分
     */
    public static String time(int a) {
        String date = "";
        if (a == 1) {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        } else if (a == 2) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        } else if (a == 3) {
            date = new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime());
        } else if (a == 0) {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
        }
        return date;
    }

    /**
     * 获取企业部门
     */
    public static List<String> getDepartments() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/list");
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setHttpMethod("GET");
        OapiDepartmentListResponse response = client.execute(request, AuthHelper.getAccessToken());
        List<String> departments = new ArrayList<String>();
        if (response.getErrcode() == 0) {
            for (int i = 0; i < response.getDepartment().size(); i++) {
//				response.getDepartment().get(i).getId();
                departments.add(response.getDepartment().get(i).getId().toString());
            }
        } else {
            departments = null;
            throw new ApiException(response.getErrmsg());
        }
        return departments;
    }

    /**
     * 获取部门人员信息
     */
    public static List<Map<String, Object>> getUsers() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");
        OapiUserListbypageRequest request = new OapiUserListbypageRequest();
        List<String> departmentIds = getDepartments();
        List<Map<String, Object>> userlist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < departmentIds.size(); i++) {
            for (int j = 0; j < 5; j++) {    // 假设部门下属人员都不超过500
                request.setDepartmentId(Long.parseLong(departmentIds.get(i)));
                request.setOffset(100 * (long) j);
                request.setSize(100L);
                request.setOrder("entry_desc");
                request.setHttpMethod("GET");
                OapiUserListbypageResponse execute = client.execute(request, AuthHelper.getAccessToken());
                if (execute.getUserlist().size() < 1) {
                    break;
                }
                for (int x = 0; x < execute.getUserlist().size(); x++) {
                    Userlist duser = execute.getUserlist().get(x);


                    String extattr = duser.getExtattr();
                    String[] attrs = null;
                    String company = null;
                    if (null != extattr) {
                        extattr = extattr.replace("{", "").replace("}", "");
                        attrs = extattr.split(",");
                        for (String str : attrs) {
                            if (str.startsWith("所属公司")) {
                                company = str.replace("所属公司=", "");
                                break;
                            }
                        }
                    }

                    Map<String, Object> usermap = new HashMap<String, Object>();
                    usermap.put("empId", duser.getJobnumber());
                    usermap.put("userId", duser.getUserid());
                    usermap.put("name", duser.getName());
                    usermap.put("mobile", duser.getMobile());
                    usermap.put("avatar", duser.getAvatar());
                    usermap.put("status", duser.getActive() ? 1 : 0);
                    usermap.put("email", duser.getOrgEmail());
                    usermap.put("depts", duser.getDepartment());
                    usermap.put("company", company);
                    userlist.add(usermap);
                }
            }
        }
        return removeDuplicateWithOrder(userlist);
    }

    /**
     * 获取部门人员userid
     */
    public static List<String> getUserids() throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/user/listbypage");
        OapiUserListbypageRequest request = new OapiUserListbypageRequest();
        List<String> departmentIds = getDepartments();
        List<String> userlist = new ArrayList<String>();
        for (int i = 0; i < departmentIds.size(); i++) {
            for (int j = 0; j < 5; j++) {    // 假设部门下属人员都不超过500
                request.setDepartmentId(Long.parseLong(departmentIds.get(i)));
                log.debug(departmentIds.get(i));
                request.setOffset(100 * (long) j);
                request.setSize(100L);
                request.setOrder("entry_desc");
                request.setHttpMethod("GET");
                OapiUserListbypageResponse execute = client.execute(request, AuthHelper.getAccessToken());
                if (execute.getUserlist() == null) {
                    log.warn("execute.getUserlist()  isnull");
                    continue;
                }
                for (int x = 0; x < execute.getUserlist().size(); x++) {
                    Userlist duser = execute.getUserlist().get(x);
                    String empNum = duser.getJobnumber();
                    if (null != empNum && !"".equals(empNum.trim())) {
                        userlist.add(duser.getUserid());
                    }
                }
            }
        }
        return removeDuplicateString(userlist);
    }

    /**
     * 获取员工考勤信息
     */
    public static List<Map<String, Object>> getAttendances(List<String> userids, String fromDate, String toDate)
            throws ApiException {
//		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/isopensmartreport");
//		OapiAttendanceIsopensmartreportRequest request = new OapiAttendanceIsopensmartreportRequest();
//		request.setHttpMethod("POST");
//		OapiAttendanceIsopensmartreportResponse execute = client.execute(request, AuthHelper.getAccessToken());
//		boolean smartFlag = execute.getResult().getSmartReport();
//		System.out.println(smartFlag);
//		if (smartFlag) {
        System.out.println(new Date());
        System.out.println("DD employ qty" + userids.size());
        List<Map<String, Object>> ddAttendanceData = new ArrayList<Map<String, Object>>();
//		String cols = "65148833,65148810,65148829";	// 考勤结果, 应出勤天数, 加班-审批单统计 
        String cols = "65148833,65148810,65148815,65148813,65148829";    // 考勤结果, 应出勤天数(天), 工作时长(分), 出勤天数(天), 加班-审批单统计(小时)
        if (null != userids && userids.size() > 0) {
            for (String userid : userids) {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getcolumnval");
                OapiAttendanceGetcolumnvalRequest request = new OapiAttendanceGetcolumnvalRequest();
                request.setUserid(userid);
                request.setColumnIdList(cols);    // 考勤结果, 应出勤天数(天), 工作时长(分), 出勤天数(天), 加班-审批单统计(小时)
                request.setFromDate(StringUtils.parseDateTime(fromDate));
                request.setToDate(StringUtils.parseDateTime(toDate));
                OapiAttendanceGetcolumnvalResponse execute = client.execute(request, AuthHelper.getAccessToken());
                if (null != execute & null != execute.getResult()) {
                    List<ColumnValForTopVo> results = execute.getResult().getColumnVals();
                    // System.out.println(execute.getBody());
                    int length = results.get(0).getColumnVals().size();
                    for (int i = 0; i < length; i++) {
                        Date attdDate = results.get(0).getColumnVals().get(i).getDate();
                        // 考勤结果:正常,休息,休息并打卡,加班12-22 09:00到12-22 18:00 8小时
                        String attdResult = results.get(0).getColumnVals().get(i).getValue();
                        // 应出勤天数
                        String dutyDay = results.get(1).getColumnVals().get(i).getValue();
                        // 工作时长
//						String workTime = results.get(2).getColumnVals().get(i).getValue();
                        // 出勤天数
                        String attdDay = results.get(3).getColumnVals().get(i).getValue();
                        // 加班-审批单统计
                        String allowedOtHours = results.get(4).getColumnVals().get(i).getValue();
                        Map<String, Object> attdRcd = new HashMap<String, Object>();
                        attdRcd.put("userid", userid);
                        attdRcd.put("attdDate", attdDate);
                        attdRcd.put("attdResult", attdResult);
                        attdRcd.put("dutyDay", dutyDay);
                        attdRcd.put("attdDay", attdDay);
                        attdRcd.put("allowedOtHours", allowedOtHours);
                        ddAttendanceData.add(attdRcd);
                        //					List<DdAttendanceData> ddAttendanceData = new ArrayList<DdAttendanceData>();
                    }
                }
            }
        }
        System.out.println(new Date());
        System.out.println(ddAttendanceData.size());
        return ddAttendanceData;
//		return removeDuplicateWithOrder(ddAttendanceData);
    }

    /**
     * 获取员工请假信息
     */
    public static List<Map<String, Object>> getLeaves(List<String> userids, String fromDate, String toDate)
            throws ApiException {
        System.out.println(new Date());
        System.out.println("DD employ qty" + userids.size());
        List<Map<String, Object>> ddLeaveData = new ArrayList<Map<String, Object>>();
        if (null != userids && userids.size() > 0) {
            for (String userid : userids) {
                DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavetimebynames");
                OapiAttendanceGetleavetimebynamesRequest req = new OapiAttendanceGetleavetimebynamesRequest();
//				req.setUserid("160635502121412782");
                req.setUserid(userid);
                req.setLeaveNames("调休,年假,事假,病假");
                req.setFromDate(StringUtils.parseDateTime(fromDate));
                req.setToDate(StringUtils.parseDateTime(toDate));
                OapiAttendanceGetleavetimebynamesResponse response = client.execute(req, AuthHelper.getAccessToken());
                if (null != response & null != response.getResult()) {
                    List<OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo> results = response.getResult().getColumns();
                    int length = results.get(0).getColumnvals().size();
                    for (int i = 0; i < length; i++) {
                        Date attdDate = results.get(0).getColumnvals().get(i).getDate();
                        String xchangeOffHours = results.get(0).getColumnvals().get(i).getValue();
                        Integer exchangeOffHours = Math.round(Float.valueOf(xchangeOffHours));
                        String anlLeaveHours = results.get(1).getColumnvals().get(i).getValue();
                        Integer annualLeaveHours = Math.round(Float.valueOf(anlLeaveHours));
                        String absLeaveHours = results.get(2).getColumnvals().get(i).getValue();
                        Integer absenceLeaveHours = Math.round(Float.valueOf(absLeaveHours));
                        String sckLeaveHours = results.get(3).getColumnvals().get(i).getValue();
                        Integer sickLeaveHours = Math.round(Float.valueOf(sckLeaveHours));
                        Map<String, Object> attdRcd = new HashMap<String, Object>();
                        attdRcd.put("userid", userid);
                        attdRcd.put("attdDate", attdDate);
                        attdRcd.put("exchangeOffHours", exchangeOffHours);
                        attdRcd.put("annualLeaveHours", annualLeaveHours);
                        attdRcd.put("absenceLeaveHours", absenceLeaveHours);
                        attdRcd.put("sickLeaveHours", sickLeaveHours);
                        ddLeaveData.add(attdRcd);
                    }
                }
            }
        }
        System.out.println(new Date());
        System.out.println(ddLeaveData.size());
        return ddLeaveData;
    }


    /**
     * 获取员工花名册信息
     */
    public static List<DdHrmDataSign> getHrmData(List<String> userIds) throws ApiException {
        System.out.println(new Date());
        System.out.println("DD employ qty: " + userIds.size());
        List<DdHrmDataSign> ddHrmData = new ArrayList<>();
        if (userIds.isEmpty()) {
            return ddHrmData;
        }

        for (String userId : userIds) {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/list");
            OapiSmartworkHrmEmployeeListRequest req = new OapiSmartworkHrmEmployeeListRequest();
            req.setUseridList(userId);
            req.setFieldFilterList("sys00-jobNumber,sys00-name,sys00-position,sys02-sexType,sys02-birthTime,sys02-certAddress,sys02-address," +
                    "sys02-certNo,sys00-mobile,sys05-nowContractStartTime,sys05-nowContractEndTime,sys00-mainDeptId," +
                    "sys00-mainDept,sys01-probationPeriodType,sys01-regularTime,sys01-planRegularTime,sys03-graduateSchool,sys00-confirmJoinTime," +
                    "sys00-email,sys04-bankAccountNo,sys04-accountBank,customField");
            OapiSmartworkHrmEmployeeListResponse execute = client.execute(req, AuthHelper.getAccessToken());
            if (null != execute && null != execute.getResult()) {
                List<EmpFieldInfoVO> result = execute.getResult();
                DdHrmDataSign hrmData = new DdHrmDataSign();
                hrmData.setDdUserId(userId);
                if (result.isEmpty()) {
                    break;
                }
                for (EmpFieldInfoVO info : result) {
                    List<EmpFieldVO> list = info.getFieldList();
                    for (EmpFieldVO field : list) {
                        String code = field.getFieldCode();
                        String name = field.getFieldName();
                        String value = field.getValue();
                        String label = field.getLabel();
                        if ("sys00-name".equals(code)) {    // 员工姓名
                            hrmData.setDdName(value);
                        }
                        if ("sys00-position".equals(code)) {    // 员工职位
                            hrmData.setDdPosition(value);
                        }
                        if ("sys00-jobNumber".equals(code)) {    // 员工工号
                            hrmData.setDdJobNumber(value);
                        }
                        if ("sys00-mainDeptId".equals(code) && !StringUtils.isEmpty(value)) {    // 主部门id
                            hrmData.setDdMainDeptId(Long.valueOf(value));
                        }
                        if ("sys00-mainDept".equals(code)) {    // 主部门
                            hrmData.setDdMainDept(value);
                        }
                        if ("sys00-confirmJoinTime".equals(code)) {    // 入职时间
                            hrmData.setDdConfirmJoinTime(value);
                        }
                        if ("所属公司".equals(name)) {
                            hrmData.setDdCompanyName(value);
                        }
                        if ("tsExclFlg".equals(name) && !StringUtils.isEmpty(value)) {  // tsExclFlg 项目分配排除标识
                            hrmData.setDdTsExclFlag(Integer.valueOf(value));
                        }
                        if ("sys02-sexType".equals(code) && !StringUtils.isEmpty(value)) {  // 性别
                            hrmData.setDdSexType(Integer.valueOf(value));
                        }
                        if ("sys02-birthTime".equals(code)) {    // 出生日期
                            hrmData.setDdBirthTime(value);
                        }
                        if ("sys02-certNo".equals(code)) {    // 身份证号
                            hrmData.setDdCertNo(value);
                        }
                        if ("sys02-certAddress".equals(code)) {    // 身份证地址
                            hrmData.setDdCertAddress(value);
                        }
                        if ("sys02-address".equals(code)) {    // 住址
                            hrmData.setDdAddress(value);
                        }
                        if ("sys00-mobile".equals(code)) {    //
                            hrmData.setDdMobile(value);
                        }
                        if ("工作城市".equals(name)) {    // 工作城市, 自定义字段
                            hrmData.setDdWorkCity(label);   // 自定义选项字段, 必须取label才是准确值
                        }
                        if ("sys05-nowContractStartTime".equals(code)) {    //
                            hrmData.setDdNowContractStartTime(value);
                        }
                        if ("sys05-nowContractEndTime".equals(code)) {    //
                            hrmData.setDdNowContractEndTime(value);
                        }
                        if ("sys01-probationPeriodType".equals(code)) {    // 试用期, 钉钉系统字段
                        // if ("员工试用期（电签用）".equals(name)) {    // 员工试用期, 自定义字段
                            hrmData.setDdProbationPeriodType(label); // 必须取label才是准确值
                        }
                        // if ("sys01-regularTime".equals(code)) {    // 实际转正日期
                        if ("sys01-planRegularTime".equals(code)) {    // 计划转正日期
                            hrmData.setDdRegularTime(value);
                        }
                        if ("sys03-graduateSchool".equals(code)) {    //
                            hrmData.setDdGraduateSchool(value);
                        }
                        if ("sys00-email".equals(code)) {    //
                            hrmData.setDdEmail(value);
                        }
                        if ("sys04-bankAccountNo".equals(code)) {    //
                            hrmData.setDdBankAccountNo(value);
                        }
                        if ("sys04-accountBank".equals(code)) {    //
                            hrmData.setDdAccountBank(value);
                        }
                    }
                }
                ddHrmData.add(hrmData);
            }
        }
        return ddHrmData;
    }


    /*
     * 将时间戳转换为时间
     */
    public static Date stampToDate(String s, int a) throws ParseException {
        SimpleDateFormat simpleDateFormat = null;
        Date date = null;
        if (a == 1) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = new Long(s);
            date = new Date(lt);
        } else if (a == 2) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s);
            date = new Date(lt);
        } else if (a == 3) {
            simpleDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
            date = simpleDateFormat.parse(s);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        } else if (a == 4) {
            simpleDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
            date = simpleDateFormat.parse(s);
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return date;
    }

    /**
     * list<Map> 去重
     */
    public static List<Map<String, Object>> removeDuplicateWithOrder(List<Map<String, Object>> list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    /**
     * list<String> 去重
     */
    public static List<String> removeDuplicateString(List<String> list) {
        Set<String> set = new HashSet<String>();
        List<String> newList = new ArrayList<String>();
        for (Iterator<String> iter = list.iterator(); iter.hasNext(); ) {
            String element = iter.next();
            if (set.add(element)) {
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
        return list;
    }

    public static String getHolidays(String httpArg) {
		// String httpUrl = "https://api.goseek.cn/Tools/holiday?date=";
        String httpUrl = "http://www.easybots.cn/api/holiday.php?d=";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + httpArg;
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
//			System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取企业待入职人员
     */
    public static List<String> queryPreentry(Long offset) throws ApiException {
        List<String> ddUserIdList = new ArrayList<String>();
        DingTalkClient client = new DefaultDingTalkClient(
                "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/querypreentry");
        OapiSmartworkHrmEmployeeQuerypreentryRequest req = new OapiSmartworkHrmEmployeeQuerypreentryRequest();
        req.setOffset(offset);
        req.setSize(50L); // 最大50，，否者报错
        OapiSmartworkHrmEmployeeQuerypreentryResponse response = client.execute(req, AuthHelper.getAccessToken());
		// System.out.println(response.getBody());
        if (0 == response.getErrcode()) {
            ddUserIdList = response.getResult().getDataList();
            Long nextOffset = response.getResult().getNextCursor();
            if (null != nextOffset && nextOffset != 0) {
                ddUserIdList.addAll(queryPreentry(nextOffset));
            }
        } else {
            throw new ApiException(response.getErrmsg());
        }
        return ddUserIdList;
    }

    /**
     * 获取企业在职人员
     */
    public static List<String> queryOnjob(Long offset) throws ApiException {
        List<String> ddUserIdList = new ArrayList<String>();
        DingTalkClient client = new DefaultDingTalkClient(
                "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/queryonjob");
        OapiSmartworkHrmEmployeeQueryonjobRequest req = new OapiSmartworkHrmEmployeeQueryonjobRequest();
        req.setStatusList("2,3,5");
        req.setOffset(offset);
        req.setSize(50L);
        OapiSmartworkHrmEmployeeQueryonjobResponse response = client.execute(req, AuthHelper.getAccessToken());
		// System.out.println(response.getBody());
        if (0 == response.getErrcode()) {
            ddUserIdList = response.getResult().getDataList();
            Long nextOffset = response.getResult().getNextCursor();
            if (null != nextOffset && nextOffset != 0) {
                ddUserIdList.addAll(queryOnjob(nextOffset));
            }
        } else {
            throw new ApiException(response.getErrmsg());
        }
        return ddUserIdList;
    }

    /**
     * 获取企业离职人员
     */
    public static List<String> queryDimission(Long offset) throws ApiException {
        List<String> ddUserIdList = new ArrayList<String>();
        DingTalkClient client = new DefaultDingTalkClient(
                "https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/querydimission");
        OapiSmartworkHrmEmployeeQuerydimissionRequest req = new OapiSmartworkHrmEmployeeQuerydimissionRequest();
        req.setOffset(offset);
        req.setSize(50L);
        OapiSmartworkHrmEmployeeQuerydimissionResponse response = client.execute(req, AuthHelper.getAccessToken());
		// System.out.println(response.getBody());
        if (0 == response.getErrcode()) {
            ddUserIdList = response.getResult().getDataList();
            Long nextOffset = response.getResult().getNextCursor();
            if (null != nextOffset && nextOffset != 0) {
                ddUserIdList.addAll(queryDimission(nextOffset));
            }
        } else {
            throw new ApiException(response.getErrmsg());
        }
        return ddUserIdList;
    }

    public static void main(String[] args) throws ApiException {

        List<String> ids = new ArrayList<>();
        // ids.add("033419004121963014");
        // ids.add("194349542224565301");
        // ids.add("202310140524045879");
        // ids.add("274803563226177928");
        // ids.add("28234017511233489");
        // ids.add("2841055960784012");
        // ids.add("263602516226606223");
        // ids.add("3110352802784700");
        // ids.add("283723622724246065");
        // ids.add("24612167301273056");
        // ids.add("300425216233486299");
        ids.add("311035695924139776");
        ids.add("16113016817832094");
        ids.add("16113029093652238");
        ids.add("16112985077154538");
        ids.add("16112203973446705");
        ids.add("282740016323373442");
        ids.add("16115693672221046");
        List<DdHrmDataSign> list = Methods.getHrmData(ids);

        for (DdHrmDataSign d : list) {
            System.out.println(d.getDdProbationPeriodType());
        }

        // System.out.println("0:"+new Date());
        // List<String> users = getUserids();	//new ArrayList<String>();
        // users.add("160635502121412782");
        // System.out.println(users.size());
        // System.out.println("1:"+new Date());
        // List<Map<String, Object>> attds = getAttendances(users, "2020-03-01 00:00:00", "2020-03-15 00:00:00");
        // System.out.println("2:"+new Date());
        // int size = attds.size();
        // System.out.println(size);
        // String cal = getHolidays("20200202");
        // System.out.println(cal);
        //
        // List<Map<String, Object>> users = getUsers();
        // for (Map<String, Object> user : users) {
        // 	System.out.println(user.get(""));
        // }
        //
        // DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getattcolumns");
        // OapiAttendanceGetattcolumnsRequest req = new OapiAttendanceGetattcolumnsRequest();
        // OapiAttendanceGetattcolumnsResponse rsp = client.execute(req, AuthHelper.getAccessToken());
        // System.out.println(rsp.getBody());

        // DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getleavetimebynames");
        // OapiAttendanceGetleavetimebynamesRequest req = new OapiAttendanceGetleavetimebynamesRequest();
        // req.setUserid("0331092735471898"); //232424510332365168"); //"160635502121412782");//"160635502121412782","213131023129079308");
        // req.setLeaveNames("调休,年假,事假,病假");
        // req.setFromDate(StringUtils.parseDateTime("2020-02-17 00:00:00"));
        // req.setToDate(StringUtils.parseDateTime("2020-02-17 00:00:00"));
        // OapiAttendanceGetleavetimebynamesResponse rsp = client.execute(req, AuthHelper.getAccessToken());
        // if (null != rsp & null != rsp.getResult()) {
        // 	List<OapiAttendanceGetleavetimebynamesResponse.ColumnValForTopVo> results = rsp.getResult().getColumns();
        // 	int length = results.get(0).getColumnvals().size();
        // 	for (int i = 0; i < length; i++) {
        // 		Date attdDate = results.get(0).getColumnvals().get(i).getDate();
        // 		String xchangeOffHours = results.get(0).getColumnvals().get(i).getValue();
        // 		Integer exchangeOffHours = Math.round(Float.valueOf(xchangeOffHours));
        // 		String anlLeaveHours = results.get(1).getColumnvals().get(i).getValue();
        // 		Integer annualLeaveHours = Math.round(Float.valueOf(anlLeaveHours));
        // 		String absLeaveHours = results.get(2).getColumnvals().get(i).getValue();
        // 		Integer absenceLeaveHours = Math.round(Float.valueOf(absLeaveHours));
        // 		String sckLeaveHours = results.get(3).getColumnvals().get(i).getValue();
        // 		Integer sickLeaveHours = Math.round(Float.valueOf(sckLeaveHours));
        // 		System.out.println(attdDate);
        // 		System.out.println(exchangeOffHours);
        // 		System.out.println(annualLeaveHours);
        // 		System.out.println(absenceLeaveHours);
        // 		System.out.println(sickLeaveHours);
        // 	}
        // }
        // System.out.println(rsp.getBody());
        // System.out.println("11:"+new Date());

        // 钉钉花名册接口
        // DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/list");
        // OapiSmartworkHrmEmployeeListRequest req = new OapiSmartworkHrmEmployeeListRequest();
        // req.setUseridList("304804553231971606, 160635502121412782, 043733064926264614");
        // req.setFieldFilterList("");
        // OapiSmartworkHrmEmployeeListResponse execute = client.execute(req, AuthHelper.getAccessToken());
        // System.out.println(execute.getErrorCode());
        // List<EmpFieldInfoVO> resutls = execute.getResult();
        // if (null != resutls && resutls.size() > 0) {
        // 	for (EmpFieldInfoVO info : resutls) {
        // 		List<EmpFieldVO> list = info.getFieldList();
        // 		for(EmpFieldVO field : list) {
        // 			String code = field.getFieldCode();
        // 			String name = field.getFieldName();
        // 			if ("sys00-mainDeptId".equals(code)) {	// 主部门id
        // 				System.out.println(field.getValue());
        // 			}
        // 			if ("sys00-mainDept".equals(code)) {	// 主部门
        // 				System.out.println(field.getValue());
        // 			}
        // 			if ("sys00-confirmJoinTime".equals(code)) {	// 入职时间
        // 				System.out.println(field.getValue());
        // 			}
        // 			if ("所属公司".equals(name)) {
        // 				System.out.println(field.getValue());
        // 			}
        // 			if ("tsExclFlg".equals(name)) {	// tsExclFlg
        // 				System.out.println(field.getValue());
        // 			}
        // 			if ("工作城市".equals(name)) {	// tsExclFlg
        // 				System.out.println("工作城市: " + field.getValue());
        // 			}
        // 		}
        // 		String ddDptId = list.get(0).getValue();
        // 		System.out.println(ddDptId);
        // 		String ddDptName = list.get(1).getValue();
        // 		System.out.println(ddDptName);
        // 	}
        // }
        // System.out.println(execute.getBody());
        // System.out.println(Methods.queryPreentry(0L).size());
        // System.out.println(Methods.queryOnjob(0L).size());
        // System.out.println(Methods.queryDimission(0L).size());
        // List<String> list = null;
        // for (String str : list) {
        //     System.out.println(str);
        // }
    }

}
