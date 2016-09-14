// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.doclet.markdown;

import com.sun.javadoc.ClassDoc;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.markdownwriter.MarkdownWriter;

/**
 * This loads the class doclet documentation and obtains information so that
 * it may be neatly output.
 *
 * @since 2016/09/13
 */
public class MarkdownClass
{
	/** The main doclet. */
	protected final DocletMain main;
	
	/** The wrapped class doclet. */
	protected final ClassDoc doc;
	
	/** The base class name path, uses directory components. */
	protected final Path basenamepath;
	
	/** The base path for the markdown file. */
	protected final Path basemarkdownpath;
	
	/** The qualified name of this class. */
	protected final String qualifiedname;
	
	/** Is this class explicit? */
	volatile boolean _implicit;
	
	/**
	 * Initializes the markdown wrapped class.
	 *
	 * @param __dm The main doclet.
	 * @param __cd The class to reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	public MarkdownClass(DocletMain __dm, ClassDoc __cd)
		throws NullPointerException
	{
		// Check
		if (__dm == null || __cd == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.main = __dm;
		this.doc = __cd;
		
		// Setup qualified name
		String qualifiedname = __cd.qualifiedName();
		this.qualifiedname = qualifiedname;
		
		// Register class
		__dm.__registerClass(__cd.qualifiedName(), this);
		
		// Determine the base name path for this class
		{
			// Fill with fragments
			List<String> bnp = new ArrayList<>();
			int n = qualifiedname.length();
			int base = 0;
			for (int i = 0; i <= n; i++)
			{
				// Use an implicit dot so it gets treated as a fragment piece
				char c = (i == n ? '.' : qualifiedname.charAt(i));
				
				// New grouping?
				if (c == '.')
				{
					// Split
					bnp.add(qualifiedname.substring(base, i));
					
					// Character after this one
					base = i + 1;
				}
			}
		
			// Setup
			Path p;
			this.basenamepath = (p = Paths.get(bnp.remove(0),
				bnp.<String>toArray(new String[bnp.size()])));
			
			// Setup name for markdown file location
			this.basemarkdownpath = __lowerPath(
				p.resolveSibling(p.getFileName() + ".mkd"));
		}
	}
	
	/**
	 * Writes the class documentation details to the output markdown file.
	 *
	 * @since 2016/09/13
	 */
	public void writeOutput()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Lowercases the entire path set.
	 *
	 * @param __p The path to lowercase.
	 * @return The lowercase form of the path.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	static Path __lowerPath(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Add lowercase forms
		List<String> bnp = new ArrayList<>();
		for (Path p : __p)
			bnp.add(__lowerString(p.toString()));
		
		// Rebuild
		return Paths.get(bnp.remove(0),
			bnp.<String>toArray(new String[bnp.size()]));
	}
	
	/**
	 * Lowercases the specified string and replaces out of range characters
	 * with escaped symbols.
	 *
	 * @param __s The string to lowercase.
	 * @return The string lowercased in pure ASCII.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/13
	 */
	static String __lowerString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Build
		StringBuilder sb = new StringBuilder();
		int n = __s.length();
		for (int i = 0; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// Funny character?
			if (!(c == '.' || c == '_' || (c >= '0' && c <= '9') ||
				(c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')))
			{
				sb.append('_');
				sb.append(String.format("%04x", c & 0xFFFF));
			}
			
			// Lower?
			else if (c >= 'A' && c <= 'Z')
				sb.append((char)('a' + (c - 'A')));
				
			// same
			else
				sb.append(c);
		}
		
		// Return
		return sb.toString();
	}
}

