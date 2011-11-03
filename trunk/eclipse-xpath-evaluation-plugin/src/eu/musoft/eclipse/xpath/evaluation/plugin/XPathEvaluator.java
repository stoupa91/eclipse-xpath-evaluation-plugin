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
package eu.musoft.eclipse.xpath.evaluation.plugin;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;

/**
 * This class performs evaluation of XPath expression against an XML document.
 */
public class XPathEvaluator {

	private static final Processor processor = new Processor(false);

	/**
	 * Evaluates XPath expression against provided XML.<br/>
	 * </br>
	 * 
	 * <i>Implementation details of XPath evaluation using Saxon are described at
	 * http://www.saxonica.com/documentation/xpath-api/s9api-xpath.xml</i>
	 * 
	 * @param xpath
	 *          string representation of XPath expression
	 * @param xml
	 *          string representation of XML document
	 * @return a subset of elements from original XML document which satisfy
	 *         provided XPath expression
	 * @throws Exception
	 *           is thrown, if either XPath could not be compiled or XML document
	 *           could not be parsed properly or the XPath evaluation against the
	 *           XML document fails
	 */
	public static String evaluate(String xpath, String xml) throws Exception {
		if (xpath == null)
			throw new IllegalArgumentException("xpath can not be null");
		if (xml == null)
			throw new IllegalArgumentException("xml can not be null");

		XPathExecutable exec = getXPathExecuatble(xpath);
		XdmNode xdm = buildXdm(xml);
		XdmValue xpathResult = evaluate(exec, xdm);

		return transformResult(xpathResult);
	}

	private static XPathExecutable getXPathExecuatble(String xpath) throws SaxonApiException {
		XPathCompiler xpathCompiler = processor.newXPathCompiler();
		return xpathCompiler.compile(xpath);
	}

	private static XdmNode buildXdm(String xml) throws SaxonApiException {
		DocumentBuilder builder = processor.newDocumentBuilder();
		Source source = new StreamSource(new StringReader(xml));
		return builder.build(source);
	}

	private static XdmValue evaluate(XPathExecutable exec, XdmNode xdm) throws SaxonApiException {
		XPathSelector selector = exec.load();
		selector.setContextItem(xdm);
		return selector.evaluate();
	}

	private static String transformResult(XdmValue xdm) {
		StringBuilder sb = new StringBuilder();
		for (XdmItem item: xdm) {
			sb.append(item);
			sb.append("\n");
		}

		return sb.toString();
	}

}
