package com.grimsmiler; /**
 * Created by ilya on 04/11/15.
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import com.grimsmiler.Helpers.FileHelper.TestSummaryOutputHelper;
import com.grimsmiler.Helpers.Logging.LoggingHelper;
import com.grimsmiler.PrepareHTML.TestSummary.PrepareHTMLTestReport;
import com.grimsmiler.PropertyListObjects.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TestSummaryConverter {

    public static void main(String[] args) {
        try {

            if (!(new File(args[0])).exists()) {
                throw new FileNotFoundException("Output Folder not found. Exiting.");
            }

            String outputFolderPath = args[0];

            TestSummaryOutputHelper.prepareHelper(outputFolderPath);

            File fXmlFile = TestSummaryOutputHelper.getXcodeTestSummaryPlist();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            //create a root dictionary
            PropertyListDictionary root = new PropertyListDictionary("root");

            //retrieve the root node from the plist
            Node rootNode = doc.getElementsByTagName("dict").item(0);

            //parse the contents of the plist
            root.addPropertiesToPropertyList(parseDictionaryChildNodes(rootNode.getChildNodes()));

            //if parsed root node contains TestableSummaries -> success -> prepare HTML report
            if (root.getProperty("TestableSummaries") != null) {
                PropertyListArray plaTestSummary = (PropertyListArray) root.getProperty("TestableSummaries");
                PrepareHTMLTestReport.prepareHTMLTestReport(plaTestSummary);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Did You enter the path to Xcode Output Folder?");
            System.out.println("Example: java testplist2html.jar \"./<XCODE OUTPUT FOLDER NAME>\"");
        } catch (Exception e) {
            LoggingHelper.logError(e);
            System.exit(-1);
        }
    }

    private static List<PropertyListObject> parseDictionaryChildNodes(NodeList nodeList) {
        List<PropertyListObject> finalPLO = new ArrayList<PropertyListObject>();
        Node currentNode = nodeList.item(0);
        String currentNodeName = "";
        String key = "";
        do {

            currentNodeName = currentNode.getNodeName();

            if (currentNodeName.equals("key")) {
                key = currentNode.getTextContent();
            } else if (currentNodeName.equals("string")) {
                String content = currentNode.getTextContent();
                finalPLO.add(new PropertyListString(key, currentNode.getTextContent()));
            } else if (currentNodeName.equals("integer")) {
                finalPLO.add(new PropertyListInteger(key, Integer.parseInt(currentNode.getTextContent())));
            } else if (currentNodeName.equals("real")) {
                finalPLO.add(new PropertyListDouble(key, Double.parseDouble(currentNode.getTextContent())));
            } else if (currentNodeName.contains("true") || currentNodeName.contains("false")) {
                finalPLO.add(new PropertyListBoolean(key, Boolean.parseBoolean(currentNode.getNodeName())));
            } else if (currentNodeName.equals("array")) {
                PropertyListArray pla = new PropertyListArray(key);

                if (currentNode.hasChildNodes()) {
                    pla.addFromList(parseDictionaryChildNodes(currentNode.getChildNodes()));
                }

                finalPLO.add(pla);
            } else if (currentNodeName.equals("dict")) {
                PropertyListDictionary pld = new PropertyListDictionary(key);

                if (currentNode.hasChildNodes()) {
                    pld.addPropertiesToPropertyList(parseDictionaryChildNodes(currentNode.getChildNodes()));
                }

                finalPLO.add(pld);
            }

            currentNode = currentNode.getNextSibling();

        } while (currentNode != null);

        return finalPLO;
    }
}
