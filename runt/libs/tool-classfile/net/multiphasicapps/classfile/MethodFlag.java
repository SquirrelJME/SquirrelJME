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
 * These are flags which are used by methods.
 *
 * @since 2016/04/23
 */
public enum MethodFlag
	implements MemberFlag
{
	/** Public method. */
	PUBLIC,
	
	/** Private method. */
	PRIVATE,
	
	/** Protected method. */
	PROTECTED,
	
	/** Static method. */
	STATIC,
	
	/** Final method. */
	FINAL,
	
	/** Synchronized method. */
	SYNCHRONIZED,
	
	/** Bridge method. */
	BRIDGE,
	
	/** Variable argument method. */
	VARARGS,
	
	/** Native method. */
	NATIVE,
	
	/** Abstract method. */
	ABSTRACT,
	
	/** Strict floating point method. */
	STRICT,
	
	/** Synthetic method. */
	SYNTHETIC,
	
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
			case PUBLIC:		return 0x0001;
			case PRIVATE:		return 0x0002;
			case PROTECTED:		return 0x0004;
			case STATIC:		return 0x0008;
			case FINAL:			return 0x0010;
			case SYNCHRONIZED:	return 0x0020;
			case BRIDGE:		return 0x0040;
			case VARARGS:		return 0x0080;
			case NATIVE:		return 0x0100;
			case ABSTRACT:		return 0x0400;
			case STRICT:		return 0x0800;
			case SYNTHETIC:		return 0x1000;

			default:
				throw new todo.OOPS();
		}
	}
}

