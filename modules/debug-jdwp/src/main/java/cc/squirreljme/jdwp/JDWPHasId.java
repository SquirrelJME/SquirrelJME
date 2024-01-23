// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * Has an ID number.
 *
 * @since 2021/04/18
 */
public interface JDWPHasId
{
	/**
	 * Returns the debugger ID.
	 * 
	 * @return The debugger ID.
	 * @since 2021/03/12
	 */
	int debuggerId();
}
