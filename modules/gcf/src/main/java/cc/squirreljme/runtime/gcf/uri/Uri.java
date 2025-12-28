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
 * This is a URI which consists of a scheme and a {@link UriPart}.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public final class Uri
	implements Comparable<Uri>
{
	@SquirrelJMEVendorApi
	public Uri(String __uri)
	{
		throw Debugging.todo();
	}
	
	@SquirrelJMEVendorApi
	public Uri(String __scheme, String __part)
	{
		throw Debugging.todo();
	}
	
	@SquirrelJMEVendorApi
	public Uri(String __scheme, UriPart<?> __part)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public int compareTo(@NotNull Uri __o)
	{
		throw Debugging.todo();
	}
}
