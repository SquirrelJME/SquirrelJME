// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.vki;

/**
 * This contains default configuration settings.
 *
 * @since 2019/04/22
 */
public interface DefaultConfiguration
{
	/** Default RAM size. */
	public static final int DEFAULT_RAM_SIZE =
		16777216;
	
	/** Default static field space. */
	public static final int DEFAULT_STATIC_FIELD_SIZE =
		8192;
	
	/** Minimum static field size. */
	public static final int MINIMUM_STATIC_FIELD_SIZE =
		1024;
}

