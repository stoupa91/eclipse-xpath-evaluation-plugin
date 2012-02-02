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
package eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class NamespacesTable extends Composite {

	static final String COLUMN_ERASE = "";
	static final String COLUMN_PREFIX = "Prefix";
	static final String COLUMN_URI = "URI";

	private static final int PREFIX_COLUMN_INIT_WIDTH = 100;
	private static final int URI_COLUMN_INIT_WIDTH = 350;

	public static final String[] COLUMNS = { COLUMN_ERASE, COLUMN_PREFIX, COLUMN_URI };

	// The data model
	private List<Namespace> namespaces = new ArrayList<Namespace>();

	public NamespacesTable(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));

		initializeModel();
		initializeGUI();
	}

	private void initializeModel() {
		namespaces.add(new Namespace());
	}

	private void initializeGUI() {
		// Add the TableViewer
		final TableViewer tv = new TableViewer(this, SWT.FULL_SELECTION);
		tv.setContentProvider(new ContentProvider());
		tv.setLabelProvider(new TableLabelProvider());
		tv.setInput(namespaces);

		// Set up the table
		final Table table = tv.getTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		final TableColumn eraseColumn = new TableColumn(table, SWT.LEFT);
		eraseColumn.setText(COLUMN_ERASE);
		eraseColumn.pack();
		eraseColumn.setAlignment(SWT.CENTER);
		eraseColumn.setResizable(false);

		TableColumn prefixColumn = new TableColumn(table, SWT.LEFT);
		prefixColumn.setText(COLUMN_PREFIX);
		prefixColumn.setWidth(PREFIX_COLUMN_INIT_WIDTH);

		TableColumn uriColumn = new TableColumn(table, SWT.LEFT);
		uriColumn.setText(COLUMN_URI);
		uriColumn.setWidth(URI_COLUMN_INIT_WIDTH);

		// select the 1st line in the table
		table.select(0);

		// add table listeners
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent event) {
				int rowIdx = -1;
				int colIdx = -1;

				Rectangle clientArea = table.getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = table.getTopIndex();
				while (index < table.getItemCount()) {
					boolean visible = false;
					TableItem item = table.getItem(index);
					for (int i = 0; i < COLUMNS.length; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							rowIdx = index;
							colIdx = i;
							break;
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible)
						break;
					index++;
				}

				boolean isExistingRowSelected = rowIdx != -1;
				boolean isEraseColumnSelected = colIdx == 0;
				if (isExistingRowSelected && isEraseColumnSelected) {
					namespaces.remove(rowIdx);
					table.remove(rowIdx);

					// last row has been removed, put "empty" line
					if (namespaces.isEmpty() || !namespaces.get(namespaces.size() - 1).isEmpty()) {
						namespaces.add(new Namespace());
					}

					tv.refresh();
				}
			}

		});

		// Create the cell editors
		CellEditor[] editors = new CellEditor[3];
		editors[0] = null;
		editors[1] = new TextCellEditor(table);
		editors[2] = new TextCellEditor(table);

		// Set the editors, cell modifier, and column properties
		tv.setColumnProperties(COLUMNS);
		tv.setCellModifier(new CellModifier(tv));
		tv.setCellEditors(editors);

		tv.refresh();
	}

	public List<Namespace> getNamespaces() {
		return namespaces;
	}

}