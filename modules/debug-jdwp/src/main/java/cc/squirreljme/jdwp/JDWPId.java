// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Anything that has an ID utilized by the debugger.
 *
 * @since 2021/03/13
 */
public interface JDWPId
{
	/**
	 * Returns the debugger ID.
	 * 
	 * @return The debugger ID.
	 * @since 2021/03/12
	 */
	int debuggerId();
}
