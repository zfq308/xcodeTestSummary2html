package com.grimsmiler.PrepareHTML.TestSummary;

import com.grimsmiler.Helpers.FileHelper.TestSummaryOutputHelper;
import com.grimsmiler.Helpers.Logging.LoggingHelper;
import com.grimsmiler.PrepareHTML.IPrepareHTML;
import com.grimsmiler.PropertyListObjects.PropertyListArray;
import com.grimsmiler.PropertyListObjects.PropertyListDictionary;
import com.grimsmiler.PropertyListObjects.PropertyListObject;
import com.grimsmiler.PropertyListObjects.PropertyListString;
import j2html.tags.ContainerTag;
import j2html.tags.Tag;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static j2html.TagCreator.*;

/**
 * Created by ilya on 05/11/15.
 */
public class PrepareHTMLTestReport extends IPrepareHTML {

    static ContainerTag testSummaryHTMLDoc;
    static String rootTagNode = "TestSummaryRootNode";

    public static void prepareHTMLTestReport(PropertyListArray plaTestSummary) {

        //process pla for test results
        Tag testSummaryContents = (tag("ul").with(prepareArray(plaTestSummary)));
        testSummaryHTMLDoc = html()
                //add style -> CSS
                .with(prepareStyle())
                //add Test Summary
                .with(testSummaryContents)
                //add Scripts
                .with(tag("script").attr("src","http://code.jquery.com/jquery-1.10.1.min.js"))
                .with(expandCollapseULListScript())
                .with(expandCollapseDLListScript());

        //write results to file
        try {
            String htmlReportFolderPath = TestSummaryOutputHelper.getHtmlReportFolder().getCanonicalFile().toString();
            PrintWriter writer = new PrintWriter( htmlReportFolderPath + "/TestSummaryReport.html", "UTF-8");
            writer.println(testSummaryHTMLDoc.toString());
            writer.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
            System.out.println("<----------------->");
            System.out.println(e.getStackTrace());
        }
    }

    private static List<Tag> prepareArray(PropertyListArray pla) {
        List<Tag> array2HTML = new ArrayList<Tag>();

        for(PropertyListObject plo : pla.getList()) {
            PropertyListDictionary pld = (PropertyListDictionary) plo;
            if ( pld != null) {
                array2HTML.add(parseDictionary(pld));
            }
        }
        return array2HTML;//.attr("style","display: block;")
    }

    private static Tag parseDictionary(PropertyListDictionary pld) {
        if(pld.getProperty("TestObjectClass") != null) {
            PropertyListString testObjectClass = (PropertyListString) pld.getProperty("TestObjectClass");

            if (testObjectClass.getPropertyValue().equals("IDESchemeActionTestableSummary")) {
                return  parseTestableSummary(pld);
            } else if (testObjectClass.getPropertyValue().equals("IDESchemeActionTestSummaryGroup")) {
                return parseTestSummaryGroup(pld);
            } else if (testObjectClass.getPropertyValue().equals("IDESchemeActionTestSummary")) {
                return parseTestSummary(pld);
            } else {
                return parseTestStep(pld);
            }
        } else {//if pld does not have TestObjectClass -> should never happen
            return tag("li")
                    .withText("COULD NOT PARSE PropertyListDictionary:")
                    .with(tag("br"))
                    .withText(pld.toString());
        }
    }

