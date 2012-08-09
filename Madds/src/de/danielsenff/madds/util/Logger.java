package de.danielsenff.madds.util;

import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple log writing class.
 * 
 * This logger implements basic functionality for logging messages to
 * System.out
 * 
 * @author m.kandora
 *
 */
public class Logger {
	
	private static Map<Class<?>, Logger> logger = new HashMap<Class<?>, Logger>();
	
	private PrintStream printer;
	private boolean enabled;
	private Class<?> reference;
	
//	private String format = "class %s : %s";
	
	private Logger(final Class<?> reference) {
		this.reference 	= reference;
		this.enabled 	= true;
		this.printer	= System.err;
	}
	
	/**
	 * Returns a logger for the specific type
	 * 
	 * @param type
	 * @return a new Logger
	 */
	public static Logger getLogger(final Class<?> type) {
		if(logger.containsKey(type)) {
			return logger.get(type);
		}
		Logger log = new Logger(type);
		logger.put(type, log);
		return log;
	}
	
	/**
	 * Inserts a sepeator between the logs
	 */
	public void separator() 
	{
		this.log("------------------------------------");
	}
	
	/**
	 * Writes a log entry to stdout
	 * 
	 * @param message
	 */
	public void log(final Object message) {
		this.log(message, null);
	}
	
	
	/**
	 * Writes a log entry to stdout, prints the stacktrace if any existent
	 * @param message
	 * @param throwable
	 */
	public void log(final Object message, Throwable throwable) {
		this.log(message, throwable, getPrintStream());
	}
	
	public void log(final Object message, Throwable throwable, PrintStream stream) {
		if(!enabled) return; 
		System.out.println(getCaller() + ":");
		System.out.println("\t" + getNow() + " ->" + message);
		if(throwable != null) {
			throwable.printStackTrace();
		}
	}

	protected Date getNow() {
		return Calendar.getInstance().getTime();
	}
	
	private PrintStream getPrintStream() {
		return printer;
	}
	
	public void setPrintStream(final PrintStream stream) {
		this.printer = stream;
	}
	
	/**
	 * Experimental function to determine the caller of 
	 * {@link #log(Object)} or {@link #log(Object, Throwable)}
	 * 
	 * @return the calling class method as string
	 */
	protected StackTraceElement getCaller() {
		StackTraceElement[] frame = Thread.currentThread().getStackTrace();
		int index;
		// skip first -> current thread
		for(index=1; index < frame.length; index++) {
			if(!(frame[index].getClassName().equals(getClass().getName()))) {
				break;
			}
		}
		return frame[index];
	}
	
	/** 
	 * enables / disables this logger
	 * @param b
	 */
	public void enable(final boolean b) {
		this.enabled = b;
		System.err.println("Logger for Class: " + getReference() + " has been " + (enabled ? "enabled" : "disabled"));
	}
	
	private Class<?> getReference() {
		return reference;
	}
}
