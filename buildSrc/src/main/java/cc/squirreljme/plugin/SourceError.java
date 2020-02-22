package cc.squirreljme.plugin;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A single error that exists in source code.
 *
 * @since 2020/02/22
 */
public final class SourceError
{
	/** The code for the project. */
	public final String projectCode;
	
	/** The error index. */
	public final int index;
	
	/** The body of the error. */
	public final String body;
	
	/** Extra parameters. */
	public final List<String> parameters;
	
	/** Where is this located? */
	public final Path where;
	
	/**
	 * Decodes the content to tag information.
	 *
	 * @param __content The content to decode.
	 * @param __where Where the file is located.
	 * @throws IllegalArgumentException If the content is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/22
	 */
	public SourceError(String __content, Path __where)
		throws IllegalArgumentException, NullPointerException
	{
		if (__content == null)
			throw new NullPointerException();
		
		this.where = __where;
		
		// Needs to be a given length!
		int length = __content.length();
		if (length < 4)
			throw new IllegalArgumentException("Content too short.");
		
		// Project code is the first two characters
		this.projectCode = __content.substring(0, 2).toUpperCase();
		
		// The index is the next two characters
		this.index = ErrorListManager.stringToIndex(__content.substring(2, 4));
		
		// The remaining block data
		String rest = __content.substring(4).trim();
		
		// Are parameters being used?
		int podx = rest.lastIndexOf('('),
			pcdx = rest.lastIndexOf(')');
		if (podx > 0 && pcdx > 0 && podx < pcdx)
		{
			// Body is before the parameters start
			this.body = rest.substring(0, podx).trim();
			
			// Parameters is semi-colon delimated
			String[] split = rest.substring(podx + 1, pcdx).split(
				Pattern.quote(";"));
			for (int i = 0, n = split.length; i < n; i++)
				split[i] = (split[i] == null ? "" : split[i].trim());
			this.parameters = Collections.<String>unmodifiableList(
				Arrays.<String>asList(split));
		}
		
		// No parameters used
		else
		{
			this.body = rest;
			this.parameters = Collections.<String>emptyList();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/02/22
	 */
	@Override
	public final String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		// Code index
		sb.append(this.projectCode);
		sb.append(ErrorListManager.indexToString(this.index));
		sb.append(": ");
		
		// Content
		sb.append(this.body);
		
		List<String> parameters = this.parameters;
		if (!parameters.isEmpty())
		{
			sb.append(" (");
			
			for (int i = 0, n = parameters.size(); i < n; i++)
			{
				if (i > 0)
					sb.append("; ");
				
				sb.append(parameters.get(0));
			}
			
			sb.append(")");
		}
		
		// Place location of this file
		Path where = this.where;
		if (where != null)
		{
			sb.append(" <");
			sb.append(where);
			sb.append(">");
		}
		
		return sb.toString();
	}
}
