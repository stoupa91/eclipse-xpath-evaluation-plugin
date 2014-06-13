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

import java.util.ResourceBundle;

import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;

import eu.musoft.eclipse.xpath.evaluation.plugin.Activator;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.listeners.EvaluationTrigger;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.listeners.NamespaceLoadTrigger;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.listeners.QueryComboKeyHandler;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces.NamespacesTable;

public class XPathEvaluationView extends ViewPart {

	private static final ResourceBundle bundle = ResourceBundle.getBundle("messages");
	private static final int GRID_COLUMNS = 4;

	private Combo query;
	private Button execute;
	private Button prettyPrint;
	private Text result;
	private Button namespaceLoader;

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
		GridLayout grid = new GridLayout(GRID_COLUMNS, false);
		parent.setLayout(grid);

		// XPath query combo box
		query = new Combo(parent, SWT.DROP_DOWN);
		query.setLayoutData(new GridData(SWT.FILL, 0, true, false));
		query.setToolTipText(bundle.getString("label.insert.valid.query"));
		query.addKeyListener(new QueryComboKeyHandler());

		// Execute query button
		execute = new Button(parent, SWT.PUSH);
		execute.setImage(new Image(PlatformUI.getWorkbench().getDisplay(), Activator.getImageDescriptor("icons/Play.png").getImageData()));
		execute.setToolTipText(bundle.getString("label.run.query"));

		namespaceLoader = new Button(parent, SWT.PUSH);
		namespaceLoader.setImage(new Image(PlatformUI.getWorkbench().getDisplay(), Activator.getImageDescriptor("icons/xml-namespace.png").getImageData()));
		namespaceLoader.setToolTipText(bundle.getString("label.load.all.namespaces"));

		// Pretty print button
		prettyPrint = new Button(parent, SWT.CHECK);
		prettyPrint.setSelection(true); // pretty print enabled by default
		prettyPrint.setToolTipText(bundle.getString("label.pretty.print"));

		// Tabs area
		TabFolder tabs = new TabFolder(parent, SWT.TOP);
		tabs.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, GRID_COLUMNS, 1));

		// Result tab
		TabItem resultTab = new TabItem(tabs, SWT.NONE);
		resultTab.setText(bundle.getString("label.result"));
		SashForm splitPane = new SashForm(tabs, SWT.HORIZONTAL);
		result = new Text(splitPane, SWT.MULTI | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		result.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		NamespacesTable namespacesTable = new NamespacesTable(splitPane);
		splitPane.setWeights(new int[] { 6, 4 });
		resultTab.setControl(splitPane);

		// add the evaluation trigger listener
		SelectionListener evaluationTrigger = new EvaluationTrigger(query, namespacesTable, prettyPrint, result);
		query.addSelectionListener(evaluationTrigger);
		execute.addSelectionListener(evaluationTrigger);

		// add namespace loader listener
		SelectionListener namespaceLoaderListener = new NamespaceLoadTrigger(query, namespacesTable);
		namespaceLoader.addSelectionListener(namespaceLoaderListener);
	}

	/**
	 * Passing the focus request to the query combo.
	 */
	public void setFocus() {
		query.setFocus();
	}

	/**
	 * Gets the content of active text editor.
	 * 
	 * @return active text editor content as a string
	 * @throws GUIException
	 *           is thrown if there is either no workbench or no active workbench
	 *           window or no active page or no active editor or the editor is not
	 *           a text editor
	 */
	public static String getActiveTextEditorContent() throws GUIException {
		ITextEditor editor = getActiveTextEditor();

		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String content = doc.get();
		return (content != null) ? content : "";
	}

	private static IEditorPart getActiveEditor() throws GUIException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			throw new GUIException(bundle.getString("error.no.workbench"));

		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null)
			throw new GUIException(bundle.getString("error.no.workbench.window.active"));

		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			throw new GUIException(bundle.getString("error.no.page.active"));

		IEditorPart editor = page.getActiveEditor();
		if (editor == null)
			throw new GUIException(bundle.getString("error.no.text.editor.active"));

		return editor;
	}

	private static ITextEditor getActiveTextEditor() throws GUIException {
		ITextEditor textEditor = (ITextEditor) getActiveEditor().getAdapter(ITextEditor.class);
		if (textEditor == null)
			throw new GUIException(bundle.getString("error.no.text.editor"));

		return textEditor;
	}

}