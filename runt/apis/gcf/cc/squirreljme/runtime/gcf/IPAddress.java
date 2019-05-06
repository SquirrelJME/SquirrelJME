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
 * This is an immutable class which represents an IP address and possibly the
 * port.
 *
 * @since 2019/05/06
 */
public final class IPAddress
	implements SocketAddress
{
	/** System assigned port. */
	public static final int ASSIGNED_PORT =
		-1;
	
	/** The hostname. */
	public final String hostname;
	
	/** The port. */
	public final int port;
	
	/**
	 * Initializes the address.
	 *
	 * @param __h The hostname, may be {@code null}.
	 * @param __p The port, may be {@link ASSIGNED_PORT}.
	 * @throws IllegalArgumentException If the hostname or port is not valid.
	 * @since 2019/05/06
	 */
	public IPAddress(String __h, int __p)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EC06 IP port out of range. (The port)}
		if (__p != ASSIGNED_PORT && __p < 0 || __p > 65535)
			throw new IllegalArgumentException("EC06 " + __p);
		
		// {@squirreljme.error EC07 No IP address was specified.}
		if (__h == null && __p == ASSIGNED_PORT)
			throw new IllegalArgumentException("EC07");
		
		// Validate hostname
		if (__h != null)
		{
			// Make it lowercase
			__h = __h.toLowerCase();
			
			// Is this an IPv6 address?
			boolean isvsix;
			String checkpart;
			if ((isvsix = __h.startsWith("[")))
			{
				// {@squirreljme.error EC09 IPv6 address must end in bracket.
				// (The hostname)}
				if (!__h.endsWith("]"))
					throw new IllegalArgumentException("EC09 " + __h);
				
				// Only check the insides
				checkpart = __h.substring(1, __h.length() - 1);
			}
			
			// Not one
			else
				checkpart = __h;
			
			// Check characters
			for (int i = 0, n = checkpart.length(); i < n; i++)
			{
				int c = checkpart.charAt(i);
				
				// {@squirreljme.error EC0a Hostname has an invalid
				// character. (The hostname)}
				if (!((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') ||
					c == '-' || c == '.' || (isvsix && c == ':')))
					throw new IllegalArgumentException("EC0a " + __h);
			}
		}
		
		// Set
		this.hostname = __h;
		this.port = __p;
	}
	
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
	 * Is this a server connection?
	 *
	 * @return If this is a server connection.
	 * @since 2019/05/06
	 */
	public final boolean isServer()
	{
		return this.hostname == null;
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
	 * Creates an address from a URI part.
	 *
	 * @param __part The URI part.
	 * @return The resulting IP address.
	 * @throws IllegalArgumentException If the address is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final IPAddress fromUriPart(String __part)
		throws IllegalArgumentException, NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EC05 IP Address must start with double
		// slash. (The URI part)}
		if (!__part.startsWith("//"))
			throw new IllegalArgumentException("EC05 " + __part);
		
		// Parse
		return IPAddress.of(__part.substring(2));
	}
	
	/**
	 * Creates an address from the given string.
	 *
	 * @param __s The input string
	 * @return The resulting IP address.
	 * @throws IllegalArgumentException If the address is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final IPAddress of(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// The port could be weird
		try
		{
			// Need to detect that an IPv6 address is being used
			int col = __s.lastIndexOf(':'),
				eb = __s.lastIndexOf(']');
			if (eb >= 0 && eb > col)
				col = -1;
			
			// Just has a hostname?
			if (col < 0)
				return new IPAddress(__s, ASSIGNED_PORT);
			
			// Just has the port
			else if (col == 0)
				return new IPAddress(null,
					Integer.parseInt(__s.substring(1), 10));
			
			// Has both
			else
				return new IPAddress(__s.substring(0, col),
					Integer.parseInt(__s.substring(col + 1), 10));
		}
		
		// {@squirreljme.error EC08 Invalid port number. (The URI part)}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("EC08 " + __s, e);
		}
	}
}

