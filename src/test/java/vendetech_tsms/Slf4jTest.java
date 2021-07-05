/**
 * 2020年1月2日
 * 下午1:37:07 
 *
 */
package vendetech_tsms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chuguoqiang
 * 
 *         wendetech 2020年1月2日 下午1:37:07
 */
public class Slf4jTest {
//	@Test
	public void testSlf4j() {
		Logger logger = LoggerFactory.getLogger(Slf4jTest.class);
		logger.trace("=====trace=====");
		logger.debug("=====debug=====");
		logger.info("=====info=====");
		logger.warn("=====warn=====");
		logger.error("=====error=====");
	}
}
