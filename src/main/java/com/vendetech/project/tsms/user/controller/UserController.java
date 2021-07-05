package com.vendetech.project.tsms.user.controller;

import com.vendetech.framework.web.controller.BaseController;

//@RestController
//@RequestMapping("user")
public class UserController extends BaseController {
//    protected final Logger logger = LoggerFactory.getLogger(UserController.class);
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoleService roleService;
//
//    @Autowired
//    private EmployeeService employeeService;
//    /**
//     * 项目列表
//     * @return
//     */
//    @GetMapping(value = "getUserList")
//    public TableDataInfo list(UserDTO userDTO) {
//        //开始分页
//        startPage();
//        //列表展示
//        List<HashMap<String, Object>> hashMaps = userService.selectByUserList(userDTO.getKeyword());
//        return getDataTable(hashMaps);
//    }
//
//    /**
//     * 重置密码
//     * @throws MessagingException
//     * @throws UnsupportedEncodingException
//     */
//    @PutMapping("resetPwd")
//    public R resetPwd(Long userId) throws UnsupportedEncodingException, MessagingException
//    {
//        if(userId != null){
//            SysUser user1 = userService.selectByEmail(userId);
//            SysUser sysUser = new SysUser();
//            System.err.println(user1.getEmail());
//            sysUser.setSalt(RandomUtil.randomStr(6));
//            String password = GetRamdom.getRamdom();
//            System.err.println(password);
//            sysUser.setPassword(PasswordUtil.encryptPassword(sysUser.getLoginName(), password, sysUser.getSalt()));
////            SendEmailUtil.sendMail(user1.getEmail(), null, null, "重置密码", "新密码："+password);
//            MailUtils.sendEmail(user1.getEmail(),password);
// //           sysUser.setPassword(SecurityUtil.encryptPassword(password));
//            sysUser.setUserId(userId);
//            userService.resetPwd(sysUser);
//            return R.ok("密码重置成功");
//        }
//
//        return R.error("重置密码失败");
//    }
//
//    /***
//     * 添加员工姓名模糊查询
//     * @param
//     * @return
//     */
//    @GetMapping("getSaveEmployeeName")
//    public R getSaveEmployeeName(Employee  employee){
//        List<Employee> lists = employeeService.selectByEmployeeName(employee.getEmployeeName());
//        return R.data(lists);
//    }
//
//    /**
//     * 添加账号
//     * @param createUserDTO
//     * @return
//     */
//    @PostMapping("saveAccount")
//    @ResponseBody
//    public R saveAccount(@RequestBody CreateUserDTO createUserDTO){
//        if(createUserDTO != null){
//            userService.saveAccount(createUserDTO);
//            return R.ok("添加成功");
//        }
//        return R.error("添加失败");
//    }
//
//    /**
//     * 角色下拉
//     * @return
//     */
//    @GetMapping("getRoleList")
//    public R getRoleList(){
//        List<SysRole> lists= roleService.selectRoleList();
//        return R.data(lists);
//    }
//
//
//    /***
//     * 修改
//     * @param createUserDTO
//     * @return
//     */
//    @PutMapping("updateRoleByUserId")
//    @ResponseBody
//    public R  updateRoleByUserId(@RequestBody CreateUserDTO createUserDTO){
//        if(createUserDTO != null){
//            userService.updateRoleByUserId(createUserDTO);
//            return R.ok("修改成功");
//        }
//
//        return R.error("修改失败");
//    }
//
//    /**
//     * 删除
//     * @param userId
//     * @return
//     */
//    @DeleteMapping("deleteByUserId")
//    public R deleteByUserId(Long userId){
//        if(userId  != null){
//            userService.deleteByUserId(userId);
//            return R.ok("删除成功");
//        }
//        return R.error("删除失败");
//    }
//
//
//    /***
//     * 修改回显
//     * @param userId
//     * @return
//     */
//    @GetMapping("modifyUserList")
//    public R modifyUserList(Long userId){
//        if(userId != null){
//            SysUserRole user = userService.selectByUserId(userId);
//            return R.data(user);
//        }
//        return R.error("查询失败");
//    }
//
//    /***
//     * 项目经理角色模糊查询
//     * @return
//     */
//    @GetMapping("selectByRole")
//    public R selectByRole(SysUser sysUser){
//        List<HashMap<String, Object>> users = userService.selectByRole(sysUser.getUserName());
//        return R.data(users);
//    }
//
//    /***
//     * 修改用户密码
//     * @param userDTO
//     * @return
//     */
//    @PutMapping("updatePassword")
//    @ResponseBody
//    public R updatePassword(@RequestBody UserDTO userDTO){
//        SysUser user = userService.selectByPassword(userDTO.getUserId());
//        boolean matches = PasswordUtil.matches(user, user.getPassword());
//        if(userDTO.getOldPassWord()){
//            return R.error("修改密码失败,旧密码错误");
//        }
//        if(password.equals(userDTO.getNewPassWord())){
//            return R.error("新密码不能与旧密码相同");
//        }
//        if(!password.equals(userDTO.getOldPassWord())
//                &&!password.equals(userDTO.getNewPassWord())){
//            userService.updatePassword(userDTO);
//            return R.ok("修改密码成功");
//        }
//        return R.error("修改密码失败");
//    }
//
//    @GetMapping("getByUserName")
//    public R getByUserName(){
//        SysUser currentUser = this.getSysUser();
//        List<UserNameDTO> byUserName = userService.getByUserName(currentUser.getLoginName());
//        return R.data(byUserName);
//    }
}
