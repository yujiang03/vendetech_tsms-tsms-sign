package com.vendetech.hr.service.impl.kit;

public class SignKit {
	/**已过期*/
	public static final int RECORD_STATUS_INVALID = -1;
	/**人事审核不通过*/
	public static final int RECORD_STATUS_VERIFY_FAIL = -2;
	/**员工待签署*/
	public static final int RECORD_STATUS_NOT_SIGN = 0;
	/**员工已签约，待人事审核*/
	public static final int RECORD_STATUS_EMPL_SIGNED = 1;
	/**人身审核通过，待行政用章*/
	public static final int RECORD_STATUS_VERIFY_PASS = 2;
	/**双方已签署*/
	public static final int RECORD_STATUS_SIGNED = 3;
	/**已到期*/
	public static final int RECORD_STATUS_EXPIRED = 4;
	/**已撤销*/
	public static final int RECORD_STATUS_CANCELED = 5;

	/** 作废 */
	public static final int TPL_STATUS_INVALID = -1;
	/** 草稿 */
	public static final int TPL_STATUS_DRAFT = 0;
	/** 已发布 */
	public static final int TPL_STATUS_PUBLISHED = 1;
	/** 已过期 */
	public static final int TPL_STATUS_EXPIRED = 2;

	public static final String TPL_IS_UPLOAD_YES = "Y";
	public static final String TPL_IS_UPLOAD_NO = "N";

}
