// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Primitive number types.
 *
 * @since 2023/06/24
 */
public enum CPrimitiveNumberType
	implements CNumberType
{
	/** Plain. */
	PLAIN(null),
	
	/** Unsigned. */
	UNSIGNED("U"),
	
	/** Long. */
	LONG("L"),
	
	/** Unsigned long. */
	UNSIGNED_LONG("UL"),
	
	/** Long-long. */
	LONG_LONG("LL"),
	
	/** Unsigned long-long. */
	UNSIGNED_LONG_LONG("ULL"),
	
	/* End. */
	;
	
	/** The number suffix. */
	protected final String suffix;
	
	/**
	 * Initializes the primitive number type.
	 * 
	 * @param __suffix The suffix.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	CPrimitiveNumberType(String __suffix)
		throws NullPointerException
	{
		if (__suffix == null)
			throw new NullPointerException("NARG");
		
		this.suffix = __suffix;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public String prefix()
	{
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public String suffix()
	{
		return this.suffix;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public String surround()
	{
		return null;
	}
}
