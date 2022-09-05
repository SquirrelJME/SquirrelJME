// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.jvm.aot.CompileSettings;
import cc.squirreljme.jvm.aot.LinkGlob;
import cc.squirreljme.jvm.aot.summercoat.target.TargetBang;
import java.util.ServiceLoader;

/**
 * Factory for creating instances of {@link SummerCoatHandler}.
 * 
 * Used with {@link ServiceLoader}.
 * 
 * @since 2022/09/04
 */
public interface SummerCoatHandlerFactory
{
	/**
	 * Creates a handle to create for the known variant. 
	 * 
	 * @param __glob The glob to create for.
	 * @return The created handler for the given glob.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/04
	 */
	SummerCoatHandler handler(SummerCoatLinkGlob __glob)
		throws NullPointerException;
	
	/**
	 * Is this capable for handling the given target bang?
	 * 
	 * @param __bang The target bang to check.
	 * @return Is this capable for the given target bang?
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/04
	 */
	boolean supportsBang(TargetBang __bang)
		throws NullPointerException;
}
