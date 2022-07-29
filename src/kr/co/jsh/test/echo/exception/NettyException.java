package kr.co.jsh.test.echo.exception;

import java.util.Map;

public class NettyException extends RuntimeException{
	
	//°´Ã¼±¸ºÐ¿ë
	private static final long serialVersionUID = 1L;
	
	private Map<String, String> errorMap;
	
	public NettyException(String message, Map<String, String> errorMap) {
		super(message);
		this.errorMap = errorMap;
	}
	
	public Map<String, String> getErrorMap() {
		return errorMap;
	}
}
