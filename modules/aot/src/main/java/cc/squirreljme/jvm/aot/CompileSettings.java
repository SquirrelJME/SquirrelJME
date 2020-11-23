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
 * This class contains settings for compilation.
 *
 * @since 2020/11/22
 */
public final class CompileSettings
{
	/** Is this a boot loader? */
	public final boolean isBootLoader;
	
	/**
	 * Initializes the compilation settings.
	 * 
	 * @param __isBootLoader Is this a boot loader?
	 * @since 2020/11/23
	 */
	public CompileSettings(boolean __isBootLoader)
	{
		this.isBootLoader = __isBootLoader;
	}
}
