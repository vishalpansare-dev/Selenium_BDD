package com.utility.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.core.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.testng.asserts.SoftAssert;

import com.aspose.words.Document;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.groupdocs.comparison.Comparer;
import com.groupdocs.comparison.result.ChangeInfo;

import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tools.zip.ZipEntry;

import java.util.Date;

/**
 * FileUtil class contains the methods related to file operations. Like - read
 * file, prepare Map from properties file, delete file etc.
 * 
 */
public class FileUtil {
	SoftAssert sAssert = new SoftAssert();

	private FileUtil() {

	}

	/**
	 * Method to load properties files
	 * 
	 * @param file File name
	 * @return Properties instance
	 */
	public static Properties loadPropertyFile(String file) {
		File f = new File(file);
		Properties prop = new Properties();

		if (f.exists()) {
			try {
				FileInputStream in = new FileInputStream(f);
				prop.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}

	/**
	 * Read PDF File
	 */
	public static String readPdfFile(String filepath) {
		File f = new File(filepath);
		String text = null;
		try {
			PDDocument document = PDDocument.load(f);
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);

		} catch (IOException e) {

			e.printStackTrace();
		}
		return text;
	}

	/**
	 * Method to compare expected word files with actual work file
	 * 
	 * @param file File name
	 * @throws IOException
	 */

	public static String readDownloadedFile(String fileName, ExtentTest test, SoftAssert sAssert) throws IOException {
		String returnFileData = null;
		try {
			String downloadedFileName = templateLettersGetFileName(fileName);
			File secondFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "DownloadFiles" + File.separator + fileName);
			secondFile.setReadable(true); // read
			secondFile.setWritable(true); // write
			secondFile.setExecutable(true);
			String actualFileName = secondFile.getPath();

			XWPFDocument docx = new XWPFDocument(new FileInputStream(actualFileName));
			XWPFWordExtractor we = new XWPFWordExtractor(docx);
			System.out.println(we.getText());
			returnFileData = we.getText();

		} catch (Exception e) {

		}
		return returnFileData;
	}

	public static void convertDocToPDF(String fileName) throws XWPFConverterException, IOException {

		InputStream docFile = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + "src"
				+ File.separator + "DownloadFiles" + File.separator + fileName + ".docx"));
		XWPFDocument doc = new XWPFDocument(docFile);
		PdfOptions Pdf = PdfOptions.create();
		OutputStream out = new FileOutputStream(new File(System.getProperty("user.dir") + File.separator + "src"
				+ File.separator + "DownloadFiles" + File.separator + fileName + ".pdf"));
		PdfConverter.getInstance().convert(doc, out, Pdf);
	}

	public static void compareFiles(String fileName, ExtentTest test, SoftAssert sAssert) {

		String expectedFileName = templateLettersGetFileName(fileName);

		File firstFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
				+ "ExpectedFiles" + File.separator + expectedFileName);

		String getExpectedFileName = firstFile.getName();

		File secondFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
				+ "DownloadFiles" + File.separator + fileName + ".docx");
		String actualFileName = secondFile.getName();
		try {

			Comparer comparer = new Comparer(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "ExpectedFiles" + File.separator + expectedFileName);

			// add target file
			comparer.add(System.getProperty("user.dir") + File.separator + "src" + File.separator + "DownloadFiles"
					+ File.separator + fileName + ".docx");

			// compare
			final Path resultPath = comparer.compare();

			// get changes
			ChangeInfo[] changes = comparer.getChanges();
			System.out.println("Count of changes: " + changes.length);
			if (changes.length == 0) {
				sAssert.assertTrue((changes.length == 0), "Downloaded File not Mached with Expected File");
				sAssert.assertSame(actualFileName, getExpectedFileName,
						"Downloaded File is Matched with Expected File");
				test.log(Status.PASS, MarkupHelper.createLabel(
						"Downloaded File - \"" + fileName + "\" - is same as that of  - \"" + actualFileName + "\"",
						ExtentColor.GREEN));

			} else {
				sAssert.fail("Downloaded file not matched with Expected file");
				test.log(Status.FAIL, MarkupHelper.createLabel(
						"Downloaded File - \"" + fileName + "\" - is not same as that of  - \"" + actualFileName + "\"",
						ExtentColor.RED));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			test.log(Status.FAIL, MarkupHelper.createLabel(
					"Downloaded File - \"" + fileName + "\" - is not same as that of  - \"" + actualFileName + "\"",
					ExtentColor.RED));
		}

	}

	/**
	 * Method to get exact file name from Expected Files Directory
	 * 
	 * @param file File name
	 */

	public static String templateLettersGetFileName(String fileName) {
		File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "DownloadFiles"
				+ File.separator);
		String expectedFile = null;
		String[] fileList = file.list();
		for (String name : fileList) {
			if (name.contains("AcknowledgmentCard")) {
				expectedFile = name;
			} else if (name.contains("'HavaLetter'")) {
				expectedFile = name;

			} else if (name.contains("MissingInformationTemplate")) {
				expectedFile = name;

			} else if (name.contains("PreviousJurisdiction")) {
				expectedFile = name;

			} else if (name.contains("VoterPurgeLetter")) {
				expectedFile = name;

			} else if (name.contains("VoterTransferNotice")) {
				expectedFile = name;
			} else if (name.contains("NextVote")) {
				expectedFile = name;
			}

		}
		return expectedFile;

	}

	private static boolean isEqual(File sourceFile, File destinationFile) {
		try {
			return FileUtils.contentEquals(sourceFile, destinationFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method to zip the test report folder and store it at decided location
	 * 
	 * @param folderToZip              Location of folder to zip
	 * @param zipFolderNameAndLocation Name and location of zip folder
	 * @throws IOException exception
	 */
	public static void zipFolder(String folderToZip, String zipFolderNameAndLocation) throws IOException {

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(Paths.get(zipFolderNameAndLocation).toFile()));
		Files.walkFileTree(Paths.get(folderToZip), new SimpleFileVisitor<Path>() {

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				zos.putNextEntry(new ZipEntry(Paths.get(folderToZip).relativize(file).toString()));
				Files.copy(file, zos);
				zos.closeEntry();
				return FileVisitResult.CONTINUE;
			}
		});
		zos.close();
	}

	/**
	 * Method to delete files from the test report folder
	 * 
	 * @param pathOfFolderToDeleteFilesFrom path of folder from which files are to
	 *                                      be deleted
	 */
	public static void deleteFileFromFolder(String pathOfFolderToDeleteFilesFrom) {
		delete(new File(pathOfFolderToDeleteFilesFrom));
	}

	/**
	 * Method to delete files from folder
	 * 
	 * @param file file to delete
	 */
	private static void delete(File file) {
		if (file.isDirectory()) {
			for (File deleteMe : file.listFiles()) {
				// recursive delete
				delete(deleteMe);
			}
		}
		if (file.isFile()) {
			file.delete();
		}
	}

	private static void emptyDMVImportDirectory() {
		String filePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "DMV Import"
				+ File.separator;

		File file = new File(filePath);
		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {

			e.printStackTrace();
		}

		File DMVImport = new File(filePath);
		if (!DMVImport.exists()) {
			DMVImport.mkdirs();
		}

	}

	public static void replacestringWordFile(String expectedFileName) throws Exception {

		try {
			File firstFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "ExpectedFiles" + File.separator + expectedFileName + "-Original.docx");
			delete(firstFile);

			File secondFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "ExpectedFiles" + File.separator + expectedFileName + ".docx");

			delete(secondFile);

			File originalFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ expectedFileName + "-Original.docx");
			Date date = new Date();

			FileUtils.copyFile(originalFile, firstFile);

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			String strDate = formatter.format(date);

