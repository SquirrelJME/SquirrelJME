// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class will read in imports from a file and then sort them so that they
 * are in alphabetical order.
 *
 * @since 2018/01/14
 */
public class SortImports
{
	public static final Comparator<String> INSENSITIVE_COMPARATOR =
		new Comparator<String>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2018/01/04
			 */
			@Override
			public int compare(String __a, String __b)
			{
				// Use case insensitive comparison first
				int rv = __a.compareToIgnoreCase(__b);
				if (rv != 0)
					return rv;
				
				// If two strings are the same case, wise, sort it again
				// using case sensitive, so that two similar strings with
				// the same case still sort accordingly as if there were no
				// case, but capital letters are first.
				return __a.compareTo(__b);
			}
		};
	
	/** The file to sort. */
	protected final Path path;
	
	/**
	 * Initializes the sorter.
	 *
	 * @param __p The path to sort.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/14
	 */
	public SortImports(Path __p)
		throws NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		this.path = __p;
	}
	
	/**
	 * Runs the sorting script.
	 *
	 * @throws IOException On read/write errors.
	 * @since 2018/01/14
	 */
	public void run()
		throws IOException
	{
		// Read all lines in file, it is unspecified if the list is modifiable
		// so make sure it is
		Path path = this.path;
		List<String> inlines = new ArrayList<>(Files.readAllLines(path,
			Charset.forName("utf-8")));
		
		// Find the lines which are import statements
		int n = inlines.size(),
			start = -1,
			end = -1;
		boolean wasimport = false;
		for (int i = 0; i < n; i++)
		{
			boolean isimport = inlines.get(i).startsWith("import");
			
			// Now is an import
			if (isimport)
			{
				// Ignore
				if (wasimport)
					continue;
				
				// Mark start
				else
				{
					start = i;
					wasimport = true;
				}
			}
			
			// Not an import
			else
			{
				// Was an import? Mark end
				if (wasimport)
				{
					end = i;
					break;
				}
				
				// Otherwise find other lines
				else
					continue;
			}
		}
		
		// No import statements?
		if (start < 0 || end < 0)
			return;
		
		// Get sub-list to sort
		List<String> sub = inlines.subList(start, end);
		
		// Sort that
		Collections.sort(sub, INSENSITIVE_COMPARATOR);
		
		// Write out entire file again
		Files.write(path, inlines, Charset.forName("utf-8"),
			StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/10/14
	 */
	public static void main(String... __args)
		throws IOException
	{
		for (String s : __args)
			new SortImports(Paths.get(s)).run();
	}
}

