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
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathExecutable;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.XdmValue;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import eu.musoft.eclipse.xpath.evaluation.plugin.views.namespaces.Namespace;

/**
 * This class performs evaluation of XPath expression against an XML document.
 */
public class XPathEvaluator {

	private static final Processor processor;

	private static final XsltExecutable xsltExec;
	private static final boolean isPrettyPrintEnabled;

	static {
		processor = new Processor(false);

		xsltExec = getXsltRuntime();
		isPrettyPrintEnabled = xsltExec != null;
	}

	/**
	 * Evaluates XPath expression against provided XML.<br/>
	 * </br>
	 * 
	 * <i>Implementation details of XPath evaluation using Saxon are described at
	 * http://www.saxonica.com/documentation/xpath-api/s9api-xpath.xml</i>
	 * 
	 * @param xpath
	 *          string representation of XPath expression
	 * @param namespaces
	 *          list of namespaces used in Xpath expression
	 * @param xml
	 *          string representation of XML document
	 * @param isPrettyPrint
	 *          indicates whether pretty print is enabled for current evaluation
	 * @return a subset of elements from original XML document which satisfy
	 *         provided XPath expression
	 * @throws Exception
	 *           is thrown, if either XPath could not be compiled or XML document
	 *           could not be parsed properly or the XPath evaluation against the
	 *           XML document fails
	 */
	public static String evaluate(String xpath, List<Namespace> namespaces, String xml, boolean isPrettyPrint) throws Exception {
		Activator.logInfo("Evaluating XPath: " + xpath);
		Activator.logInfo("Against XML with length: " + ((xml == null) ? null : xml.length()));
		Activator.logInfo("Pretty print:" + isPrettyPrint);

		if (xpath == null)
			throw new IllegalArgumentException("xpath can not be null");
		if (xml == null)
			throw new IllegalArgumentException("xml can not be null");

		XPathExecutable exec = getXPathExecuatble(xpath, namespaces);
		XdmNode xdm = buildXdm(xml);
		XdmValue xpathResult = evaluate(exec, xdm);

		return transformResult(xpathResult, isPrettyPrint);
	}

	private static XsltExecutable getXsltRuntime() {
		Activator.logInfo("Getting XSLT runtime");

		try {
			return processor.newXsltCompiler().compile(new StreamSource(Activator.loadFile("xslt/indent.xsl")));
		} catch (Exception e) {
			// unable to load XSLT runtime
			Activator.logError("Unable to load XSLT runtime", e);
			return null;
		}
	}

	private static XPathExecutable getXPathExecuatble(String xpath, List<Namespace> namespaces) throws SaxonApiException {
		Activator.logInfo("Getting XPath executable");

		XPathCompiler xpathCompiler = processor.newXPathCompiler();
		for (Namespace n: namespaces) {
			xpathCompiler.declareNamespace(n.getPrefix(), n.getURI());
		}
		return xpathCompiler.compile(xpath);
	}

	private static XdmNode buildXdm(String xml) throws SaxonApiException {
		Activator.logInfo("Building Xdm");
		DocumentBuilder builder = processor.newDocumentBuilder();
		Source source = new StreamSource(new StringReader(xml));
		return builder.build(source);
	}

	private static XdmValue evaluate(XPathExecutable exec, XdmNode xdm) throws SaxonApiException {
		Activator.logInfo("Evaluating XPath");
		XPathSelector selector = exec.load();
		selector.setContextItem(xdm);
		return selector.evaluate();
	}

	private static String transformResult(XdmValue xdm, boolean isPrettyPrint) throws Exception {
		Activator.logInfo("Transforming result");

		if (isPrettyPrint && isPrettyPrintEnabled) {
			return prettyPrint(xdm);
		}

		StringBuilder sb = new StringBuilder();
		for (XdmItem item: xdm) {
			sb.append(item);
		}
		return sb.toString();
	}

	private static String prettyPrint(XdmValue xdm) throws Exception {
		Activator.logInfo("Pretty printing");

		Writer result = new StringWriter();
		XsltTransformer xsltTransformer = xsltExec.load();
		xsltTransformer.setDestination(new Serializer(result));

		for (XdmItem item: xdm) {
			if (item instanceof XdmNode) {
				xsltTransformer.setInitialContextNode((XdmNode) item);
				xsltTransformer.transform();
				result.write("\n");
			}
		}

		return result.toString();
	}

}
