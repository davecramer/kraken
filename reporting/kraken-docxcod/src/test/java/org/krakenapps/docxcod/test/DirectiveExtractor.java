package org.krakenapps.docxcod.test;

import static org.krakenapps.docxcod.util.XMLDocHelper.evaluateXPath;
import static org.krakenapps.docxcod.util.XMLDocHelper.newDocumentBuilder;
import static org.krakenapps.docxcod.util.XMLDocHelper.newXPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPath;

import org.krakenapps.docxcod.Directive;
import org.krakenapps.docxcod.OOXMLProcessor;
import org.krakenapps.docxcod.OOXMLPackage;
import org.krakenapps.docxcod.util.XMLDocHelper.NodeListWrapper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DirectiveExtractor implements OOXMLProcessor {

	@Override
	public void process(OOXMLPackage pkg) {
		extractField(pkg);
	}
	
	private ArrayList<Directive> directives = new ArrayList<Directive>();
	
	public ArrayList<Directive> getDirectives() {
		return directives;
	}

	private void extractField(OOXMLPackage pkg) throws TransformerFactoryConfigurationError {
		InputStream f = null;
		try {
			f = new FileInputStream(new File(pkg.getDataDir(), "word/document.xml"));
			Document doc = newDocumentBuilder().parse(f);

			XPath xpath = newXPath(doc);
			{
				NodeList nodeList = evaluateXPath(xpath, "//*[name()='w:fldChar' or name()='w:instrText' or name()='w:fldSimple']", doc);
				
				String mergedDirective = null;
				Node directivePosition = null;
				
				for (Node n : new NodeListWrapper(nodeList)) {
					if (n.getNodeName().equals("w:fldChar")) {
						String fldCharType = n.getAttributes().getNamedItem("w:fldCharType").getNodeValue();
						if (fldCharType.equals("begin")) {
							mergedDirective = "";
							directivePosition = n.getParentNode(); 
						} else if (fldCharType.equals("end") || fldCharType.equals("separate")) {
							if (directivePosition != null) {
								directives.add(new Directive(directivePosition, Directive.extractDirective(mergedDirective)));
							}
							mergedDirective = null;
							directivePosition = null;
						} else {
							mergedDirective = null;
							directivePosition = null;
						}
						
					} else if (n.getNodeName().equals("w:instrText")) {
						if (directivePosition != null)
							mergedDirective += n.getTextContent();
					} else if (n.getNodeName().equals("w:fldSimple")) {
						String nodeValue = n.getAttributes().getNamedItem("w:instr").getNodeValue();
						directives.add(new Directive(n.getParentNode(), Directive.extractDirective(nodeValue)));
					} else {
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			safeClose(f);
		}
	}

	private void safeClose(InputStream f) {
		if (f == null) 
			return;
		try {
			f.close();
		} catch (Exception e) {
			// ignore
		}
	}

}
