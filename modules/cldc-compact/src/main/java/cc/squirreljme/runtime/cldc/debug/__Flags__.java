// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.debug;

/**
 * Internal debug flags.
 *
 * @since 2026/06/22
 */
class __Flags__
{
	/** Debugging is enabled. */
	static final boolean _ENABLED =
		Boolean.parseBoolean("true");
	
	/** Verbose debugging is enabled. */
	static final boolean _VERBOSE =
		Boolean.getBoolean("cc.squirreljme.verbose");
}
