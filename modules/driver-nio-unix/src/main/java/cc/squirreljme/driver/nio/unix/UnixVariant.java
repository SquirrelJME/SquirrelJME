// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.driver.nio.unix;

/**
 * Represents the variant of Unix File systems.
 *
 * @since 2023/08/20
 */
public enum UnixVariant
{
	/** Standard variant. */
	STANDARD,
	
	/** Linux. */
	LINUX,
	
	/** BSD. */
	BSD,
	
	/** Mac OS. */
	MACOS,
	
	/* End. */
	;
	
	/**
	 * Is this a BSD filesystem?
	 *
	 * @return If this is BSD.
	 * @since 2023/08/20
	 */
	public final boolean isBsd()
	{
		return this == UnixVariant.BSD ||
			this == UnixVariant.MACOS;
	}
}
