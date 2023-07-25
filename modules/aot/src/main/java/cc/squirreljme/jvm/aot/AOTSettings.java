// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

/**
 * Ahead of time process settings.
 *
 * @since 2023/07/25
 */
public final class AOTSettings
{
	/** The compiler being used. */
	public final String compiler;
	
	/** The name. */
	public final String name;
	
	/** The mode. */
	public final String mode;
	
	/** The clutter level currently used. */
	public final String clutterLevel;
	
	/**
	 * Initializes the settings.
	 *
	 * @param __compiler The compiler used.
	 * @param __name The name.
	 * @param __mode The mode.
	 * @param __clutterLevel The current clutter level.
	 * @since 2023/07/25
	 */
	public AOTSettings(String __compiler, String __name, String __mode,
		String __clutterLevel)
	{
		this.compiler = __compiler;
		this.name = __name;
		this.mode = __mode;
		this.clutterLevel = __clutterLevel;
	}
}
