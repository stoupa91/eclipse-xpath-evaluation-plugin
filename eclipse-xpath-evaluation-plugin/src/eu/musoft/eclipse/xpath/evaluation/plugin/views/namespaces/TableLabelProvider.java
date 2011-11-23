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

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import eu.musoft.eclipse.xpath.evaluation.plugin.Activator;

/**
 * This class provides the labels for the namespace table
 */
class NamespaceTableLabelProvider implements ITableLabelProvider {

	/**
	 * Returns the image for particular column.
	 * 
	 * @param element
	 *          element
	 * @param columnIndex
	 *          column index
	 * @return Image for particular column
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		switch (columnIndex) {
			case 0:
				Image eraseIcon = new Image(PlatformUI.getWorkbench().getDisplay(), Activator.getImageDescriptor("icons/Modify.png").getImageData());
				return eraseIcon;
			case 1:
			case 2:
				return null;
		}

		throw new UnsupportedOperationException("Failed getting image for element " + element + " on column index " + columnIndex);
	}

	/**
	 * Returns the text for particular column.
	 * 
	 * @param element
	 *          element
	 * @param columnIndex
	 *          column index
	 * @return text for particular column
	 */
	public String getColumnText(Object element, int columnIndex) {
		Namespace prefix = (Namespace) element;
		switch (columnIndex) {
			case 0:
				return null;
			case 1:
				return prefix.getPrefix();
			case 2:
				return prefix.getURI();
		}

		throw new UnsupportedOperationException("Failed getting text for element " + element + " on column index " + columnIndex);
	}

	/**
	 * Adds a listener.
	 * 
	 * @param listener
	 *          the listener
	 */
	public void addListener(ILabelProviderListener listener) {
		// Ignore it
	}

	/**
	 * Disposes any created resources
	 */
	public void dispose() {
		// Nothing to dispose
	}

	/**
	 * Returns whether altering this property on this element will affect the
	 * label
	 * 
	 * @param element
	 *          the element
	 * @param property
	 *          the property
	 * @return boolean
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * Removes a listener
	 * 
	 * @param listener
	 *          the listener
	 */
	public void removeListener(ILabelProviderListener listener) {
		// Ignore
	}

}
