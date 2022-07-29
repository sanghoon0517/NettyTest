package kr.co.jsh.test.echo.exception.handler;

import kr.co.jsh.test.echo.exception.NettyException;

public class NettyExceptionHandler {
	
	public String nettyException(NettyException e) {
		return e.toString();
	}

}

//д©╫╨ер Response DTO
class CMResDto<T> {
	private int code;
	private String message;
	private T data;
}