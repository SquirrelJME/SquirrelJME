// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.uri;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import static cc.squirreljme.runtime.cldc.debug.ErrorCode.__error__;

/**
 * This represents the authority component of a URI, which generally contains
 * the hostname and port.
 *
 * @since 2025/12/29
 */
@SquirrelJMEVendorApi
public final class UriAuthority
	implements Comparable<UriAuthority>
{
	/** The original authority. */
	@SquirrelJMEVendorApi
	protected final String original;
	
	/** The specified user. */
	@SquirrelJMEVendorApi
	protected final String user;
	
	/** The host. */
	@SquirrelJMEVendorApi
	protected final String host;
	
	/** The port. */
	@SquirrelJMEVendorApi
	protected final int port;
	
	/**
	 * Parses the authority.
	 *
	 * @param __auth The authority to parse.
	 * @throws InvalidUriException If the authority is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/30
	 */
	public UriAuthority(String __auth)
		throws InvalidUriException, NullPointerException
	{
		if (__auth == null)
			throw new NullPointerException("NARG");
		
		// Keep original around
		this.original = __auth;
		
		// Perform a check for invalid characters
		for (int n = __auth.length(), i = 0; i < n; i++)
		{
			char c = __auth.charAt(i);
			
			/* {@squirreljme.error EC26 Invalid URI character. (The URI part;
			the character)} */
			if (!UriPart.isAny(c))
				throw new InvalidUriException(
					__error__("EC26 %s %c", __auth, c));
		}
		
		// Get the positions of @ for username and : for the port
		int ap = __auth.indexOf('@');
		int lc = __auth.lastIndexOf(':');
		int v6 = __auth.lastIndexOf(']');
		
		// If the colon appears before the @, then there is no port
		if (ap >= 0 && lc < ap)
			lc = -1;
		
		// If the colon appears before a ], then there is no port as this is
		// an IPv6 address
		if (v6 >= 0 && lc < v6)
			lc = -1;
		
		// Start and end range for the host
		int hs = (ap >= 0 ? ap + 1 : 0);
		int he = (lc >= 0 ? lc : __auth.length());
		
		// Extract and decode parts
		this.user = (ap < 0 ? null :
			UriPart.decode(__auth.substring(0, ap)));
		this.host = (he <= hs ? null :
			UriPart.decode(__auth.substring(hs, he)));
		
		// Decode port
		if (lc < 0)
			this.port = -1;
		else
			try
			{
				int port = Integer.parseInt(
					__auth.substring(lc + 1), 10);
				
				/* {@squirreljme.error EC27 Invalid URI port.
				(The URI part)} */
				if (port < 0)
					throw new InvalidUriException(
						__error__("EC27 %s", __auth));
				
				this.port = port;
			}
			catch (NumberFormatException __e)
			{
				throw new InvalidUriException(
					__error__("EC27 %s", __auth), __e);
			}
	}
	
	@Override
	public int compareTo(@NotNull UriAuthority __uriAuthority)
	{
		throw Debugging.todo();
	}
	
	@Override
	public boolean equals(Object __o)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public int hashCode()
	{
		return this.original.hashCode();
	}
	
	/**
	 * Returns the specified host.
	 *
	 * @return The specified host.
	 * @since 2025/12/29
	 */
	@SquirrelJMEVendorApi
	public String host()
	{
		return this.host;
	}
	
	/**
	 * Returns the port.
	 *
	 * @return The port.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public int port()
	{
		return this.port;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/30
	 */
	@Override
	public String toString()
	{
		return this.original;
	}
	
	/**
	 * Returns the user.
	 *
	 * @return The user.
	 * @since 2025/12/30
	 */
	@SquirrelJMEVendorApi
	public String user()
	{
		return this.user;
	}
}
