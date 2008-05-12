/*******************************************************************************
 * Copyright (c) 2004, 2008 IBM Corporation and Others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kentarou FUKUDA - initial API and implementation
 *******************************************************************************/

package org.eclipse.actf.visualization.engines.blind.html.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;

import org.eclipse.actf.model.ui.editor.browser.IWebBrowserACTF;
import org.eclipse.actf.util.FileUtils;
import org.eclipse.actf.visualization.engines.blind.BlindVizEnginePlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HandleFramePage {

	public static boolean hasFrameset(Document document,
			IWebBrowserACTF webBrowser) {

		NodeList framesetNl = document.getElementsByTagName("frameset");

		if (framesetNl.getLength() > 0) {

			NodeList frameList = document.getElementsByTagName("frame");

			String sFileName = BlindVizEnginePlugin.getTempDirectory()
					+ "frameList.html";

			String base = webBrowser.getURL();

			try {
				URL baseURL = new URL(base);

				NodeList baseNL = document.getElementsByTagName("base");
				if (baseNL.getLength() > 0) {
					Element baseE = (Element) baseNL
							.item(baseNL.getLength() - 1);
					String baseUrlS = baseE.getAttribute("href");
					if (baseUrlS.length() > 0) {
						URL tmpUrl = new URL(baseURL, baseUrlS);
						base = tmpUrl.toString();
					}
				}
			} catch (Exception e) {
			}

			PrintWriter fileOutput;

			try {
				fileOutput = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(sFileName), "UTF-8"));
			} catch (IOException e) {
				// e.printStackTrace();
				// TODO
				return true;
			}

			fileOutput.write("<html>");
			// " lang=\""+lang+\">"); //use var
			fileOutput.write("<head>" + FileUtils.LINE_SEP);
			fileOutput
					.write("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" >"
							+ FileUtils.LINE_SEP);
			fileOutput.write("<base href=\"" + base + "\"></head>"
					+ FileUtils.LINE_SEP + "<body><P>");
			fileOutput.write("This page contains of "); // var
			fileOutput.write(String.valueOf(frameList.getLength()));
			fileOutput.write(" frames."); // var
			fileOutput.write("<br>" + FileUtils.LINE_SEP);
			fileOutput.write("Please select one of them."); // var
			fileOutput.write("</P>" + FileUtils.LINE_SEP + "<ol>"
					+ FileUtils.LINE_SEP);

			String strTitle, strName;
			for (int i = 0; i < frameList.getLength(); i++) {
				Element frameEl = (Element) frameList.item(i);
				strTitle = frameEl.getAttribute("title");
				strName = frameEl.getAttribute("name");
				if (strTitle.equals(""))
					strTitle.equals("none");
				if (strName.equals(""))
					strName.equals("none");
				fileOutput.write("<li><a href=\"" + frameEl.getAttribute("src")
						+ "\">Title: \"" + strTitle + "\".<BR> Name: \""
						+ strName + "\".<BR> src: \""
						+ frameEl.getAttribute("src") + "\".</a>"
						+ FileUtils.LINE_SEP);
			}
			fileOutput.write("</ol></body></html>");

			fileOutput.flush();
			fileOutput.close();

			webBrowser.navigate(sFileName);
			return true;

		} else {
			return false;
		}
	}
}
