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
 * This represents a flag which may be associated with an inner class.
 *
 * @since 2018/05/15
 */
public enum InnerClassFlag
	implements Flag
{
	/** Public. */
	PUBLIC,
	
	/** Private. */
	PRIVATE,
	
	/** Protected. */
	PROTECTED,
	
	/** Static. */
	STATIC,
	
	/** Final. */
	FINAL,
	
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
	 * @since 2018/05/15
	 */
	@Override
	public final int javaBitMask()
	{
		switch (this)
		{
			case PUBLIC:		return 0x0001;
			case PRIVATE:		return 0x0002;
			case PROTECTED:		return 0x0004;
			case STATIC:		return 0x0008;
			case FINAL:			return 0x0010;
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