    private static Tag parseTestableSummary(PropertyListDictionary pld) {
        ContainerTag itemInList = tag("li");

        PropertyListArray testsArray = (PropertyListArray) (pld.getProperty("Tests"));
        String projectPath = (String) pld.getPropertyValue("ProjectPath");
        String targetName = (String) pld.getPropertyValue("TargetName");
        String testName = (String) pld.getPropertyValue("TestName");
        String testObjectClass = (String) pld.getPropertyValue("TestObjectClass");

        ContainerTag testableSummary = tag("span");

        testableSummary = IPrepareHTML.addPropertyAsAttribute(testableSummary, "ProjectPath", projectPath);
        testableSummary = IPrepareHTML.addPropertyAsAttribute(testableSummary,"TargetName",targetName);
        testableSummary = IPrepareHTML.addPropertyAsAttribute(testableSummary,"TestName",testName);
        testableSummary = IPrepareHTML.addPropertyAsAttribute(testableSummary,"TestObjectClass",testObjectClass);

        testableSummary.withText("Project: " + testName);

        itemInList.with(testableSummary);

        //if array not null -> parse and add as children
        if (testsArray != null) {
            itemInList.with(prepareArray(testsArray));
        }

        return tag("ul").attr("id",rootTagNode).with(itemInList);
    }

    private static Tag parseTestSummaryGroup(PropertyListDictionary pld) {
        ContainerTag itemInList = tag("li");

        PropertyListArray subtestsArray = (PropertyListArray) (pld.getProperty("Subtests"));
        String testIdentifier = (String) pld.getPropertyValue("TestIdentifier");
        String testName = (String) pld.getPropertyValue("TestName");
        String testObjectClass = (String) pld.getPropertyValue("TestObjectClass");

        ContainerTag testSummaryGroup = tag("span");

        testSummaryGroup = IPrepareHTML.addPropertyAsAttribute(testSummaryGroup,"TestIdentifier",testIdentifier);
        testSummaryGroup = IPrepareHTML.addPropertyAsAttribute(testSummaryGroup,"TestName",testName);
        testSummaryGroup = IPrepareHTML.addPropertyAsAttribute(testSummaryGroup,"TestObjectClass",testObjectClass);

        testSummaryGroup.withText(testName);

        itemInList.with(testSummaryGroup);

        //if array not null -> parse and add as children
        if (subtestsArray != null) {
            itemInList.with(prepareArray(subtestsArray));
        }

        return tag("ul").with(itemInList);
    }

    private static Tag parseTestSummary(PropertyListDictionary pld) {
        ContainerTag itemInList = tag("li");

        PropertyListArray activitySummariesArray = (PropertyListArray) (pld.getProperty("ActivitySummaries"));
        String testIdentifier = (String) pld.getPropertyValue("TestIdentifier");
        String testName = (String) pld.getPropertyValue("TestName");
        String testObjectClass = (String) pld.getPropertyValue("TestObjectClass");
        String testStatus = (String) pld.getPropertyValue("TestStatus");
        String testSummaryGUID = (String) pld.getPropertyValue("TestSummaryGUID");

        ContainerTag testSummary = tag("span");

        testSummary = IPrepareHTML.addPropertyAsAttribute(testSummary,"TestIdentifier",testIdentifier);
        testSummary = IPrepareHTML.addPropertyAsAttribute(testSummary,"TestName",testName);
        testSummary = IPrepareHTML.addPropertyAsAttribute(testSummary,"TestObjectClass",testObjectClass);
        testSummary = IPrepareHTML.addPropertyAsAttribute(testSummary,"TestStatus",testStatus);
        testSummary = IPrepareHTML.addPropertyAsAttribute(testSummary,"TestSummaryGUID",testSummaryGUID);

        testSummary.withText(testName + " ")
                .attr("class",testStatus.equals("Failure") ? "failedTest" : "passedTest")
                .withText((testStatus.equals("Failure")) ? "Failed" : "Passed");

        itemInList.with(testSummary);

        ContainerTag activitiesSummaryTable = table();
        ContainerTag activitiesSummaryTrTag = tag("tr");
        ContainerTag activitiesSummaryTdTag = tag("td");

        List<Tag> testStepList = new ArrayList<Tag>();

        //process Test Steps and add them to test summary
        if (activitySummariesArray != null) {
            for (PropertyListObject obj : activitySummariesArray.getList()) {
                PropertyListDictionary pldTestStep = (PropertyListDictionary) obj;
                if (pldTestStep != null) {
                    testStepList.add(parseTestStep(pldTestStep));

                }
            }
        }

        //add as a list item
        activitiesSummaryTdTag.with(testStepList);

        //if test failed -> process failure reason
        if(testStatus.equals("Failure")) {
            PropertyListArray failureSummariesPLA = (PropertyListArray) (pld.getProperty("FailureSummaries"));

            if (failureSummariesPLA != null) {
                for (PropertyListObject obj : failureSummariesPLA.getList()) {
                    PropertyListDictionary pldFailureCause = (PropertyListDictionary) obj;
                    if (pldFailureCause != null) {
                        activitiesSummaryTdTag.with(parseFailureReason(pldFailureCause));
                    }
                }
            }
        }

        itemInList
                .with(activitiesSummaryTable
                                .with(activitiesSummaryTrTag
                                                .with(activitiesSummaryTdTag
                                                )
                                )
                );

        return tag("ul").with(itemInList);
    }

