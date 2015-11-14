package com.grimsmiler.Helpers.FileHelper;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by ilya on 13/11/15.
 */
public class TestSummaryOutputHelper {

    private static File xcodeAttachmentFolder;
    private static File xcodeOutputFolder;
    private static File xcodeTestFolder;
    private static File xcodeTestSummaryPlist;
    private static File attachmentsForHTMLReport;
    private static File htmlReportFolder;

    public static File getHtmlReportFolder() {
        return htmlReportFolder;
    }

    public static File getXcodexcodeOutputFolder() {
        return xcodeOutputFolder;
    }

    public static File getXcodeAttachmentFolder() {
        return xcodeAttachmentFolder;
    }

    public static File getXcodeTestSummaryPlist() {
        return xcodeTestSummaryPlist;
    }

    public static void prepareHelper(String pathToxcodeOutputFolder) throws IOException {
        //check that entered folder exists, otherwise throw exception.
        if(!(new File(pathToxcodeOutputFolder)).exists()) {
            throw new FileNotFoundException("Ouput Folder not found. Check the entered path. Exiting");
        } else { // if found
            //assign to class variable
            xcodeOutputFolder = new File(pathToxcodeOutputFolder);
            //identify subfolder we are interested in
            xcodeTestFolder = new File(xcodeOutputFolder + "/Logs/Test/");

            //check that subfolders exist -> else throw FileNotFound
            if(!xcodeTestFolder.exists()) {
                throw new FileNotFoundException("<OUTPUT_FOLDER>/Logs/Test folder not found. Check the entered path. Exiting");
            }

            xcodeAttachmentFolder = new File(pathToxcodeOutputFolder + "/Logs/Test/Attachments");

            if(!xcodeAttachmentFolder.exists()) {
                throw new FileNotFoundException("<OUTPUT_FOLDER>/Logs/Test/Attachments folder not found. Check the entered path. Exiting");
            }

            FilenameFilter filter = getFileFilterWithFilenamePart("TestSummaries.plist");

            //if checks passed -> find *TestSummaries.plist -> should be 1, otherwise throw Exception
            File[] filenameMatchArray = xcodeTestFolder.listFiles(filter);
            if(filenameMatchArray.length == 1) {
                xcodeTestSummaryPlist = filenameMatchArray[0];
                prepareTestSummaryOutputDirectory();
            } else if (filenameMatchArray.length == 0) {
                throw new FileNotFoundException("Could not find TestSummaries.plist. <OUTPUT_FOLDER>/logs/Tests should have at least 1 (and only 1!) plist! Exiting");
            } else if (filenameMatchArray.length > 1) {
                throw new FileNotFoundException("Found several plists with name TestSummaries. Please clean up the folder. There can be only 1 plist.");
            }
        }
    }
    
    public static void prepareTestSummaryOutputDirectory() throws IOException{
        htmlReportFolder = new File("./HTML_Test_Report");
        prepareAttachmentsForHTMLReport();
    }

    public static void prepareAttachmentsForHTMLReport() throws IOException{
        attachmentsForHTMLReport = new File(htmlReportFolder.getCanonicalPath() + "/Attachments_For_HTML_Report");

        copy(xcodeAttachmentFolder,attachmentsForHTMLReport);
    }

    private static final void copy( File source, File destination ) throws IOException {
        if( source.isDirectory() ) {
            copyDirectory( source, destination );
        } else {
            copyFile( source, destination );
        }
    }

    private static final void copyDirectory( File source, File destination ) throws IOException {
        if( !source.isDirectory() ) {
            throw new IllegalArgumentException( "Source (" + source.getPath() + ") must be a directory." );
        }

        if( !source.exists() ) {
            throw new IllegalArgumentException( "Source directory (" + source.getPath() + ") doesn't exist." );
        }

        if( destination.exists() ) {
            destination.delete();
        }

        destination.mkdirs();
        File[] files = source.listFiles();

        for( File file : files ) {
            if( file.isDirectory() ) {
                copyDirectory( file, new File( destination, file.getName() ) );
            } else {
                copyFile( file, new File( destination, file.getName() ) );
            }
        }
    }

    private static final void copyFile( File source, File destination ) throws IOException {
        FileChannel sourceChannel = new FileInputStream( source ).getChannel();
        FileChannel targetChannel = new FileOutputStream( destination ).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
        sourceChannel.close();
        targetChannel.close();
    }

    public static String getRelativePathToAttachedFile(String filename) throws IOException {
        FilenameFilter filter = getFileFilterWithFilenamePart(filename);
        File[] fileList = attachmentsForHTMLReport.listFiles(filter);
        if (fileList.length == 1) {
            return fileList[0].getCanonicalPath();
        } else if(fileList.length > 1) {
            throw new FileNotFoundException("Found several matching files. Please clean up the output folder folder. There can be only 1 match for attachments.");
        } else {
            throw new FileNotFoundException("Could not find " + filename + " in attachments folder for HTML Report. Exiting...");
        }
    }

    private static FilenameFilter getFileFilterWithFilenamePart(final String filenamePart) {
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains(filenamePart);
            }
        };

        return filter;
    }
}
