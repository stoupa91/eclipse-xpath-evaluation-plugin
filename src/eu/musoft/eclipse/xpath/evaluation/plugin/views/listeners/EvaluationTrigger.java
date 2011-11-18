/**
 * Copyright (c) 2011, Martin Uhlir
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;

import eu.musoft.eclipse.xpath.evaluation.plugin.Activator;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.GUIException;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.XPathEvaluationView;

/**
 * This class takes care of triggering the evaluation process by registering it
 * on XPath query combo box and execute queyr button.
 */
public class EvaluationTrigger implements SelectionListener {

	private Combo query;
	private Button prettyPrint;
	private Text result;

	public EvaluationTrigger(Combo query, Button prettyPrint, Text result) {
		this.query = query;
		this.prettyPrint = prettyPrint;
		this.result = result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
	 * .events.SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {
		// widget selected should be triggered only by Button widget
		if (e.getSource() instanceof Button) {
			evaluate(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected(SelectionEvent e) {
		// widget default selected should be triggered only by Combo widget
		if (e.getSource() instanceof Combo) {
			evaluate(e);
		}
	}

	private void evaluate(SelectionEvent event) {
		// load input data used in evaluation process
		String xpath = query.getText();
		String xml = null;
		try {
			xml = XPathEvaluationView.getActiveTextEditorContent();
		} catch (final GUIException e) {
			new UIJob("XPath evaluation") {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					Status errorStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage());
					return errorStatus;
				}
			}.schedule();

			return;
		}

		// execute the XPath evaluation (new thread will be created!)
		new EvaluationJob(xpath, xml, prettyPrint.getSelection(), result).schedule();

		// update the newly entered XPath to the history
		new QueryHistoryManager(query).update();

		// set the caret at the end of query text string
		int textLength = query.getText().length();
		query.setSelection(new Point(textLength, textLength));
	}
}
