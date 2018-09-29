// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Represents the category of the conversion.
 *
 * @since 2018/09/29
 */
enum __PrintFCategory__
{
	/** General. */
	GENERAL,
	
	/** Character. */
	CHARACTER,
	
	/** Integral. */
	INTEGRAL,
	
	/** Floating point. */
	FLOATING_POINT,
	
	/** Date/Time. */
	DATE_TIME,
	
	/** Percent. */
	PERCENT,
	
	/** Line Separator. */
	LINE_SEPARATOR,
	
	/** End. */
	;
	
	/**
	 * Is the specified flag valid?
	 *
	 * @param __f The flag to check.
	 * @return If it is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/29
	 */
	final boolean __hasFlag(__PrintFFlag__ __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		switch (this)
		{
				// May only be left justified
			case CHARACTER:
			case DATE_TIME:
			case GENERAL:
				return __f == __PrintFFlag__.LEFT_JUSTIFIED;
				
				// Anything is valid
			case INTEGRAL:
				return true;
				
				// Not valid at all
			case PERCENT:
			case LINE_SEPARATOR:
				return false;
				
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Is the precision valid?
	 *
	 * @return Is the precision valid?
	 * @since 2018/09/29
	 */
	final boolean __hasPrecision()
	{
		switch (this)
		{
			case GENERAL:
			case FLOATING_POINT:
				return true;
			
			default:
				return false;
		}
	}
	
	/**
	 * Is the width valid?
	 *
	 * @return If the width is valid.
	 * @since 2018/09/29
	 */
	final boolean __hasWidth()
	{
		return this != LINE_SEPARATOR;
	}
}

