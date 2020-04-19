// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is just a {@link VMClassLibrary} which can have its name be
 * overridden, in the event that this is needed.
 *
 * @since 2020/04/19
 */
public class NameOverrideClassLibrary
	implements VMClassLibrary
{
	/** The base library. */
	protected final VMClassLibrary base;
	
	/** The new name of the library. */
	protected final String name;
	
	/**
	 * Initializes the name overriding class library.
	 *
	 * @param __base The base library to rename.
	 * @param __name The new name to use for the library.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/04/19
	 */
	public NameOverrideClassLibrary(VMClassLibrary __base, String __name)
		throws NullPointerException
	{
		if (__base == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.base = __base;
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public String[] listResources()
	{
		return this.base.listResources();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public String name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/04/19
	 */
	@Override
	public InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		return this.base.resourceAsStream(__rc);
	}
}
