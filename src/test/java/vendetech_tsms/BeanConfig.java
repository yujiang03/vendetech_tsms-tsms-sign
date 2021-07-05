/**
 * 2020年1月3日
 * 下午3:34:55 
 *
 */
package vendetech_tsms;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author chuguoqiang
 * 
 * wendetech 2020年1月3日 下午3:34:55
 */
//@ComponentScan
//@ComponentScan(value = "vendetech_tsms")
public class BeanConfig {
	
	
//	@Test
	public void testComponentScan() {
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(BeanConfig.class);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            System.out.println("beanName: " + beanName);
        }
    }
	
}

