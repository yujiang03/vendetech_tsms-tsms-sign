package com.vendetech.hr.service.impl.kit;

import com.vendetech.common.utils.security.Md5Utils;

public class PasswordKit {

	public static String encryptPassword(String username, String password, String salt) {
		return Md5Utils.hash(username + password + salt);
	}
}
