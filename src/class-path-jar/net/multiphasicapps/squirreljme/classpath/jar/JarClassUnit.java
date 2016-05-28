// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar;

import net.multiphasicapps.descriptors.ClassNameSymbol;
import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.ci.CIException;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;

/**
 * This provides access to classes from a JAR file.
 *
 * @since 2016/05/25
 */
public abstract class JarClassUnit
	extends ClassUnit
{
	/** The key this is associated with. */
	protected final String key;
	
	/**
	 * This represents a single class unit which is provided within a JAR.
	 *
	 * @param __k The key this maps to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public JarClassUnit(String __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.key = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/27
	 */
	@Override
	public final CIClass locateClass(ClassNameSymbol __cns)
		throws CIException, NullPointerException
	{
		// Check
		if (__cns == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/26
	 */
	@Override
	public final String toString()
	{
		return this.key;
	}
}

