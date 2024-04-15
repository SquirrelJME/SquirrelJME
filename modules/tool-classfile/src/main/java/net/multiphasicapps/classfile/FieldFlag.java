// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * These are flags which are associated with class fields.
 *
 * @since 2016/04/23
 */
public enum FieldFlag
	implements MemberFlag
{
	/** Public field. */
	PUBLIC,
	
	/** Private field. */
	PRIVATE,
	
	/** Protected field. */
	PROTECTED,
	
	/** Static field. */
	STATIC,
	
	/** Final field. */
	FINAL,
	
	/** Volatile field. */
	VOLATILE,
	
	/** Transient field. */
	TRANSIENT,
	
	/** Synthetic field. */
	SYNTHETIC,
	
	/** Enumeration. */
	ENUM,
	
	/* End. */
	;
	
	/**
	 * Returns the bit mask which is used for this flag.
	 *
	 * @return The bit mask used for the flag.
	 * @since 2017/07/07
	 */
	@Override
	public final int javaBitMask()
	{
		switch (this)
		{
			case PUBLIC:	return 0x0001;
			case PRIVATE:	return 0x0002;
			case PROTECTED:	return 0x0004;
			case STATIC:	return 0x0008;
			case FINAL:		return 0x0010;
			case VOLATILE:	return 0x0040;
			case TRANSIENT:	return 0x0080;
			case SYNTHETIC:	return 0x1000;
			case ENUM:		return 0x4000;

			default:
				throw Debugging.oops();
		}
	}
}

