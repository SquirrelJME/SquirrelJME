// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.lib.client;

import cc.squirreljme.runtime.cldc.library.Library;
import cc.squirreljme.runtime.cldc.library.LibraryControlKey;
import cc.squirreljme.runtime.cldc.library.LibraryResourceScope;
import cc.squirreljme.runtime.cldc.library.LibraryType;
import cc.squirreljme.runtime.cldc.service.ServiceCaller;
import java.io.InputStream;

/**
 * This class represents a library as seen by the client.
 *
 * @since 2018/01/12
 */
final class __ClientLibrary__
	implements Library
{
	/** The system caller. */
	protected final ServiceCaller caller;
	
	/** The library index. */
	protected final int index;
	
	/**
	 * Initializes the library.
	 *
	 * @param __dx The library index.
	 * @param __sc The client library.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/12
	 */
	__ClientLibrary__(int __dx, ServiceCaller __sc)
		throws NullPointerException
	{
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		this.index = __dx;
		this.caller = __sc;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	public final String controlGet(LibraryControlKey __k)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	public final void controlSet(LibraryControlKey __k, String __v)
		throws NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof Library))
			return false;
		
		return this.index == ((Library)__o).index();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/03
	 */
	@Override
	public final int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/02
	 */
	@Override
	public final InputStream loadResource(LibraryResourceScope __scope,
		String __name)
		throws NullPointerException
	{
		if (__scope == null || __name == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/02
	 */
	@Override
	public final LibraryType type()
	{
		throw new todo.TODO();
	}
}

