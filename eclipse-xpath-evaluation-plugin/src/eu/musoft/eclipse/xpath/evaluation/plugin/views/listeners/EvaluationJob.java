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

import java.util.List;
import java.util.ResourceBundle;

import net.sf.saxon.s9api.XdmValue;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.progress.UIJob;

import eu.musoft.eclipse.xpath.evaluation.plugin.XPathEvaluator;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.components.Notification;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces.Namespace;

/**
 * This class performs actual XPath evaluation. The task will run in a new
 * thread while invoking the 'schedule()' method on this class instance.
 */
class EvaluationJob extends Job {

  private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");
  
	private Combo query;
	private Text result;

	private String xpath;
	private List<Namespace> namespaces;
	private String xml;
	private boolean isPrettyPrint;

	/**
	 * Constructor.
	 * 
	 * @param xpath
	 *          XPath to be evaluated
	 * @param namespaces
	 *          list of namespaces used in Xpath expression
	 * @param xml
	 *          XML to evaluate the XPath expression on
	 * @param result
	 *          evaluated subset of the original XML
	 * @param query
	 */
	public EvaluationJob(final String xpath, final List<Namespace> namespaces, final String xml, final boolean isPrettyPrint, final Text result, Combo query) {
		super(bundle.getString("label.xpath.evaluation"));

		this.query = query;
		this.result = result;
		this.xpath = xpath;
		this.namespaces = namespaces;
		this.xml = xml;
		this.isPrettyPrint = isPrettyPrint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor
	 * )
	 */
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			XdmValue evaluatedResult = XPathEvaluator.evaluate(xpath, namespaces, xml, isPrettyPrint);
			String evaluatedResultXml = XPathEvaluator.transformResult(evaluatedResult, isPrettyPrint);
			outputResult(evaluatedResultXml, evaluatedResult.size());
		} catch (final Exception e) {
			new UIJob(bundle.getString("label.xpath.evaluation")) {
				@Override
				public IStatus runInUIThread(IProgressMonitor monitor) {
					Notification.showToolTip(SWT.BALLOON | SWT.ICON_ERROR, bundle.getString("label.error"), e.getMessage(), query);
					return Status.OK_STATUS;
				}
			}.schedule();
		}

		return Status.OK_STATUS;
	}

	private void outputResult(final String evaluatedResult, final int resultsCount) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				result.setText(evaluatedResult);

				Notification.showToolTip(SWT.BALLOON | SWT.ICON_INFORMATION, bundle.getString("label.information"), bundle.getString("label.number.of.results") + ": " + resultsCount, query);
			}
		});
	}
}
