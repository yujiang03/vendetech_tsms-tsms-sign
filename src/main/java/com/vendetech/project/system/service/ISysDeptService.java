package com.vendetech.project.system.service;

import java.util.List;

import com.vendetech.framework.web.domain.TreeSelect;
import com.vendetech.project.system.domain.SysDept;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.DeptListVo;

/**
 * 部门管理 服务层
 * 
 * @author vendetech
 */
public interface ISysDeptService {
	/**
	 * 查询部门管理数据
	 * 
	 * @param dept 部门信息
	 * @return 部门信息集合
	 */
	public List<SysDept> selectDeptList(SysDept dept);

	/**
	 * 构建前端所需要树结构
	 * 
	 * @param depts 部门列表
	 * @return 树结构列表
	 */
	public List<SysDept> buildDeptTree(List<SysDept> depts);

	/**
	 * 构建前端所需要下拉树结构
	 * 
	 * @param depts 部门列表
	 * @return 下拉树结构列表
	 */
	public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

	/**
	 * 根据角色ID查询部门树信息
	 * 
	 * @param roleId 角色ID
	 * @return 选中部门列表
	 */
	public List<Integer> selectDeptListByRoleId(Long roleId);

	/**
	 * 根据部门ID查询信息
	 * 
	 * @param deptId 部门ID
	 * @return 部门信息
	 */
	public SysDept selectDeptById(Long deptId);

	/**
	 * 是否存在部门子节点
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	public boolean hasChildByDeptId(Long deptId);

	/**
	 * 查询部门是否存在用户
	 * 
	 * @param deptId 部门ID
	 * @return 结果 true 存在 false 不存在
	 */
	public boolean checkDeptExistUser(Long deptId);

	/**
	 * 校验部门名称是否唯一
	 * 
	 * @param dept 部门信息
	 * @return 结果
	 */
	public String checkDeptNameUnique(SysDept dept);

	/**
	 * 新增保存部门信息
	 * 
	 * @param dept 部门信息
	 * @return 结果
	 */
	public int insertDept(SysDept dept);

	/**
	 * 修改保存部门信息
	 * 
	 * @param dept 部门信息
	 * @return 结果
	 */
	public int updateDept(SysDept dept);

	/**
	 * 删除部门管理信息
	 * 
	 * @param deptId 部门ID
	 * @return 结果
	 */
	public int deleteDeptById(Long deptId);

	/**
	 * @param currentUser
	 * @return
	 */
	public List<Department> getDepartmentList(SysUser currentUser);

	/**
	 * @param departmentId
	 * @return
	 */
	public List<Department> getDepartmentList(String ddDepartmentId);

	/**
	 *  构建前端路由所需要的菜单(react 用)
	 * 
	 * @param menus 菜单列表
	 * @return 路由列表
	 */
	public List<DeptListVo> buildDepts(List<Department> depts);
}
