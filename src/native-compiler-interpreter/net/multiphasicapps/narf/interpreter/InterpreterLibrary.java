// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.narf.library.NLClassLibrary;

/**
 * This provides the means of locating classes which exist somewhere on the
 * classpath.
 *
 * @since 2016/04/20
 */
public class InterpreterLibrary
	extends NLClassLibrary
{
	/** The boot classpath. */
	protected final Set<Path> bootpath;
	
	/** The classpath. */
	protected final Set<Path> classpath;
	
	/**
	 * Initializes the interpreter library which uses the real filesystem or
	 * ZIP files for class data.
	 *
	 * @param __bootcp The boot classpath.
	 * @param __cp The program classpath.
	 * @since 2016/04/20
	 */
	public InterpreterLibrary(Set<Path> __bootcp, Set<Path> __cp)
		throws NullPointerException
	{
		// Check
		if (__bootcp == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// Setup boot classpath
		bootpath = MissingCollections.<Path>unmodifiableSet(
			new LinkedHashSet<>(__bootcp));
		
		// Setup standard classpath
		classpath = MissingCollections.<Path>unmodifiableSet(
			new LinkedHashSet<>(__cp));
	}
}

