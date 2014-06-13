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

import java.util.ResourceBundle;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.progress.UIJob;

import eu.musoft.eclipse.xpath.evaluation.plugin.views.GUIException;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.XPathEvaluationView;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.components.Notification;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces.NamespacesTable;

public class NamespaceLoadTrigger implements SelectionListener {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");

	private Combo query;
	private NamespacesTable namespacesTable;

	public NamespaceLoadTrigger(Combo query, NamespacesTable namespacesTable) {
		this.query = query;
		this.namespacesTable = namespacesTable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		// widget selected should be triggered only by Button widget
		if (e.getSource() instanceof Button) {
			query.forceFocus(); // used to hide any tooltip if previously visible
			loadNamespaces(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private void loadNamespaces(SelectionEvent event) {
		String xml = null;
		try {
			xml = XPathEvaluationView.getActiveTextEditorContent();
		} catch (final GUIException e) {
			new UIJob(bundle.getString("label.namespace.loader")) {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					Notification.showToolTip(SWT.BALLOON | SWT.ICON_ERROR, bundle.getString("label.error"), e.getMessage(), query);
					return Status.OK_STATUS;
				}
			}.schedule();

			return;
		}

		// execute the Namespace loader job (new thread will be created!)
		new NamespaceLoadJob(xml, namespacesTable, query).schedule();
	}

}
