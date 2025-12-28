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
 * This is the part specific part of a URI.
 *
 * @since 2025/12/28
 */
@SquirrelJMEVendorApi
public abstract class UriPart
	implements Comparable<UriPart>
{
	/** The original full part. */
	@SquirrelJMEVendorApi
	protected final String original;
	
	/**
	 * Initializes the base part.
	 *
	 * @param __part The full part.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	UriPart(String __part)
		throws NullPointerException
	{
		if (__part == null)
			throw new NullPointerException("NARG");
		
		// Remember the original full part
		this.original = __part;
	}
	
	@Override
	public abstract int compareTo(@NotNull UriPart __b);
	
	/**
	 * Returns this URI as a generic URI.
	 *
	 * @return The generic URI.
	 * @throws InvalidUriException If this is not a generic URI.
	 * @since 2025/12/28
	 */
	@SquirrelJMEVendorApi
	public final UriGenericPart asGeneric()
		throws InvalidUriException
	{
		/* {@squirreljme.error EC22 This is a not a generic URI part. */
		if (!(this instanceof UriGenericPart))
			throw new InvalidUriException(
				__error__("EC22"));
		
		return (UriGenericPart)this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/28
	 */
	@Override
	public final String toString()
	{
		return this.original;
	}
}
