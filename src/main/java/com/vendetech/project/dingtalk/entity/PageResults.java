/**    
* @{#} PageResults.java Create on 2015年7月26日 上午1:49:57    
* Copyright (c) 2013 by efetion.    
*/
package com.vendetech.project.dingtalk.entity;

import java.util.List;

/**
 * @version 1.0
 * @description
 */
public class PageResults<T> {

	// 当前页
	private int pageNo;

	// 起始下标
	private int start;

	// 每页个个数
	private int pageSize;

	// 总条数
	private int totalCount;

	// 总页数
	private int pageCount;

	// 记录
	private List<T> results;

	public PageResults() {
	}

	public PageResults(int page, int size) {
		setPageNo(page);
		setPageSize(size);
	}

	public int getStart() {
		return (pageNo - 1) * pageSize + 1;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo <= 0 ? 1 : pageNo;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void resetPageNo() {
		pageCount = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
	}
}
