// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

/**
 * This represents a single definition of a kernel service which manages that
 * are defined.
 *
 * @since 2018/03/15
 */
public final class KernelService
{
	/** The service index. */
	protected final int index;
	
	/** The server class. */
	protected final String serverclass;
	
	/** The client class. */
	protected final String clientclass;
	
	/**
	 * Initializes the service information.
	 *
	 * @param __dx The service index.
	 * @param __cl The client class.
	 * @param __sv The server class.
	 * @since 2018/03/15
	 */
	public KernelService(int __dx, String __cl, String __sv)
		throws NullPointerException
	{
		if (__cl == null || __sv == null)
			throw new NullPointerException("NARG");
		
		this.index = __dx;
		this.clientclass = __cl;
		this.serverclass = __sv;
	}
	
	/**
	 * Returns the client class.
	 *
	 * @return The client class.
	 * @since 2018/03/15
	 */
	public final String clientClass()
	{
		return this.clientclass;
	}
	
	/**
	 * Returns the class which provides for the client.
	 *
	 * @return The providing provider class.
	 * @since 2018/03/15
	 */
	public final String clientProviderClass()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the index of this service.
	 *
	 * @return The service index.
	 * @since 2018/03/15
	 */
	public final int index()
	{
		return this.index;
	}
}

