package com.vendetech.job.service.impl.kit;

public class SignScheduleTaskKit {
	/**已过期*/
	public static final int RECORD_STATUS_INVALID = -1;
	/**待签署*/
	public static final int RECORD_STATUS_NOT_SIGN = 0;
	/**已签署*/
	public static final int RECORD_STATUS_SIGNED = 1;
	/**已到期*/
	public static final int RECORD_STATUS_EXPIRED = 2;

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
