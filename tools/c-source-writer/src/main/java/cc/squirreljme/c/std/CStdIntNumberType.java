// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.std;

import cc.squirreljme.c.CNumberType;

/**
 * Number types for {@code stdint.h}.
 *
 * @since 2023/06/24
 */
public enum CStdIntNumberType
	implements CNumberType
{
	/** Signed 8-bit integer. */
	INT8("INT8_C"),
	
	/** Unsigned 8-bit integer. */
	UINT8("UINT8_C"),
	
	/** Signed 16-bit integer. */
	INT16("INT16_C"),
	
	/** Unsigned 16-bit integer. */
	UINT16("UINT16_C"),
	
	/** Signed 32-bit integer. */
	INT32("INT32_C"),
	
	/** Unsigned 32-bit integer. */
	UINT32("UINT32_C"),
	
	/** Signed 64-bit integer. */
	INT64("INT64_C"),
	
	/** Unsigned 64-bit integer. */
	UINT64("UINT64_C"),
	
	/* End. */
	;
	
	/** The surround used. */
	protected final String surround;
	
	/**
	 * Initializes the number type.
	 * 
	 * @param __surround The surround to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	CStdIntNumberType(String __surround)
		throws NullPointerException
	{
		if (__surround == null)
			throw new NullPointerException("NARG");
		
		this.surround = __surround;
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
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public String surround()
	{
		return this.surround;
	}
}
