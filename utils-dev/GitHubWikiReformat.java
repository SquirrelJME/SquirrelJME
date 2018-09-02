// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * Reformats a Wiki page for the GitHub Wiki.
 *
 * This is assumed to be running on Linux.
 *
 * @since 2018/09/02
 */
public class GitHubWikiReformat
{
	/**
	 * Main entry point.
	 *
	 * @param __args The first argument is the file being looked at, a relative
	 * path from the root.
	 * @throws Throwable On any exception.
	 * @since 2018/09/02
	 */
	public static void main(String... __args)
		throws Throwable
	{
		if (__args == null || __args.length != 1 || __args[0] == null)
			throw new IllegalArgumentException("Expected path specifying " +
				"the file being modified.");
		
		// Turn it into a path, make root to simplify it
		Path file = Paths.get("/", __args[0]),
			parent = file.getParent();
		System.err.printf("Reformatting %s (in %s)...%n", file, parent);
		
		// Read original file into string
		String original;
		try (Reader r = new InputStreamReader(System.in, "utf-8"))
		{
			StringBuilder sb = new StringBuilder();
			
			for (;;)
			{
				int c = r.read();
				
				if (c < 0)
					break;
				
				sb.append((char)c);
			}
			
			original = sb.toString();
		}
		
		// Pattern used to search for file sequences
		Pattern want = Pattern.compile("\\([^)]*\\.mkd\\)");
		
		// Output result
		StringBuilder out = new StringBuilder();
		
		// Replace patterns
		Matcher match = want.matcher(original);
		for (int lastdx = 0;;)
		{
			// Try to find it
			if (!match.find())
			{
				// Add everything from the last match to the end
				out.append(original.substring(lastdx));
				
				// Stop
				break;
			}
			
			// Starting and end points, used to sub-sequence
			int start = match.start(),
				end = match.end();
			
			// Add everything from the last index to this match
			out.append(original.substring(lastdx, start));
			
			// Set last index to the end of our match, so it is not picked up
			// again
			lastdx = end;
			
			// The opening parenthesis was lost
			out.append('(');
			
			// Some magic needs to be performed on the path, make it a
			// relative path always
			Path origpath = Paths.get(parent + "/" +
				original.substring(start + 1, end - 1));
			
			// Remove the leading slash
			String storepath = origpath.toString().substring(1);
			
			// Convert characters specially
			out.append(storepath.replaceAll(Pattern.quote("/"), "@d@").
				replaceAll(Pattern.quote("-"), "@h@").
				replaceAll(Pattern.quote(".mkd"), ""));
			
			// The ending parenthesis was lost
			out.append(')');
		}
		
		System.out.print(out);
	}
}