    private static Tag parseFailureReason(PropertyListDictionary pldFailureSummary) {
        String fileName = (String) pldFailureSummary.getPropertyValue("FileName");
        Integer lineNumber = (Integer) pldFailureSummary.getPropertyValue("LineNumber");
        String message = (String) pldFailureSummary.getPropertyValue("Message");
        Boolean performanceFailure = (Boolean) pldFailureSummary.getPropertyValue("PerformanceFailure");

        ContainerTag failureDt = tag("dt").with(tag("b").withText("Failure Summary"));

        failureDt.with(tag("dd").with(tag("b").withText("File Name: "))
                .withText(fileName).with(tag("br")));
        failureDt.with(tag("dd").with(tag("b").withText("Line Number: "))
                .withText("" + lineNumber).with(tag("br")));
        failureDt.with(tag("dd").with(tag("b").withText("Message: "))
                .withText("" + message).with(tag("br")));
        failureDt.with(tag("dd").with(tag("b").withText("Performance Failure: "))
                .withText("" + performanceFailure).with(tag("br")));

        return tag("dl").with(failureDt);
    }

    private static ContainerTag parseTestStep(PropertyListDictionary pldTestStep) {

        ContainerTag tagTestStep = tag("dl");

        //retrieve properties of the Test Step
        Double startTime = (Double) pldTestStep.getPropertyValue("StartTimeInterval");
        Double finishTime = (Double) pldTestStep.getPropertyValue("FinishTimeInterval");
        String title = (String) pldTestStep.getPropertyValue("Title");
        String uuid = (String) pldTestStep.getPropertyValue("UUID");

        //convert properties to HTML table elements
        Tag stepTitle = tag("dt")
                .with(tag("b").withText("Step Title: "))
                .withText((checkNotNull(title) != null ? title : ""));
        Tag stepTime = tag("dd")
                .with(tag("b").withText("Time taken: "))
                .withText("" + (checkNotNull(finishTime - startTime) ? (finishTime - startTime) : Double.NaN) + "");
        Tag stepUuid = tag("dd")
                .with(tag("b").withText("UUID: "))
                .withText((checkNotNull(uuid) ? uuid : ""));

        //add the values to testStep table
        tagTestStep = tagTestStep.with(stepTitle,stepTime,stepUuid);
        //check for attachments
        parseForAttachments(pldTestStep,tagTestStep);
        //process testStep for subActivities -> substeps
        parseForSubsteps(pldTestStep,tagTestStep);

        //return testStep Table
        return tagTestStep;
    }

