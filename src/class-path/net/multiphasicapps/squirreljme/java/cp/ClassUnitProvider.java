// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;

/**
 * This is a provider for {@link ClassUnit}s which are then constructed into
 * {@link ClassPath}s used for execution.
 *
 * If a class unit does not change, then it must use the same reference.
 *
 * @since 2016/05/25
 */
public abstract class ClassUnitProvider
{
	/**
	 * Returns the array of class units which are available by this provider.
	 *
	 * @return The array of class units.
	 * @since 2016/05/25
	 */
	public abstract ClassUnit[] classUnits();
	
	/**
	 * This loads the given units from this given provider and creates a
	 * {@link ClassPath} which allows for verification and resource lookup.
	 *
	 * @param __un The name of the units to load along with all of their
	 * dependencies.
	 * @return The classpath with the given unit and its dependencies.
	 * @throws IllegalArgumentException If no units were specified.
	 * @throws MissingClassUnitException If a required dependency is missing.
	 * @since 2016/06/20
	 */
	public final ClassPath getStandardClassPath(String... __un)
		throws IllegalArgumentException, MissingClassUnitException
	{
		// {@squirreljme.error BN0l No class unit names were specified.}
		if (__un == null || __un.length <= 0)
			throw new IllegalArgumentException("BN0l");
		
		// Obtain all the units
		ClassUnit[] units = classUnits();
		
		// The units to be processed and ones already determined/added
		Deque<ClassUnit> inq = new LinkedList<>();
		Set<ClassUnit> did = new HashSet<>();
		
		// Add inputs to the queue
		List<ClassUnit> rv = new LinkedList<>();
		for (String s : __un)
		{
			// Find the class unit
			ClassUnit cu = __locate(units, s);
			
			// {@squirreljme.error BN0m An input class unit was not available.
			// (The requested input class unit)}
			if (cu == null)
				throw new MissingClassUnitException(String.format("BN0m %s",
					s));
			
			// Add it
			inq.add(cu);
		}
		
		// Process anything in the queue
		while (!inq.isEmpty())
		{
			// Process the given package?
			ClassUnit cu = inq.removeFirst();
			if (!did.add(cu))
				continue;
			
			// Add to return value
			rv.add(cu);
			
			// Get the manifest for the unit
			JavaManifest man;
			try
			{
				man = cu.manifest();
			}
			
			// {@squirreljme.error BN0p Failed to read the manifest of a given
			// class unit. (The class unit where the manifest could not be
			// read)}
			catch (IOException e)
			{
				throw new MissingClassUnitException(String.format("BN0p %s",
					cu), e);
			}
			
			// {@squirreljme.error BN0n A class unit was processed however it
			// does not have a manifest. (The class unit)}
			if (man == null)
				throw new MissingClassUnitException(String.format("BN0n %s",
					cu.toString()));
			
			// Get main attributes
			JavaManifestAttributes ma = man.getMainAttributes();
			
			// Get the class path
			String classpath = ma.get("class-path");
			//if (classpath != null)
			{
				// Go through the dependencies and use them all
				int n = classpath.length();
				for (int i = 0; i < n; i++)
				{
					char c = classpath.charAt(i);
					
					// Ignore whitespace
					if (c <= ' ')
						continue;
					
					// Find the next whitespace character
					int j;
					for (j = i + 1; j < n; j++)
						if (classpath.charAt(j) <= ' ')
							break;
					
					// Split off
					String spl = classpath.substring(i, j);
					
					// Set next
					i = j;
					
					// Locate the dependencies
					ClassUnit dep = __locate(units, spl);
					
					// {@squirreljme.error BN0o A dependency of a given class
					// unit does not exist. (The source class unit; The class
					// unit it depends on)}
					if (dep == null)
						throw new MissingClassUnitException(String.format(
							"BN0o %s %s", cu, spl));
					
					// Add it to be processed
					inq.offerLast(dep);
				}
			}
		}
		
		// Build it
		return new ClassPath(rv.<ClassUnit>toArray(new ClassUnit[rv.size()]));
	}
	
	/**
	 * Locates a single class unit using the specified name.
	 *
	 * @param __un The unit to locate.
	 * @return The class unit with the given name, or {@code null} if not
	 * found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	public final ClassUnit locateClassUnit(String __un)
		throws NullPointerException
	{
		// Check
		if (__un == null)
			throw new NullPointerException("NARG");
		
		return __locate(classUnits(), __un);
	}
	
	/**
	 * Locates the given class unit within an array of class units.
	 *
	 * @param __uns The class units to look in.
	 * @param __un The class unit to find.
	 * @return The located class unit or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/20
	 */
	private static final ClassUnit __locate(ClassUnit[] __uns, String __un)
		throws NullPointerException
	{
		// Check
		if (__uns == null || __un == null)
			throw new NullPointerException("NARG");
		
		// Go through the array and find a match
		for (ClassUnit cu : __uns)
			if (cu.equals(__un))
				return cu;
		
		// Not found
		return null;
	}
}

