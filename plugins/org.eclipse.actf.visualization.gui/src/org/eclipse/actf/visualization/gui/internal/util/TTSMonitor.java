/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Takashi ITOH - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.gui.internal.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.actf.util.win32.RegistryUtil;
import org.eclipse.actf.visualization.gui.ui.views.IMSAAEventView;

public class TTSMonitor extends Thread {

	private IMSAAEventView eventView;
	private boolean repeat = false;

	private static int port = getMonitorPort();// 5556;

	private static TTSMonitor gMonitor = null;

	public static void startThread(IMSAAEventView eventView) {
		if (null == gMonitor && port > 0) {
			gMonitor = new TTSMonitor(eventView);
			gMonitor.start();
		}
	}

	public static void stopThread() {
		if (null != gMonitor) {
			gMonitor.repeat = false;
			gMonitor.interrupt();
		}
	}

	public TTSMonitor(IMSAAEventView eventView) {
		this.eventView = eventView;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			repeat = true;
			while (repeat) {
				Socket socket = serverSocket.accept();
				if (null != socket) {
					PrintWriter out = new PrintWriter(socket.getOutputStream(),
							true);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(socket.getInputStream()));

					StringBuffer sb = new StringBuffer();
					try {
						String inputLine;
						while ((inputLine = in.readLine()) != null) {
							sb.append(inputLine);
							sb.append("\n"); //$NON-NLS-1$
						}
					} catch (IOException e) {
					} finally {
						if (sb.length() > 0) {
							eventView.setTTSText(sb.toString());
						}
						out.close();
						in.close();
						socket.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != serverSocket) {
				try {
					serverSocket.close();
				} catch (Exception e) {

				}
			}
		}
	}

	private static int getMonitorPort() {
		return RegistryUtil
				.getRegistryInt(
						RegistryUtil.HKEY_LOCAL_MACHINE,
						"SOFTWARE\\Microsoft\\Speech\\Voices\\Tokens\\AcTFTTS\\Settings", //$NON-NLS-1$
						"MonitorPort"); //$NON-NLS-1$
	}

}
