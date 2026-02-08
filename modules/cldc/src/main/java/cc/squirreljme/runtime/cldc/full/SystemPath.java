// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

/**
 * Represents a system path.
 *
 * @deprecated Do not use, only used by the debugger which will be
 * rewritten at some point.
 * @since 2024/02/25
 */
@Deprecated
public enum SystemPath
{
	/** Cache directory. */
	@Deprecated
	CACHE,
	
	/** Configuration directory. */
	@Deprecated
	CONFIG,
	
	/** Data directory. */
	@Deprecated
	DATA,
	
	/** State directory. */
	@Deprecated
	STATE,
	
	/* End. */
	;
}
