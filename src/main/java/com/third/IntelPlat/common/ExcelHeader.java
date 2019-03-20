package com.third.IntelPlat.common;

public class ExcelHeader implements Comparable<ExcelHeader> {
	public static final String FILE_TYPE_XLSX = ".xlsx";
	public static final String FILE_TYPE_XLS = ".xls";
	
	private String tilte;
	private int order;
	private String methodName;

	public ExcelHeader() {
	}

	public ExcelHeader(String tilte, int order, String methodName) {
		super();
		this.tilte = tilte;
		this.order = order;
		this.methodName = methodName;
	}

	public String getTilte() {
		return tilte;
	}

	public void setTilte(String tilte) {
		this.tilte = tilte;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 用于对ExcelHeader进行排序
	 * */
	public int compareTo(ExcelHeader o) {
		return this.order > o.order ? 1 : (this.order < o.order ? -1 : 0);
	}

	@Override
	public String toString() {
		return "ExcelHeader [tilte=" + tilte + ", order=" + order+ ", methodName=" + methodName + "]";
	}

}