//			Path path = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator
//					+ "ExpectedFiles" + File.separator + expectedFileName);
			Document doc = new Document(System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "ExpectedFiles" + File.separator + expectedFileName + "-Original.docx");

			doc.getRange().replace("07/13/2022", strDate);

			doc.save(System.getProperty("user.dir") + File.separator + "src" + File.separator + "ExpectedFiles"
					+ File.separator + expectedFileName + ".docx");

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public static String renamexmlFile(String expectedFileName) throws Exception {

		String returnxmlFileName = null;
		File firstxmlFile = new File(
				System.getProperty("user.dir") + File.separator + "src" + File.separator + expectedFileName);

		File copyFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "DMV Import"
				+ File.separator + expectedFileName);

		emptyDMVImportDirectory();
		FileUtils.copyFile(firstxmlFile, copyFile);

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		LocalDateTime now = LocalDateTime.now();
		System.out.println(dtf.format(now));
		String s = dtf.format(now).toString();
		File secondFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
				+ "DMV Import" + File.separator + s + "_" + expectedFileName);

		boolean flag = copyFile.renameTo(secondFile);

		if (flag == true) {
			System.out.println("File Successfully Rename");
		}
		// if renameTo() return false then else block is
		// executed
		else {
			System.out.println("Operation Failed");
		}
		return returnxmlFileName = secondFile.getName();

	}

	public static String getDMVFileName() throws Exception {

		File folder = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator);
		File[] listOfFiles = folder.listFiles();
		String filename = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			filename = listOfFiles[i].getName();
			if (filename.endsWith(".xml") || filename.endsWith(".XML")) {
				System.out.println(filename);
				break;
			}

		}
		return filename;

	}

	public static String getUpdatedDMVFileName() throws Exception {

		File folder = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "DMV Import"
				+ File.separator);
		File[] listOfFiles = folder.listFiles();
		String filename = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			filename = listOfFiles[i].getName();
			if (filename.endsWith(".xml") || filename.endsWith(".XML")) {
				System.out.println(filename);
				break;
			}

		}
		return filename;

	}

	// getZip File Name
	public static String getDownloadedZipFileName() throws IOException {
		String returnFileData = null;
		try {
			Util.cleardownloadfileDirectory();
			String source = System.getProperty("user.dir") + File.separator + "src" + File.separator + "DownloadFiles"
					+ File.separator + "ballots.zip";
			String destination = System.getProperty("user.dir") + File.separator + "src" + File.separator
					+ "DownloadFiles";

			ZipFile zipFile = new ZipFile(source);

			zipFile.extractAll(destination);

		} catch (Exception e) {

		}
		return returnFileData;
	}
}
// End of FileUtil class
