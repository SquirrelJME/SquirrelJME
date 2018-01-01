// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.javase;

import java.nio.file.Path;
import net.multiphasicapps.squirreljme.runtime.kernel.KernelProgram;

/**
 * This is the base class for Java programs, this just contains the information
 * needed to know where the JARs are located.
 *
 * @since 2017/12/31
 */
public abstract class JavaProgram
	extends KernelProgram
{
	/** The path to the JAR. */
	protected final Path path;
	
	/**
	 * Initializes the base Java program.
	 *
	 * @param __dx The program index.
	 * @param __path The path to the JAR.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/31
	 */
	public JavaProgram(int __dx, Path __path)
		throws NullPointerException
	{
		super(__dx);
		
		if (__path == null)
			throw new NullPointerException("NARG");
		
		this.path = __path;
	}
	
	/**
	 * Returns the path to the JAR.
	 *
	 * @return The JAR path.
	 * @since 2017/12/31
	 */
	public final Path jarPath()
	{
		return this.path;
	}
}

