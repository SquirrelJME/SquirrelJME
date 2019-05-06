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
	/** The IP Address. */
	protected final IPAddress ipaddr;
	
	/** The file. */
	protected final FileAddress file;
	
	/** The query. */
	protected final String query;
	
	/** The fragment. */
	protected final String fragment;
	
	/**
	 * Initializes the HTTP Address.
	 *
	 * @param __ip The IP address.
	 * @param __file The file.
	 * @param __query The query, may be {@code null}.
	 * @param __frag The fragment, may be {@code null}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public HTTPAddress(IPAddress __ip, FileAddress __file, String __query,
		String __frag)
		throws NullPointerException
	{
		if (__ip == null || __file == null)
			throw new NullPointerException("NARG");
		
		this.ipaddr = __ip;
		this.file = __file;
		this.query = __query;
		this.fragment = __frag;
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
	 * Returns the IP address of this HTTP address.
	 *
	 * @return The IP Address.
	 * @since 2019/05/06
	 */
	public final IPAddress ipAddress()
	{
		return this.ipaddr;
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
		
		// {@squirreljme.error EC0b HTTP address must start with double
		// slash. (The URI part)}
		if (!__p.startsWith("//"))
			throw new IllegalArgumentException("EC0b " + __p);
		__p = __p.substring(2);
		
		// Debug
		todo.DEBUG.note("Decode %s", __p);
		
		// Only contains the host part
		int sl = __p.indexOf('/');
		if (sl < 0)
			return new HTTPAddress(IPAddress.of(__p), FileAddress.of("/"),
				null, null);
		
		// Parse host portion
		IPAddress ipaddr = IPAddress.of(__p.substring(0, sl));
		
		// Parse remaining part, but keep the slash
		__p = __p.substring(sl);
		
		// Parse fragment
		String fragment;
		int fl = __p.indexOf('#');
		if (fl >= 0)
			throw new todo.TODO();
		
		// No fragment used
		else
			fragment = null;
		
		// Parse query
		String query;
		int ql = __p.indexOf('?');
		if (ql >= 0)
			throw new todo.TODO();
		
		// No query used
		else
			query = null;
		
		// Build remaining address
		return new HTTPAddress(ipaddr, FileAddress.of(__p), query, fragment);
	}
}

