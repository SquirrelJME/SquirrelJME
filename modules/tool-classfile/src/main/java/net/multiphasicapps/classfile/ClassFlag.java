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
 * These are flags which modify how a class is accessed and is behaved.
 *
 * @since 2016/04/23
 */
public enum ClassFlag
	implements Flag
{
	/** Public access. */
	PUBLIC,
	
	/** Final. */
	FINAL,
	
	/** Super. */
	SUPER,
	
	/** Interface. */
	INTERFACE,
	
	/** Abstract. */
	ABSTRACT,
	
	/** Synthetic. */
	SYNTHETIC,
	
	/** Annotation. */
	ANNOTATION,
	
	/** Enumeration. */
	ENUM,
	
	/** End. */
	;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/13
	 */
	@Override
	public final int javaBitMask()
	{
		switch (this)
		{
			case PUBLIC:		return 0x0001;
			case FINAL:			return 0x0010;
			case SUPER:			return 0x0020;
			case INTERFACE:		return 0x0200;
			case ABSTRACT:		return 0x0400;
			case SYNTHETIC:		return 0x1000;
			case ANNOTATION:	return 0x2000;
			case ENUM:			return 0x4000;
			default:
				throw new todo.OOPS();
		}
	}
}

