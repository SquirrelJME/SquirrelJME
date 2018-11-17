// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import java.io.InputStream;
import java.io.IOException;

/**
 * This is a class library resource which is based on class resources.
 *
 * @since 2018/11/14
 */
public final class ResourceBasedClassLibrary
	implements VMClassLibrary
{
	/** The class to get resources from. */
	protected final Class<?> actingclass;
	
	/** The prefix for entries. */
	protected final String prefix;
	
	/** The name of this library. */
	protected final String name;
	
	/**
	 * Initializes the resource based class library.
	 *
	 * @param __act The class to act on.
	 * @param __pre The prefix for lookup.
	 * @param __name The name of the library.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/15
	 */
	public ResourceBasedClassLibrary(Class<?> __act, String __pre,
		String __name)
		throws NullPointerException
	{
		if (__act == null || __pre == null || __name == null)
			throw new NullPointerException("NARG");
		
		this.actingclass = __act;
		this.prefix = __pre;
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final String name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public final InputStream resourceAsStream(String __rc)
		throws IOException, NullPointerException
	{
		if (__rc == null)
			throw new NullPointerException("NARG");
		
		return this.actingclass.getResourceAsStream(
			this.prefix + __rc);
	}
}

