/**
 * 2019年11月8日
 * 下午3:04:54 
 *
 */
package com.vendetech.project.tsms.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author chuguoqiang
 * 
 * wendetech 2019年11月8日 下午3:04:54
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeptListVo {

    /**
     * 路由名字
     */
    private String title;

    /**
     * 路由地址
     */
    private String value;    

    /**
     * 子路由
     */
    private List<DeptListVo> children;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the children
	 */
	public List<DeptListVo> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<DeptListVo> children) {
		this.children = children;
	}
    
}
