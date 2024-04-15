// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.logging.Logger;

/**
 * Logger instance holders.
 *
 * @since 2022/07/01
 */
final class __LogHolder__
{
	/** The logger to log to. */
	public final Logger logger;
	
	/**
	 * Initializes the log holder.
	 * 
	 * @param __logger The logger to log to.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/01
	 */
	public __LogHolder__(Logger __logger)
		throws NullPointerException
	{
		if (__logger == null)
			throw new NullPointerException("NARG");
		
		this.logger = __logger;
	}
}
