/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Junji MAEDA - initial API and implementation
 *******************************************************************************/
package org.eclipse.actf.visualization.engines.lowvision.internal.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class DebugUtil {
	// the string for expressing directories
	private static final String DRIVE_DELIM = ":"; // for DOS related OSs

	// *************************************************************
	// to be changed according to the debugging phase
	// public static String DEBUG_STRING = "style-addcss-page-post-cache-diff";
	// private static final String DEBUG_STRING =
	// "post-genRE-infoSet-diff-cache";
	private static final String DEBUG_STRING = "TRACE";

	// private static final String DEBUG_STRING =
	// "TRACE_HISTOGRAM_CONTAINER_EXTRACT_PROBLEM_COMPONENT_REPORT";

	// (1)print utilities
	public static void errMsg(Object o, String s) {
		printMsg(o, s, System.err, 0);
	}

	public static void errMsg(Object o, String s, int level) {
		printMsg(o, s, System.err, level);
	}

	public static void outMsg(Object o, String s) {
		printMsg(o, s, System.out, 0);
	}

	public static void outMsg(Object o, String s, int level) {
		printMsg(o, s, System.out, level);
	}

	public static void debugMsg(Object o, String s, String debugType) {
		if (DEBUG_STRING.indexOf(debugType) > -1) {
			printMsg(o, s, System.out, 0);
		}
	}

	public static void debugMsg(Object o, String s, String debugType, int level) {
		if (DEBUG_STRING.indexOf(debugType) > -1) {
			printMsg(o, s, System.out, level);
		}
	}

	public static void fileMsg(Object o, String s, String fileName)
			throws FileNotFoundException {
		fileMsg(o, s, "", fileName, 0, false);
	}

	public static void fileMsg(Object o, String s, String debugType,
			String fileName) throws FileNotFoundException {
		fileMsg(o, s, debugType, fileName, 0, true);
	}

	public static void fileMsg(Object o, String s, String debugType,
			String fileName, int level) throws FileNotFoundException {
		fileMsg(o, s, debugType, fileName, level, true);
	}

	public static void fileMsg(Object o, String s, String debugType,
			String fileName, int level, boolean checkString)
			throws FileNotFoundException {
		if (checkString && DEBUG_STRING.indexOf(debugType) == -1) {
			return;
		}

		String pathName = "";
		if (isAbsolutePath(fileName)) {
			pathName = fileName;
		} else {
			errMsg(o, "Please provide log file name in absolute path.");
			return;
		}
		File logFile = new File(pathName);

		// checking the condition of the log file
		if (logFile.exists()) {
			if (!logFile.canWrite()) {
				errMsg(o, "Cannot create the log file: " + pathName, 1);
				return;
			}
		} else { // the log file doesn't exist
			String parent = logFile.getParent();
			if (parent == null)
				parent = System.getProperty("user.dir");
			File dir = new File(parent);
			if (!dir.exists()) {
				errMsg(o,
						"Cannot create log files in the directory(does not exist): "
								+ parent, 1);
				return;
			}
			if (dir.isFile()) {
				errMsg(o,
						"Cannot create log files in the directory(it is a file): "
								+ parent, 1);
				return;
			}
			if (!dir.canWrite()) {
				errMsg(o,
						"Cannot create log files in the directory(unwritable): "
								+ parent, 1);
				return;
			}
		}

		PrintStream ps = new PrintStream(new FileOutputStream(pathName, true),
				true);
		printMsg(o, s, ps, level);
	}

	public static void printMsg(Object o, String s, PrintStream ps, int level) {
		if (level != 0) {
			ps.println("--------------------------------");
		}
		if (o != null) {
			ps.println(o.getClass().getName() + ": " + s);
		} else {
			ps.println(s);
		}
		if (level != 0) {
			ps.println("--------------------------------");
		}
		ps.flush();
	}

	public static void printArray(Object o, Object[] array, String s,
			PrintStream ps) {
		ps.println("----- printing an array (from here) -----");
		int count = array.length;
		if (o != null) {
			ps.print(o.getClass().getName() + ": ");
		}
		ps.print(s + ": ");
		ps.println("length= " + count);
		for (int i = 0; i < count; i++) {
			ps.println(array[i].toString());
		}
		ps.println("----- printing an array (to here) -----");
	}

	public static void outArray(Object o, Object[] array, String s) {
		printArray(o, array, s, System.out);
	}

	public static void errArray(Object o, Object[] array, String s) {
		printArray(o, array, s, System.err);
	}

	public static void printVector(Object o, Vector vector, String s,
			PrintStream ps) {
		ps.println("----- printing a Vector (from here) -----");
		int count = vector.size();
		if (o != null) {
			ps.print(o.getClass().getName() + ": ");
		}
		ps.print(s + ": ");
		ps.println("size= " + count);
		for (int i = 0; i < count; i++) {
			ps.println(vector.elementAt(i).toString());
		}
		ps.println("----- printing a Vector (to here) -----");
	}

	public static void outVector(Object o, Vector vector, String s) {
		printVector(o, vector, s, System.out);
	}

	public static void errVectir(Object o, Vector vector, String s) {
		printVector(o, vector, s, System.err);
	}

	private static boolean isAbsolutePath(String pathName) {
		// "/a/..."
		if (pathName.startsWith(File.separator)) {
			return (true);
		}
		// "c:\\..."
		if (Character.isLetter(pathName.charAt(0))
				&& pathName.indexOf(DRIVE_DELIM) == 1
				&& pathName.indexOf(File.separator) == 2) {
			return (true);
		}
		return (false);
	}

}
