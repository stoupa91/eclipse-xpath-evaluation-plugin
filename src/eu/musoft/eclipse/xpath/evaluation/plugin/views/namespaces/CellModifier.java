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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

class CellModifier implements ICellModifier {

	private Viewer viewer;

	public CellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns whether the property of the element can be modified.
	 * 
	 * @param element
	 *          element
	 * @param property
	 *          property
	 * @return true, if the property can be modified. false, otherwise
	 */
	public boolean canModify(Object element, String property) {
		if (NamespacesTable.COLUMN_ERASE.equals(property))
			return false;
		if (NamespacesTable.COLUMN_PREFIX.equals(property))
			return true;
		if (NamespacesTable.COLUMN_URI.equals(property))
			return true;

		throw new UnsupportedOperationException("Unknown property to modify: " + property);
	}

	/**
	 * Returns the value of the property.
	 * 
	 * @param element
	 *          element
	 * @param property
	 *          property
	 * @return value of the element's property
	 */
	public Object getValue(Object element, String property) {
		Namespace n = (Namespace) element;
		if (NamespacesTable.COLUMN_PREFIX.equals(property))
			return n.getPrefix();
		if (NamespacesTable.COLUMN_URI.equals(property))
			return n.getURI();

		throw new UnsupportedOperationException("Failed getting element " + element + " for property " + property);
	}

	/**
	 * Modifies the element.
	 * 
	 * @param element
	 *          element
	 * @param property
	 *          property
	 * @param value
	 *          value
	 */
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item) {
			Namespace n = (Namespace) ((Item) element).getData();

			if (NamespacesTable.COLUMN_PREFIX.equals(property))
				n.setPrefix(((String) value).trim());
			if (NamespacesTable.COLUMN_URI.equals(property))
				n.setURI((String) value);

			List<Namespace> namespaces = (List<Namespace>) viewer.getInput();

			// remove all empty lines if any
			for (Iterator<Namespace> it = namespaces.iterator(); it.hasNext();) {
				Namespace namespace = it.next();
				if (namespace.isEmpty()) {
					it.remove();
				}
			}

			// append a new line
			insertNewLine(namespaces);

			// Force the viewer to refresh
			viewer.refresh();
		}
	}

	private void insertNewLine(List<Namespace> namespaces) {
		namespaces.add(new Namespace());
	}

}
