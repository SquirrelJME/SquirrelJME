// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

/**
 * This is thrown when there is an error compiling.
 *
 * @since 2022/09/07
 */
public class CompilationException
	extends RuntimeException
{
	public CompilationException()
	{
		super();
	}
	
	public CompilationException(String __m)
	{
		super(__m);
	}
	
	public CompilationException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	public CompilationException(Throwable __c)
	{
		super(__c);
	}
}
