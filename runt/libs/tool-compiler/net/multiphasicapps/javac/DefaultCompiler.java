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

import java.util.ServiceLoader;
import net.multiphasicapps.javac.cute.CuteCompiler;

/**
 * This creates an instance of a compiler and should be the class which is
 * used to initializes the compiler. If no compiler services are provided then
 * the default internal compiler will be used instead.
 *
 * @since 2017/11/28
 */
public final class DefaultCompiler
{
	/**
	 * {@squirreljme.property net.multiphasicapps.javac.forcedefault=(boolean)
	 * If this is set to {@code true} then the default built-in compiler will
	 * be used.}
	 */
	private static final String _FORCE_DEFAULT_PROPERTY =
		"net.multiphasicapps.javac.forcedefault";
	
	/**
	 * Not used.
	 *
	 * @since 2017/11/28
	 */
	private DefaultCompiler()
	{
	}
	
	/**
	 * Creates a new instance of a compiler.
	 *
	 * @return The new compiler instance.
	 * @since 2017/11/28
	 */
	public static final Compiler createInstance()
	{
		// Go through services if it is not forced to default
		if (!Boolean.getBoolean(_FORCE_DEFAULT_PROPERTY))
			for (CompilerService cs : ServiceLoader.<CompilerService>load(
				CompilerService.class))
			{
				Compiler rv = cs.createInstance();
				if (rv != null)
					return rv;
			}
		
		// No services, use the built-in compiler
		return new CuteCompiler();
	}
}

