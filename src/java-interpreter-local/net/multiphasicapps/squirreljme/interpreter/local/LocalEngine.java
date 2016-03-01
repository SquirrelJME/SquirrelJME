// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter.local;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.squirreljme.interpreter.InterpreterClass;
import net.multiphasicapps.squirreljme.interpreter.InterpreterEngine;
import net.multiphasicapps.squirreljme.zips.StandardZIPFile;

/**
 * This is an extension of the interpreter engine which provides access to
 * JAR files for loading of classes.
 *
 * @since 2016/03/01
 */
public class LocalEngine
	extends InterpreterEngine
{
	/** The bootstrap class path. */
	protected final Set<Path> bootclasspath;
	
	/** The standard class path. */
	protected final Set<Path> classpath;
	
	/**
	 * This initializes the local interpreter engine.
	 *
	 * @param __bcp The boot class path.
	 * @param __cp The standard class path.
	 * @param __main The main entry class.
	 * @param __args Program arguments to pass to main.
	 * @throws IllegalArgumentException If there are no bootstrap classpath
	 * paths available.
	 * @since 2016/03/01
	 */
	public LocalEngine(Set<Path> __bcp, Set<Path> __cp, String __main,
		String... __args)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__bcp == null || __cp == null || __main == null)
			throw new NullPointerException();
		
		// Force arguments to exist
		if (__args == null)
			__args = new String[0];
		
		// Copy classpaths
		bootclasspath = MissingCollections.<Path>unmodifiableSet(
			new LinkedHashSet<>(__bcp));
		classpath = MissingCollections.<Path>unmodifiableSet(
			new LinkedHashSet<>(__cp));
		
		// Need bootstrap classes
		if (bootclasspath.isEmpty())
			throw new IllegalArgumentException("No bootstrap classes.");
		
		// Find the main class
		InterpreterClass mainclass = loadClass(__main.replace('.', '/'));
		if (mainclass == null)
			throw new IllegalArgumentException(String.format("The class " +
				"'%s' does not exist.", __main));
		
		throw new Error("TODO");
	}
}

