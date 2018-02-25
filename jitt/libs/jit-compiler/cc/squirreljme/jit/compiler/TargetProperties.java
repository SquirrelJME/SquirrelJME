// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.compiler;

import cc.squirreljme.jit.objectfile.DataProperties;

/**
 * This is used describe how the compiler should generate code.
 *
 * @since 2018/02/24
 */
public abstract class TargetProperties
{
	/** Properties used for writing data. */
	protected final DataProperties dataproperties;
	
	/**
	 * Initializes the target properties.
	 *
	 * @param __dp The properties to use for data.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/24
	 */
	public TargetProperties(DataProperties __dp)
		throws NullPointerException
	{
		if (__dp == null)
			throw new NullPointerException("NARG");
		
		this.dataproperties = __dp;
	}
	
	/**
	 * Returns the properties used for writing data.
	 *
	 * @return The properties for writing data.
	 * @since 2018/02/25
	 */
	public final DataProperties dataProperties()
	{
		return this.dataproperties;
	}
}

