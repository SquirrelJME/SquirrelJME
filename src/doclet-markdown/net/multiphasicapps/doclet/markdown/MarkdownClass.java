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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
			this.basenamepath = Paths.get(bnp.remove(0), bnp.<String>toArray(
				new String[bnp.size()]));
			System.err.printf("DEBUG -- %s%n", this.basenamepath);
		}
	}
}

