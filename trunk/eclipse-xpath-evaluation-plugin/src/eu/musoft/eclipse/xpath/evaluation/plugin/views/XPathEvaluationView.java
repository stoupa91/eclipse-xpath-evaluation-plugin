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
package eu.musoft.eclipse.xpath.evaluation.plugin.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import eu.musoft.eclipse.xpath.evaluation.plugin.Activator;

public class XPathEvaluationView extends ViewPart {

	private Combo query;
	private Button execute;
	private Text result;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "eu.musoft.eclipse.xpath.evaluation.plugin.views.XPathEvaluationView";

	/**
	 * The constructor.
	 */
	public XPathEvaluationView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		GridLayout grid = new GridLayout(2, false);
		parent.setLayout(grid);

		query = new Combo(parent, SWT.DROP_DOWN);
		query.setLayoutData(new GridData(SWT.FILL, 0, true, false));
		query.setToolTipText("Insert valid XPath query");

		execute = new Button(parent, 0);
		execute.setImage(new Image(PlatformUI.getWorkbench().getDisplay(), Activator.getImageDescriptor("icons/Apply.png").getImageData()));
		execute.setToolTipText("Run query");

		result = new Text(parent, SWT.MULTI);
		result.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}

	/**
	 * Passing the focus request to the query combo.
	 */
	public void setFocus() {
		query.setFocus();
	}
}