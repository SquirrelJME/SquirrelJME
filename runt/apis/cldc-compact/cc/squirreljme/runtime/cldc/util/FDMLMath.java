// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This class contains math methods which are derived from the Freely
 * Distributed Math Library, which is written in C which is then translated
 * into Java.
 *
 * The library is located at http://www.netlib.org/fdlibm/ and was
 * developed at Sun Microsystems, Inc.
 *
 * @since 2018/11/02
 */
@ImplementationNote("This code is derived from the Freely " +
	"Distributable Math Library (http://www.netlib.org/fdlibm/).")
public final class FDMLMath
{
	/**
	 * Not used.
	 *
	 * @since 2018/11/02
	 */
	private FDMLMath()
	{
	}
	
	/**
	 * Logarithm of a number.
	 *
	 * @param __v The input value.
	 * @return The resulting logarithm value.
	 * @since 2018/11/02
	 */
	@ImplementationNote("Source http://www.netlib.org/fdlibm/e_log.c")
	public static double log(double __v)
	{
		throw new todo.TODO();
	}
	
	/**
	 * Square root of a number.
	 *
	 * @param __v The input value.
	 * @return The resulting square root value.
	 * @since 2018/11/02
	 */
	@ImplementationNote("Source: http://www.netlib.org/fdlibm/e_sqrt.c")
	public static double sqrt(double __v)
	{
		throw new todo.TODO();
	}
}

