/**
 * 2020年1月7日
 * 下午1:48:18 
 *
 */
package com.vendetech.project.system.thread;

import java.util.concurrent.Callable;

/**
 * @author chuguoqiang
 * 
 *         wendetech 2020年1月7日 下午1:48:18
 */
public class UserJobCallableThread implements Callable<String> {

    @Override
	public String call() throws Exception {
		System.out.println("进入CallableThread的call()方法, 开始睡觉, 睡觉时间为" + System.currentTimeMillis());
		Thread.sleep(3000);
		return "123";
	}
}
