package ${package}.log;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import io.microlam.logging.LambdaHandler;
import ${package}.params.SystemParameters;

public class LoggingConfiguration {

	public static boolean configure() {
		try {
			LogManager logManager = LogManager.getLogManager();
			logManager.reset();
			System.setProperty("java.util.logging.SimpleFormatter.format", "%2$s %4$s: %5$s%6$s");
			logManager.getLogger("").addHandler(new LambdaHandler(false /* useJsonFormating */));
			
			return updateLogLevel(logManager);
		}
		catch(Exception ex) {
			System.err.println("Cannot configure logManager!");
			ex.printStackTrace();
		}
		return false;
	}

	private static boolean updateLogLevel(LogManager logManager) {
		String debug = SystemParameters.load().getStringValue("DEBUG", "false");
		boolean isDebug = Boolean.parseBoolean(debug);
		Logger rootLogger = logManager.getLogger("");
		rootLogger.setLevel((isDebug)?Level.FINE:Level.INFO);
		return isDebug;
	}

	public static boolean updateLogLevel() {
		try {
			LogManager logManager = LogManager.getLogManager();
			return updateLogLevel(logManager);
		}
		catch(Exception ex) {
			System.err.println("Cannot configure logManager!");
			ex.printStackTrace();
		}
		return false;
	}

}
