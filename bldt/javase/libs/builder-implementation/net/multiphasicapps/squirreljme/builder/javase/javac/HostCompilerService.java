// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.javase.javac;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import net.multiphasicapps.javac.Compiler;
import net.multiphasicapps.javac.CompilerService;

/**
 * This provides the host compiler as a service.
 *
 * @since 2017/11/28
 */
public class HostCompilerService
	implements CompilerService
{
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public Compiler createInstance()
	{
		JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		if (javac == null)
			return null;
		return new HostCompiler(javac);
	}
}

