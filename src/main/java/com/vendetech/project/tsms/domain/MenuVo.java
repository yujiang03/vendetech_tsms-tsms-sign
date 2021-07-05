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
public class MenuVo {

    /**
     * 路由名字
     */
    private String name;

    /**
     * 路由地址
     */
    private String path;
    
    /**
     *  图标
     */
    private String icon;
    
    /**
     * 组件地址
     */
    private String component;

    /**
     * 子路由
     */
    private List<MenuVo> routes;
    
    
    /**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }


    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    /**
	 * @return the routes
	 */
	public List<MenuVo> getRoutes() {
		return routes;
	}

	/**
	 * @param routes the routes to set
	 */
	public void setRoutes(List<MenuVo> routes) {
		this.routes = routes;
	}

}
