/**
 * Copyright (c) 2014, Martin Uhlir
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * The name of the author may not be used to endorse or promote
 *       products derived from this software without specific prior written
 *       permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.musoft.eclipse.xpath.evaluation.plugin.views.listeners;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import net.sf.saxon.tree.NamespaceNode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

import eu.musoft.eclipse.xpath.evaluation.plugin.views.components.Notification;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces.Namespace;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces.NamespacesTable;

/**
 * This class loads all namespaces used in the XML. The task will run in a new thread while invoking the 'schedule()' method on this class instance.
 */
public class NamespaceLoadJob extends Job {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

	private String xml;
	private NamespacesTable namespacesTable;
	private Control component;

	/**
	 * 
	 * @param xml
	 *          XML to inspect for provided namespaces
	 * @param namespacesTable
	 *          table which contains namespace prefixes and URIs
	 * @param component
	 *          a component where to place a notification tooltip over
	 */
	public NamespaceLoadJob(String xml, NamespacesTable namespacesTable, final Control component) {
		super(bundle.getString("label.namespace.loader"));

		this.xml = xml;
		this.namespacesTable = namespacesTable;
		this.component = component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			Map<String, String> allNamespacesMap = getAllNamespacesMap();
			updateNamespacesTableData(allNamespacesMap);
			outputResult();
		} catch (final Exception e) {
			new UIJob(bundle.getString("label.namespace.loader")) {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					Notification.showToolTip(SWT.BALLOON | SWT.ICON_ERROR, bundle.getString("label.error"), e.getMessage(), component);
					return Status.OK_STATUS;
				}
			}.schedule();
		}
		return Status.OK_STATUS;
	}

	private Map<String, String> getAllNamespacesMap() throws XPathExpressionException, XPathFactoryConfigurationException {
		XPathExpression xPathExpression = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI, "net.sf.saxon.xpath.XPathFactoryImpl", null).newXPath().compile("//namespace::*");
		List<NamespaceNode> nodeList = (ArrayList<NamespaceNode>) xPathExpression.evaluate(new StreamSource(new StringReader(xml)), XPathConstants.NODESET);
		Map<String, String> allNamespacesMap = new TreeMap<String, String>();
		if (nodeList != null) {
			for (int i = 0; i < nodeList.size(); i++) {
				NamespaceNode node = nodeList.get(i);

				if (!allNamespacesMap.containsKey(node.getLocalPart())) {
					allNamespacesMap.put(node.getLocalPart(), node.getStringValue());
				}
			}
		}

		return allNamespacesMap;
	}

	private void updateNamespacesTableData(Map<String, String> allNamespacesMap) {
		List<Namespace> currentNamespaces = namespacesTable.getNamespaces();
		currentNamespaces.clear();

		for (Map.Entry<String, String> entry : allNamespacesMap.entrySet()) {
			Namespace ns = new Namespace();
			ns.setPrefix(entry.getKey());
			ns.setURI(entry.getValue());
			currentNamespaces.add(ns);
		}
		currentNamespaces.add(new Namespace());
	}

	private void outputResult() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				namespacesTable.refresh();
			}
		});
	}

}
