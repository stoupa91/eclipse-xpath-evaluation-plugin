<h1> <img src="eclipse-xpath-evaluation-plugin/icons/Diagram.png" alt="Logo" width="32" height="32" /> Eclipse XPath evaluation plugin</h1>

This plugin provides a new view in an eclipse perspective and enables the user to evaluate XPath expressions against the active text editor containing XML.

Features:
 * XPath 2.0 expression validation
 * XML validation (well formed)
 * Executing XPath 2.0 expressions against XML
 * Namespace support in XPath queries
 * Pretty print

Supported Eclipse versions:
 * Neon / 4.6+
 * Mars / 4.5+
 * Luna / 4.4+
 * Kepler / 4.3+
 * Juno / 4.2+
 * Indigo / 3.7+
 * Helios / 3.6+
 * Galileo / 3.5+
 * Ganymede / 3.4+ 

Java version:
 * 1.6 and above (from plugin version 1.4.1 onwards)
 * 1.5 (up to plugin version 1.3.0)

<h2>Installation</h2>

**Automatic installation - Drag & Drop (Indigo, Juno, Kepler, Luna, Mars, Neon)**
 * Drag and drop following icon [![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](http://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=148833 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client") into a running Eclipse Indigo/Juno/Kepler/Luna/Mars/Neon workspace 
 * Click "Next"
 * Accept license
 * Click "Finish"
 * You might be warned that you are installing unsigned content. If you are OK with it, click "OK" to proceed.
 * At the end of installation process you will be asked to restart Eclipse, click on "Restart now" 

**Automatic installation from within Eclipse Marketplace (Helios, Indigo, Juno, Kepler, Luna, Mars, Neon)**
 * Go to Help -> Eclipse Marketplace
 * Type into the search box "xpath", select "Tools" in the market combo box and/or "XML" in the categories combo box to narrow down the results and click "Go"
 * One of the results should appear "Eclipse XPath evaluation plugin", click on "Install" button next to it
 * Click "Next"
 * Accept license
 * Click "Finish"
 * You might be warned that you are installing unsigned content. If you are OK with it, click "OK" to proceed.
 * At the end of installation process you will be asked to restart Eclipse, click on "Restart now" 

**Manual installation from within Eclipse (Galileo, Helios, Indigo, Juno, Kepler, Luna, Mars, Neon)**
 * Go to Help -> Install New Software
 * Add repository ("Add.." button)
 * Fill in Name as "XPath" and Location as "https://raw.githubusercontent.com/stoupa91/eclipse-xpath-evaluation-plugin/master/eclipse-xpath-evaluation-plugin-update-site/"
 * Select the loaded plug-in in "Uncategorized" section
 * Click "Next", "Next"
 * Accept license
 * Click "Finish"
 * You might be warned that you are installing unsigned content. If you are OK with it, click "OK" to proceed.
 * At the end of installation process you will be asked to restart Eclipse, click on "Restart now" 

**Manual installation from within Eclipse (Ganymede)**
 * Go to Help -> Software Updates
 * Click "Add Site.."
 * Fill in Location as "https://raw.githubusercontent.com/stoupa91/eclipse-xpath-evaluation-plugin/master/eclipse-xpath-evaluation-plugin-update-site/"
 * Select the loaded plug-in in "Uncategorized" section
 * Click "Install"
 * Accept license
 * Click "Finish"
 * You might be warned that you are installing unsigned content. If you are OK with it, click "OK" to proceed.
 * At the end of installation process you will be asked to restart Eclipse, click on "Restart now" 

**Manual installation (Ganymede, Galileo, Helios)**
 * Download provided zip file with the most recent version of the plug-in implementation from "https://github.com/stoupa91/eclipse-xpath-evaluation-plugin/tree/master/eclipse-xpath-evaluation-plugin-update-site/archive"
 * Extract the zip file into the home directory of your eclipse installation
 * Launch Eclipse IDE

<h2>Post-installation tips</h2>

If you experience any issue related to either the plugin's GUI or your eclipse installation in general like tooltips displayed as <a href="https://github.com/stoupa91/eclipse-xpath-evaluation-plugin/issues/6">black bubbles</a> or any distortion of the icons you may want to put following two lines into your eclipse.ini file and restart your eclipse:
<pre>
--launcher.GTK_version
2
</pre>

<h2>Usage</h2>

Open XPath view (Indigo, Juno, Kepler, Luna, Mars, Neon)
 * Go to Window -> Show View -> Other -> XML -> XPath, whereas new view, for executing the XPath expressions, will be shown 

Open XPath view (Ganymede, Galileo, Helios)
 * Go to Window -> Show View -> Other -> Other -> XPath, whereas new view, for executing the XPath expressions, will be shown 

<b>XPath view usage</b>
 * Open an XML file in (any) text editor within your eclipse IDE
 * Provide XPath query you want to execute in the combo box
 * Optionally, you can define several namespaces used in your XPath query in the table on the right side of the view by providing namespace prefix and its URI or click on 'Load all namespaces from current file' to automatically load all the namespaces
 * Execute the query either by pressing 'Enter' (while the cursor is still in the XPath query input box) or by clicking on 'Run query' button
 * The result will be shown under the query combo box

<h2>ZIP archive</h2>
<a href="https://github.com/stoupa91/eclipse-xpath-evaluation-plugin/raw/master/eclipse-xpath-evaluation-plugin-update-site/archive/eclipse-xpath-evaluation-plugin-1.4.4.zip">eclipse-xpath-evaluation-plugin-1.4.4.zip</a>
<br />
<a href="https://github.com/stoupa91/eclipse-xpath-evaluation-plugin/raw/master/eclipse-xpath-evaluation-plugin-update-site/archive/eclipse-xpath-evaluation-plugin-1.3.0.zip">eclipse-xpath-evaluation-plugin-1.3.0.zip</a>
