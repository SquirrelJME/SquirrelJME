package cc.squirreljme.plugin;

import cc.squirreljme.plugin.util.CommentReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.TreeMap;
import org.gradle.api.Project;
import org.gradle.api.tasks.SourceTask;

/**
 * This is used to get the error lists from a project.
 *
 * @since 2020/02/22
 */
public final class ErrorListManager
{
	/** The maximum error code index that can exist. */
	private static final int _MAX_ERROR_CODE =
		Character.MAX_RADIX * Character.MAX_RADIX;
	
	/** The tag used to detect errors. */
	private static final String _ERROR_TAG =
		"squirreljme.error";
	
	/** The project to manage the list for. */
	protected final Project project;
	
	/** The listed errors. */
	private final Map<Integer, SourceError> _errors =
		new TreeMap<>();
	
	/** The error code for this project. */
	private String _projectErrorCode;
	
	/**
	 * Initializes the error code manager.
	 *
	 * @param __project The project to manage.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public ErrorListManager(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project.");
		
		this.project = __project;
	}
	
	/**
	 * Returns the next available error.
	 *
	 * @return The next available error.
	 * @since 2020/02/22
	 */
	public final String next()
	{
		return ErrorListManager.indexToString(this.nextIndex());
	}
	
	/**
	 * Returns the next available error.
	 *
	 * @return The next available error.
	 * @since 2020/02/22
	 */
	public final int nextIndex()
	{
		this.__init();
		
		Map<Integer, SourceError> errors = this._errors;
		synchronized (this)
		{
			for (int i = 0; i <= ErrorListManager._MAX_ERROR_CODE; i++)
				if (!errors.containsKey(i))
					return i;
			
			throw new IllegalArgumentException("Ran out of error indexes.");
		}
	}
	
	/**
	 * Prints the errors to the given stream.
	 *
	 * @param __out The stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public final void print(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("No stream specified.");
		
		this.__init();
		
		synchronized (this)
		{
			for (SourceError e : this._errors.values())
				__out.println(e);
		}
	}
	
	/**
	 * Initializes the code map.
	 *
	 * @since 2020/02/22
	 */
	private void __init()
	{
		Project project = this.project;
		Map<Integer, SourceError> errors = this._errors;
		
		synchronized (this)
		{
			// Already loaded?
			if (this._projectErrorCode != null)
				return;
			
			// This must exist
			SquirrelJMEPluginConfiguration config = project.getExtensions()
				.<SquirrelJMEPluginConfiguration>getByType(
					SquirrelJMEPluginConfiguration.class);
			
			// Fail if this is missing
			String projectErrorCode = config.javaDocErrorCode;
			if (projectErrorCode == null)
				throw new IllegalStateException(String.format(
					"Project %s has no error code.", project.getName()));
			
			// Used as a flag and to indicate the group for this
			this._projectErrorCode = projectErrorCode;
			
			// Temporary buffer for data reading
			StringBuilder buffer = new StringBuilder();
			
			// Parse source code
			for (File source : ((SourceTask)project.getTasks()
				.getByName("compileJava")).getSource().getFiles())
				try (InputStream in = Files.newInputStream(source.toPath(),
					StandardOpenOption.READ);
					CommentReader cr = new CommentReader(in))
				{
					Path sourcePath = source.toPath();
					
					// Easier to read everything into a buffer
					buffer.delete(0, buffer.length());
					cr.readAll(buffer);
					
					// Look for error sequences
					for (int i = 0, n = buffer.length(); i < n;)
					{
						// Find next JavaDoc tag-ish
						int jddx = buffer.indexOf("{@", i);
						if (jddx < 0)
							break;
						
						// Skip past the tag specifier
						i = jddx + 2;
						
						// Find closing brace index
						int clbr = buffer.indexOf("}", i);
						if (clbr < 0)
							break;
						
						// Find error tag, stop if not found or it is after
						// the closing brace
						int tndx = buffer.indexOf(ErrorListManager._ERROR_TAG, i);
						if (tndx < 0 || tndx > clbr)
							break;
						
						// Skip to the end of the tag
						i = tndx + ErrorListManager._ERROR_TAG.length();
						
						// Decode the tag contents
						String content = buffer.substring(i, clbr).trim();
						SourceError sourceError = null;
						try
						{
							sourceError = new SourceError(content, sourcePath);
						}
						catch (RuntimeException e)
						{
							throw new IllegalArgumentException(String.format(
								"Project %s (in %s) has invalid error %s.",
								project.getName(), source, content), e);
						}
						
						// Detect errors in the codes
						if (!projectErrorCode.equals(sourceError.projectCode))
							throw new IllegalArgumentException(String.format(
								"Project %s (in %s) has wrong project code " +
								"in %s (should be %s).",
								project.getName(), source, sourceError,
								projectErrorCode));
						
						// Make sure all codes are unique!
						SourceError dup;
						if ((dup = errors.put(sourceError.index,
							sourceError)) != null)
							throw new IllegalArgumentException(String.format(
								"Project %s (in %s) has duplicate error " +
								"%s conflicting with %s.",
								project.getName(), source, sourceError, dup));
					}
				}
				catch (IOException e)
				{
					throw new RuntimeException("Could not decode file.", e);
				}
		}
	}
	
	/**
	 * Returns string form of the given index.
	 *
	 * @param __index The index to translate.
	 * @return The translated string to index.
	 * @since 2020/02/22
	 */
	public static String indexToString(int __index)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(Character.toUpperCase(Character.forDigit(
			__index / Character.MAX_RADIX, Character.MAX_RADIX)));
		sb.append(Character.toUpperCase(Character.forDigit(
			__index % Character.MAX_RADIX, Character.MAX_RADIX)));
		
		return sb.toString();
	}
	
	/**
	 * Returns the index for the given string.
	 *
	 * @return The resulting index.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public static int stringToIndex(String __string)
		throws IllegalArgumentException, NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("No string specified.");
		
		if (__string.length() != 2)
			throw new IllegalArgumentException("Invalid string length.");
		
		int rv = (Character.digit(__string.charAt(0), Character.MAX_RADIX) *
			Character.MAX_RADIX) +
			Character.digit(__string.charAt(1), Character.MAX_RADIX);
		
		if (rv < 0 || rv > ErrorListManager._MAX_ERROR_CODE)
			throw new IllegalArgumentException("Out of range index.");
		
		return rv;
	}
}
