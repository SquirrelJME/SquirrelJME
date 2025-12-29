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
 * Generic URI part.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class UriGenericPart
	extends UriPart
	implements UriPartAuthority, UriPartFragment, UriPartPath,
		UriPartPathParameter, UriPartQueryParameter
{
	/**
	 * Parses the given URI part as a generic part.
	 *
	 * @param __part The part to parse.
	 * @throws InvalidUriException If the part is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public UriGenericPart(String __part)
		throws InvalidUriException, NullPointerException
	{
		super(__part);
		
		throw Debugging.todo();
	}
	
	@Override
	public String getAuthority()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getFragment()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String getPath()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String pathParam(int __dx)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	@Override
	public int pathParamCount()
	{
		throw Debugging.todo();
	}
	
	@Override
	public String queryParam(int __dx)
		throws IndexOutOfBoundsException
	{
		throw Debugging.todo();
	}
	
	@Override
	public int queryParamCount()
	{
		throw Debugging.todo();
	}
}
