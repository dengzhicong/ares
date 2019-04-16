package com.ares.log;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ares.file.FileOperate;
import com.ares.time.TimeUtils;

public class LogUtil {

	private static final String thisClassName = LogUtil.class.getName();
	// private static final String msgSep = " -\r\n";
	private static Logger logger = null;

	static {
		logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
	}

	private static String getStackMsg(Object msg) {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if (ste == null) {
			return "";
		}

		boolean srcFlag = false;
		for (int i = 0; i < ste.length; i++) {
			StackTraceElement s = ste[i];
			// 如果上一行堆栈代码是本类的堆栈，则该行代码则为源代码的最原始堆栈。
			if (srcFlag) {
				if (s == null) {
					return "";
				}
				StringBuffer sb = new StringBuffer("(");
				sb.append(s.getFileName());
				sb.append(":");
				sb.append(s.getLineNumber());
				sb.append(")");
				// sb.append(msgSep);
				sb.append("-");
				sb.append(msg.toString());
				return sb.toString();
			}
			// 定位本类的堆栈
			if (thisClassName.equals(s.getClassName())) {
				srcFlag = true;
				i++;
			}
		}
		return "";
	}

	public static void info(Object msg, Object... v) {
		String message = getStackMsg(msg);
		if (Level.OFF.intLevel() == logger.getLevel().intLevel()) {
			writeLog(message, "log.log", v);
			return;
		}
		logger.info(message, v);
	}

	public static void warn(Object msg, Object... v) {
		String message = getStackMsg(msg);
		if (Level.OFF.intLevel() == logger.getLevel().intLevel()) {
			writeLog(message, "log-warn.log", v);
			return;
		}
		logger.warn(message, v);
	}

	public static void error(Object msg, Object... v) {
		String message = getStackMsg(msg);
		if (Level.OFF.intLevel() == logger.getLevel().intLevel()) {
			writeLog(message, "log-error.log", v);
			return;
		}
		logger.error(message, v);
	}

	public static void errorEx(Object msg, String ex) {
		String message = getStackMsg(msg);
		logger.error(message + ex);
	}

	/**
	 * 当level为OFF时，需要手动写入日志
	 */
	private static void writeLog(String message, String fileName, Object... v) {
		String time = TimeUtils.format2string(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:SS");
		String str = message.replace("{}", "%s");
		String content = time + " " + String.format(str, v);
		String path = System.getProperty("user.dir") + File.separator + "logs" + File.separator + fileName;
		FileOperate.writeTxtFile(content, path);
	}
}
