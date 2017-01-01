// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

/**
 * This is the base class for things which are positioned in the class.
 *
 * @since 2016/09/11
 */
@Deprecated
abstract class __Positioned__
{
	/** The data start position. */
	volatile long _startpos =
		-1L;
	
	/** The data end position. */
	volatile long _endpos =
		-1L;
}

