package edu.pitt.dbmi.nlp.noble.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * The Class FileTools.
 */
public class FileTools {

	/**
	 * unzip file to directory copy/pasted from
	 * http://javadevtips.blogspot.com/2011/10/unzip-files.html
	 * http://www.thecoderscorner.com/team-blog/java-and-jvm/12-reading-a-zip-file-from-java-using-zipinputstream
	 * and modified
	 *
	 * @param is the is
	 * @param destDirectory the dest directory
	 * @throws Exception the exception
	 */
	public static void unzip(InputStream is, File destDirectory) throws Exception {
		// create the destination directory structure (if needed)
		if (!destDirectory.exists())
			destDirectory.mkdirs();

		// create a buffer to improve copy performance later.
		byte[] buffer = new byte[2048];

		// open the zip file stream
		ZipInputStream stream = new ZipInputStream(new BufferedInputStream(is));
		try {

			// now iterate through each item in the stream. The get next
			// entry call will return a ZipEntry for each file in the
			// stream
			ZipEntry entry;
			while ((entry = stream.getNextEntry()) != null) {
				// Once we get the entry from the stream, the stream is
				// positioned read to read the raw data, and we keep
				// reading until read returns 0 or less.
				File outpath = new File(destDirectory, entry.getName());
				if (entry.isDirectory()) {
					outpath.mkdirs();
				} else {
					if (!outpath.getParentFile().exists())
						outpath.getParentFile().mkdirs();
					FileOutputStream output = null;
					try {
						output = new FileOutputStream(outpath);
						int len = 0;
						while ((len = stream.read(buffer)) > 0) {
							output.write(buffer, 0, len);
						}
					} finally {
						// we must always close the output file
						if (output != null)
							output.close();
					}
				}
			}
		} finally {
			// we must always close the zip file.
			stream.close();
		}
	}

	/**
	 * recursivley delete directory.
	 *
	 * @param directory the directory
	 */
	public static void deleteDirectory(File directory){
		if(directory.exists()){
			for(File f: directory.listFiles()){
				if(f.isDirectory())
					deleteDirectory(f);
				else
					f.delete();
			}
			directory.delete();
		}
	}
	
	/**
	 * This method gets a text file (HTML too) from input stream from given map.
	 *
	 * @param in the in
	 * @return String that was produced
	 * @throws IOException             if something is wrong WARNING!!! if you use this to read HTML
	 *             text and want to put it somewhere you should delete newlines
	 */
	public static String getText(InputStream in) throws IOException {
		return getText(in, "\n");
	}

	/**
	 * This method gets a text file (HTML too) from input stream from given map.
	 *
	 * @param in the in
	 * @param lineSeparator the line separator
	 * @return String that was produced
	 * @throws IOException             if something is wrong WARNING!!! if you use this to read HTML
	 *             text and want to put it somewhere you should delete newlines
	 */
	public static String getText(InputStream in, String lineSeparator) throws IOException {
		StringBuilder strBuf = new StringBuilder();
		BufferedReader buf = new BufferedReader(new InputStreamReader(in));
		try {
			for (String line = buf.readLine(); line != null; line = buf.readLine()) {
				strBuf.append(line + lineSeparator);
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			buf.close();
		}
		return strBuf.toString();
	}
	
	/**
	 * get recursive directory content that is sorted
	 * @param directory - directory to recurse into
	 * @param filter - extention filter that valid files can end with
	 * @return sorted list of files taht are contained in a directory
	 */
	
	public static List<File> getFilesInDirectory(File directory, String filter){
		List<File> files = getFiles(directory, new ArrayList<File>(), filter);
		Collections.sort(files);
		return files;
	}
	
	/**
	 * get recursive directory content that is sorted
	 * @param directory- directory to recurse into
	 * @return sorted list of files taht are contained in a directory
	 */
	
	public static List<File> getFilesInDirectory(File directory){
		List<File> files = getFiles(directory, new ArrayList<File>(),null);
		Collections.sort(files);
		return files;
	}
	

	/**
	 * Gets the files.
	 *
	 * @param in the in
	 * @param list the list
	 * @return the files
	 */
	private static List<File> getFiles(File in,List<File> list, String filter) {
		if(in.isDirectory()){
			for(File f: in.listFiles()){
				getFiles(f,list,filter);
			}
		}else if(in.isFile() && (filter == null  || in.getName().endsWith(filter))){
			list.add(in);
		}
		return list;
	}

	
}
