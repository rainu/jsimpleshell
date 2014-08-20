package de.raysha.lib.jsimpleshell.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A utility class for file operations.
 *
 * @author rainu
 */
public abstract class FileUtils {

	/**
	 * Write a content to the given file.
	 *
	 * @param targetFile The file.
	 * @param content The content to be written.
	 * @param append Append to the file?
	 * @throws IOException
	 */
	public static void write(File targetFile, String content, boolean append) throws IOException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(targetFile, append);
			writer.append(content);
		} finally {
			writer.close();
		}
	}
}