    private static void parseForSubsteps(PropertyListDictionary pldWithSubsteps, ContainerTag tagToAddAttachment) {
        //check if subActivities (substeps) exist in pld
        if ((pldWithSubsteps.getProperty("SubActivities")) != null) {
            PropertyListArray pla = (PropertyListArray) (pldWithSubsteps.getProperty("SubActivities"));

            ContainerTag substepsDL = tag("dl");

            //if subActivities is not null -> substeps exist -> create Substeps
            Tag subStepsHeader = tag("dt").with(
                    tag("b").withText("Substeps: ")
            );

            substepsDL.with(subStepsHeader);

            //for each substep activity
            for(PropertyListObject plo : pla.getList()) {
                PropertyListDictionary pldSubStep = (PropertyListDictionary) plo;
                //check if not null
                if (pldSubStep != null) {
                    //if substeps != null -> create substep and add to substeps tag
                    //create row for step
                    Tag subStep = tag ("dd")
                            .with(
                                    parseTestStep(pldSubStep)
                            );
                    //add created substep below previous (or header)
                    substepsDL.with(subStep);
                }
            }

            tagToAddAttachment.with(tag("dd").with(substepsDL));
        }
    }

    private static void parseForAttachments(PropertyListDictionary pldWithAttachments, ContainerTag tagToAddAttachment) {
        //check if pld has attachments
        if(pldWithAttachments.getProperty("Attachments") != null) {
            //if so - retrieve
            PropertyListArray plaAttachments = (PropertyListArray) pldWithAttachments.getProperty("Attachments");
            List<Tag> attachmentTagList = new ArrayList<Tag>();

            //for each attachment
            for(PropertyListObject obj : plaAttachments.getList()) {
                PropertyListDictionary pldAttachment = (PropertyListDictionary) obj;
                //check if exists
                if(pldAttachment != null) {
                    //process and add to attachment List
                    attachmentTagList.add(parseAttachment(pldAttachment));
                }
            }

            //prepare a separate DL
            ContainerTag attachmentDL = tag("dl");
            //prepare Definition Term -> Name
            Tag attachmentHeader = tag("dt").with(
                    tag("span").with(tag("b").withText("Attachments: "))
            );
            //Prepare Definition Discription -> attachments
            Tag attachmentSummary = tag("dd").with(attachmentTagList);
            //add the Attahcments as Child Definistion List
            attachmentDL.with(attachmentHeader);
            attachmentDL.with(attachmentSummary);

            tagToAddAttachment.with(
                    tag("dd").with(attachmentDL)
            );
        }
    }


    private static Tag parseAttachment(PropertyListDictionary pld) {
        String filename = (String) pld.getPropertyValue("FileName");
        Tag attachment;
        //if filename contains Screenshot -> parse as a new column (should be placed with TITLE)
        if (filename.contains("Screenshot")) {
            attachment = tag("p")
                    .with(tag("span")
                                    .with(
                                            tag("b").withText("Screenshot: "))
                    )
                    .withText(filename)
                    .with(tag("br")
                                    .with(
                                            tag("img").withSrc(findFile(filename))
                                    )
                    );
        } else { //if filename contains ElementOfInterest -> parse as new row in table
            attachment = tag("p")
                    .with(tag("b").withText("File Attachment: "))
                    .withText(filename);
        }

        return attachment;
    }

    private static String findFile(String filename) {
        try {
            return TestSummaryOutputHelper.getRelativePathToAttachedFile(filename);
        } catch (IOException e) {
            LoggingHelper.logError(e);
            return filename;
        }
    }

    private static Boolean checkNotNull(Object value) {
        return value != null ? true : false;
    }

    private static Tag expandCollapseULListScript() {
        String script = "$(function(){\n" +
                "$('#" + rootTagNode + "').find('SPAN').click(function(e){\n" +
                "    $(this).parent().children('UL').toggle();\n" +
                "});\n" +
                "});";
        ContainerTag expandCollapseScriptTag = tag("script").attr("type","text/javascript").withText(script);

        return expandCollapseScriptTag;
    }

    private static Tag expandCollapseDLListScript() {
        String script = "$('dt').click(function(e){\n" +
                "    $(this).nextUntil('dt').toggle();\n" +
                "});\n" +
                "\n" +
                "$('dd').hide();";
        ContainerTag expandCollapseScriptTag = tag("script").attr("type","text/javascript").withText(script);

        return expandCollapseScriptTag;
    }

    private static Tag prepareStyle(){
        return PrepareCSS.getStyleTag();
    }
}