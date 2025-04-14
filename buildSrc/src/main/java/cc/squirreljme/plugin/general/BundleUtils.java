// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import java.net.URI;

/**
 * Bundling utilities.
 *
 * @since 2024/03/04
 */
public final class BundleUtils
{
	/**
	 * Not used.
	 *
	 * @since 2024/03/04
	 */
	private BundleUtils()
	{
	}
	
	/**
	 * Gets the JSON data from the given URI. 
	 *
	 * @param <J> The class type.
	 * @param __class The class type.
	 * @param __uri The URI to get from.
	 * @return The resultant data.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/04
	 */
	public static <J> J get(Class<J> __class, String __uri)
		throws NullPointerException
	{
		if (__class == null || __uri == null)
			throw new NullPointerException("NARG");
		
		return BundleUtils.get(__class, URI.create(__uri));
	}
	
	/**
	 * Gets the JSON data from the given URI. 
	 *
	 * @param <J> The class type.
	 * @param __class The class type.
	 * @param __uri The URI to get from.
	 * @return The resultant data.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/04
	 */
	public static <J> J get(Class<J> __class, URI __uri)
		throws NullPointerException
	{
		if (__class == null || __uri == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}
