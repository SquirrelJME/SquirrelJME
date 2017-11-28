// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

/**
 * This is used with {@link java.util.ServiceLoader} and is used to create
 * instances of the Java compiler when needed. This allows multiple compilers
 * to exist at the same time.
 *
 * Creation of the compiler should instead be done by
 * {@link DefaultCompiler#createInstance()}.
 *
 * @see DefaultCompiler
 * @since 2017/11/28
 */
public interface CompilerService
{
	/**
	 * Returns a new instance of the compiler.
	 *
	 * @return A new instance of the compiler, this may return {@code null} if
	 * a compiler cannot be provided.
	 * @since 2017/11/28
	 */
	public abstract Compiler createInstance();
}

