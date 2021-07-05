/**
 * 2020年1月7日
 * 上午10:09:08 
 *
 */
package com.vendetech.project.system.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chuguoqiang
 * 
 * wendetech 2020年1月7日 上午10:09:08
 */
public class UserJobThread extends Thread {
	/**
	 * @Description: TODO(logger this class)
	
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(UserJobThread.class);
	
	@Override
	public void run() {
		
		logger.info("UserJobThread start...");
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 1/0;
	}

}
