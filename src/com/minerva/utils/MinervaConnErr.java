package com.minerva.utils;

public class MinervaConnErr extends Exception {
	
	int errcode;
	String errdisc;
	
	public MinervaConnErr(int errcode) {
		this.errcode = errcode;
		errdisc = null;
	}
	
	public int errorCode() {
		return errcode;
	}
	
	public void setErrDescription(String disc) {
		errdisc = disc;
	}
	
	public String errorDescription() {
		return errdisc;
	}
}
