// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * Constants used to define what happens when an exit value is given.
 *
 * @since 2020/03/07
 */
public interface ExitValueConstants
{
	/** Test passes. */
	byte SUCCESS =
		0;
	
	/** Test fails. */
	byte FAILURE =
		1;
	
	/** Test skipped. */
	byte SKIPPED =
		2;
}
