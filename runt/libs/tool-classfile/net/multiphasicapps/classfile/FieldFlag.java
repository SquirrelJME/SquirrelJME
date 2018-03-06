// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

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
	
	/** End. */
	;
	
	/**
	 * Returns the bit mask which is used for this flag.
	 *
	 * @return The bit mask used for the flag.
	 * @since 2017/07/07
	 */
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
				throw new RuntimeException("OOPS");
		}
	}
}

