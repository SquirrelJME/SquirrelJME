// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.localinterp;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.classfile.CFMemberKey;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.IdentifierSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.interpreter.JVMClass;
import net.multiphasicapps.interpreter.JVMEngine;
import net.multiphasicapps.interpreter.JVMMethod;
import net.multiphasicapps.interpreter.JVMObject;
import net.multiphasicapps.interpreter.JVMThread;

/**
 * This is an extension of the interpreter engine which provides access to
 * JAR files for loading of classes.
 *
 * @since 2016/03/01
 */
public class LocalEngine
	extends JVMEngine
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
		super();
	
		// Check
		if (__bcp == null || __cp == null || __main == null)
			throw new NullPointerException("NARG");
		
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
			throw new IllegalArgumentException("LI01");
		
		// Setup classpath
		for (Path p : bootclasspath)
			addClassPath(new LocalClassPath(this, p));
		for (Path p : classpath)
			if (!bootclasspath.contains(p))
				addClassPath(new LocalClassPath(this, p));
		
		// Find the main class
		JVMClass mainclass = loadClass(__main);
		if (mainclass == null)
			throw new IllegalArgumentException(String.format(
				"LI02 %s", __main));
		
		// Find the main method
		JVMMethod mainmethod = mainclass.getMethods().get(
			new IdentifierSymbol("main"),
			new MethodSymbol("([Ljava/lang/String;)V"));
		if (mainmethod == null || !mainmethod.getFlags().isStatic() ||
			!mainmethod.getFlags().isPublic())
			throw new IllegalArgumentException(String.format(
				"LI03 %s", __main));
		
		// Create arguments for the main thread
		JVMObject pargs = spawnStringArray(__args);
		
		// Create main thread
		JVMThread mthread = createThread(mainmethod, pargs);
	}
}

