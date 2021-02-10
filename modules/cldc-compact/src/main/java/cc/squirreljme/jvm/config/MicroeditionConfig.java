// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.config;

/**
 * Represents the Microedition-Config setting used.
 *
 * @since 2020/04/09
 */
@Deprecated
public interface MicroeditionConfig
{
	/** Unknown. */
	@Deprecated
	byte UNKNOWN =
		0;
	
	/** CLDC Compact. */
	@Deprecated
	byte CLDC_8_COMPACT =
		1;
	
	/** CLDC Full. */
	@Deprecated
	byte CLDC_8_FULL =
		2;
}
