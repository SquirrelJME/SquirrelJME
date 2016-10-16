// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap;

import java.io.InputStream;
import java.io.IOException;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceReference;
import net.multiphasicapps.squirreljme.projects.ProjectInfo;

/**
 * This provides access to resources.
 *
 * @since 2016/09/21
 */
class __Reference__
	implements ResourceReference
{
	/** The project information. */
	protected final ProjectInfo info;
	
	/** The name of the resource. */
	protected final String name;
	
	/**
	 * Initializes the resource reference.
	 *
	 * @param __in The project information.
	 * @param __n The resource name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/1
	 */
	__Reference__(ProjectInfo __in, String __n)
		throws NullPointerException
	{
		// Check
		if (__in == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.info = __in;
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/21
	 */
	@Override
	public InputStream open()
	{
		// Open it
		try
		{
			return this.info.open(this.name);
		}
		
		// {@squirreljme.error CL07 Could not open the resource.}
		catch (IOException e)
		{
			throw new RuntimeException("CL07", e);
		}
	}
}

