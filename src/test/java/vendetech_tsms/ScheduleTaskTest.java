package vendetech_tsms;

import com.taobao.api.ApiException;
import com.vendetech.VendetechApplication;
import com.vendetech.project.tsms.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {VendetechApplication.class})
@ActiveProfiles("dev")
public class ScheduleTaskTest {

    @Autowired
    private UserService userService;

	@Test
	public void updateSysUserByTask() throws ApiException {
		userService.updateSysUserByTask();
	}


	// @Test
	// public void updateSysUserByTask()

}
