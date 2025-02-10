package com.dlz.comm.exception;

/**
 * 数据库操作异常
 * 
 * @author dingkui
 *
 */
public class DbException extends BaseException {
	private static final long serialVersionUID = 1L;
//	static {
//		ExceptionErrors.addErrors(1000, "数据库连接异常");
//		ExceptionErrors.addErrors(1001, "数据库执行sql异常");
//		ExceptionErrors.addErrors(1002, "数据库参数校验异常");
//		ExceptionErrors.addErrors(1003, "数据库执行其他异常");
//		ExceptionErrors.addErrors(1004, "数据库结果异常");
//		ExceptionErrors.addErrors(1005, "数据转换异常");
//	}

	public DbException(String message, int errorCode, Throwable cause) {
		super(errorCode, message, cause);
	}
	public DbException(String message, int errorCode) {
		super(errorCode, message, null);
	}
}
