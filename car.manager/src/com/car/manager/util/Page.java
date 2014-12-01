package com.car.manager.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Page<T> extends com.jfinal.plugin.activerecord.Page<T>{
	
	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引
	
	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int slider = 1;// 前后显示页面长度
	
	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	private int length = 8;
	
	public Page(List<T> list, int pageNumber, int pageSize, int totalPage, int totalRow) {
		super(list, pageNumber, pageSize, totalPage, totalRow);
	}
	
	
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		
		
	}
	
	/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 */
	@Override
	public String toString() {

		initialize();
		
		StringBuilder sb = new StringBuilder();
		
		if (getPageNumber() == first) {// 如果是首页
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+prev+","+getPageSize()+");\">&#171; 上一页</a></li>\n");
		}

		int begin = getPageNumber() - (length  / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+getPageSize()+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == getPageNumber()) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+getPageSize()+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+getPageSize()+");\">"
					+ (i + 1 - first) + "</a></li>\n");
		}

		if (getPageNumber() == last) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+next+","+getPageSize()+");\">"
					+ "下一页 &#187;</a></li>\n");
		}

		sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">当前 ");
		sb.append("<input type=\"text\" value=\""+getPageNumber()+"\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"(this.value,"+getPageSize()+");\" onclick=\"this.select();\"/> / ");
		sb.append("<input type=\"text\" value=\""+getPageSize()+"\" onkeypress=\"var e=window.event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"("+getPageNumber()+",this.value);\" onclick=\"this.select();\"/> 条，");
		sb.append("共 " + getTotalRow() + " 条</a><li>\n");

		sb.insert(0,"<ul>\n").append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");

//		sb.insert(0,"<div class=\"page\">\n").append("</div>\n");
		
		return sb.toString();
	}
	
//	public static void main(String[] args) {
//		Page<String> p = new Page<String>(3, 3);
//		System.out.println(p);
//		System.out.println("首页："+p.getFirst());
//		System.out.println("尾页："+p.getLast());
//		System.out.println("上页："+p.getPrev());
//		System.out.println("下页："+p.getNext());
//	}


	/**
	 * 首页索引
	 * @return
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * @return
	 */
	public int getLast() {
		return last;
	}
	
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	public int getTotalPage() {
		return getLast();
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * @return
	 */
	public boolean isLastPage() {
		return lastPage;
	}
	
	/**
	 * 上一页索引值
	 * @return
	 */
	public int getPrev() {
		if (isFirstPage()) {
			return getPageNumber();
		} else {
			return getPageNumber() - 1;
		}
	}

	/**
	 * 下一页索引值
	 * @return
	 */
	public int getNext() {
		if (isLastPage()) {
			return getPageNumber();
		} else {
			return getPageNumber() + 1;
		}
	}
	

	/**
	 * 获取点击页码调用的js函数名称
	 * function ${page.funcName}(getPageNumber()){location="${ctx}/list-${category.id}${urlSuffix}?getPageNumber()="+i;}
	 * @return
	 */
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	/**
	 * 分页是否有效
	 * @return this.getPageSize()==-1
	 */
	public boolean isDisabled() {
		return this.getPageSize()==-1;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNumber() - 1) * getPageSize();
		if (firstResult >= getTotalRow()) {
			firstResult = 0;
		}
		return firstResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}

	
	 
}
