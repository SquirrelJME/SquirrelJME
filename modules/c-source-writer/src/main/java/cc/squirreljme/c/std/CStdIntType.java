// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c.std;

import cc.squirreljme.c.COpaqueType;
import cc.squirreljme.c.CType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Standard {@code stdint.h} types.
 *
 * @since 2023/06/24
 */
public enum CStdIntType
	implements CTypeProvider
{
	/** Signed 8-bit integer. */
	INT8("int8_t"),
	
	/** Unsigned 8-bit integer. */
	UINT8("uint8_t"),
	
	/** Signed 16-bit integer. */
	INT16("int16_t"),
	
	/** Unsigned 16-bit integer. */
	UINT16("uint16_t"),
	
	/** Signed 32-bit integer. */
	INT32("int32_t"),
	
	/** Unsigned 32-bit integer. */
	UINT32("uint32_t"),
	
	/** Signed 64-bit integer. */
	INT64("int64_t"),
	
	/** Unsigned 64-bit integer. */
	UINT64("uint64_t"),
	
	/* End. */
	;
	
	/** The token used for the type. */
	protected final String token;
	
	/** The type cache. */
	private volatile Reference<CType> _type;
	
	/**
	 * Initializes the base type.
	 * 
	 * @param __token The token used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	CStdIntType(String __token)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		this.token = __token;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CType type()
	{
		Reference<CType> ref = this._type;
		CType rv;
		
		if (ref == null || (rv = ref.get()) == null)
		{
			// This is opaque because from the general code perspective, we
			// really do not know what the true type of this is...
			rv = COpaqueType.of(this.token, false);
			this._type = new WeakReference<>(rv);
		}
		
		return rv;
	}
}
