/**
 * Copyright (c) 2011 - 2012, Martin Uhlir
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
package eu.musoft.eclipse.xpath.evaluation.plugin.views.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;

public class Notification {

	/**
	 * Shows the tool tip message above the component.
	 * 
	 * @param style
	 *          SWT style of the tool tip
	 * @param message
	 *          message to display
	 * @param component
	 *          component above which the tool tip shall be displayed
	 */
	public static void showToolTip(int style, String message, final Control component) {
		Shell parent = component.getShell();
		final ToolTip tooltip = new ToolTip(parent, style);
		tooltip.setText(message);

		Point componentLocation = component.toDisplay(component.getLocation());
		tooltip.setLocation(componentLocation.x, componentLocation.y + (component.getSize().y / 2));

		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				tooltip.setVisible(false);
				component.removeListener(SWT.KeyDown, this);
				component.removeListener(SWT.FocusOut, this);
			}
		};
		component.addListener(SWT.KeyDown, listener);
		component.addListener(SWT.FocusOut, listener);

		tooltip.setAutoHide(true);
		tooltip.setVisible(true);
	}

}
