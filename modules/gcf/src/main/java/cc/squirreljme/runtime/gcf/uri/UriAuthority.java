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
	public UriAuthority(String __auth)
		throws InvalidUriException, NullPointerException
	{
		if (__auth == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
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
	
	@Override
	public int hashCode()
	{
		throw Debugging.todo();
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
		throw Debugging.todo();
	}
}
