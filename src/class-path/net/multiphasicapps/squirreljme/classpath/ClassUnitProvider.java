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

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
		Deque<String> inq = new LinkedList<>();
		Set<String> did = new HashSet<>();
		
		// Add inputs to the queue
		List<ClassUnit> rv = new ArrayList<>();
		for (String s : __un)
			inq.add(s);
		
		// Process anything in the queue
		while (!inq.isEmpty())
		{
			if (true)
				throw new Error("TODO");
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
		
		throw new Error("TODO");
	}
}

