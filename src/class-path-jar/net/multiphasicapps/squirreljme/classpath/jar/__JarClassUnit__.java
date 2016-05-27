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

import net.multiphasicapps.squirreljme.ci.CIClass;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;

/**
 * This provides access to classes from a JAR file.
 *
 * @since 2016/05/25
 */
final class __JarClassUnit__
	extends ClassUnit
{
	/** The provider for the class unit. */
	protected final JarClassUnitProvider provider;
	
	/** The key this is associated with. */
	protected final String key;
	
	/**
	 * This represents a single class unit which is provided within a JAR.
	 *
	 * @param __jcup The provider for class units.
	 * @param __k The key this maps to.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public __JarClassUnit__(JarClassUnitProvider __jcup, String __k)
		throws NullPointerException
	{
		// Check
		if (__jcup == null || __k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.provider = __jcup;
		this.key = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/26
	 */
	@Override
	public String toString()
	{
		return this.key;
	}
}

