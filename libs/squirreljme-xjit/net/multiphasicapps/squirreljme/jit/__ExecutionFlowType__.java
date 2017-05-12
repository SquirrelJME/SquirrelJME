// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This represents the type of return that an instruction performs after it is
 * executed.
 *
 * @since 2017/05/09
 */
@Deprecated
enum __ExecutionFlowType__
{
	/** The next instruction. */
	NEXT,
	
	/** Forward control flow to another instruction address. */
	FORWARD,
	
	/** Return from the current method. */
	RETURN,
	
	/** Throw an exception from the current method. */
	THROW,
	
	/** End. */
	;
}

