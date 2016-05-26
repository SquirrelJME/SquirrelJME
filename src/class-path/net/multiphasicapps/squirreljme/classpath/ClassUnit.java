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

import net.multiphasicapps.narf.classinterface.NCIClass;

/**
 * This is a class unit which is a collection of classes and resources such as
 * those contained within a single JAR.
 *
 * @since 2016/05/25
 */
public abstract class ClassUnit
	implements Comparable<ClassUnit>
{
	/**
	 * {@inheritDoc}
	 * @since 2016/05/26
	 */
	@Override
	public int compareTo(ClassUnit __cu)
	{
		return toString().compareTo(__cu.toString());
	}
	
	/**
	 * Returns the name of the JAR file which is used for the given class
	 * unit.
	 *
	 * @return The class unit key.
	 * @since 2016/05/26
	 */
	@Override
	public abstract String toString();
}

