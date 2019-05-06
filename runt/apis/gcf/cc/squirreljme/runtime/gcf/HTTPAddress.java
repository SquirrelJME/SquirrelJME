// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

/**
 * This represents an IP address.
 *
 * @since 2019/05/06
 */
public final class HTTPAddress
	implements SocketAddress
{
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the IP address of this HTTP address.
	 *
	 * @return The IP Address.
	 * @since 2019/05/06
	 */
	public final IPAddress ipAddress()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Decodes an address from the URI part.
	 *
	 * @param __p The part to decode from.
	 * @return The HTTP address.
	 * @throws IllegalArgumentException If the address is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final HTTPAddress fromUriPart(String __p)
		throws IllegalArgumentException, NullPointerException
	{
		if (__p == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

