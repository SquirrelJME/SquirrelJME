// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general.cmake;

/**
 * CMake failed to do something.
 *
 * @since 2026/08/06
 */
public class CMakeException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message.
	 * @since 2026/08/06
	 */
	public CMakeException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2026/08/06
	 */
	public CMakeException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}
