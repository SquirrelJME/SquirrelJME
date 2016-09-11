// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

/**
 * This is the base class for things which are positioned in the class.
 *
 * @since 2016/09/11
 */
abstract class __Positioned__
{
	/** The data start position. */
	volatile long _startpos;
	
	/** The data end position. */
	volatile long _endpos;
}

