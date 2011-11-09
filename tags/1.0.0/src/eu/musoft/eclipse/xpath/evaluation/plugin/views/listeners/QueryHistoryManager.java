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

import org.eclipse.swt.widgets.Combo;

/**
 * This class manages the history of query combo box. It allows up to MAX_COUNT
 * items to be listed in the query list, whereas older items will be removed.
 */
class QueryHistoryManager {

	private static final int MAX_COUNT = 10;

	private Combo query;

	/**
	 * @param query
	 *          query combo
	 */
	public QueryHistoryManager(Combo query) {
		this.query = query;
	}

	/**
	 * Updates the query history.
	 */
	public void update() {
		String xpath = query.getText().trim();

		if (xpath.length() == 0) { // don't update the history, new xpath is empty
			return;
		}

		query.deselectAll();

		// remove item from the combo box if already exists
		try {
			query.remove(xpath);
		} catch (IllegalArgumentException e) {
			// do nothing. indicates that xpath could not be found in the combo box
		}

		// add item to the top of the combo box
		query.add(xpath, 0);
		query.select(0);

		// // remove last item if count exceeds MAX_COUNT limit
		int count = query.getItemCount();
		if (count > MAX_COUNT) {
			query.remove(count - 1);
		}
	}

}
