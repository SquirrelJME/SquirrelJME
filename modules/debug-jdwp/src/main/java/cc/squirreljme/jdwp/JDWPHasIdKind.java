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
 * Has an ID number with a kind.
 *
 * @since 2024/01/23
 */
public interface JDWPHasIdKind
{
	/**
	 * Returns the debugger ID.
	 * 
	 * @return The debugger ID.
	 * @since 2021/03/12
	 */
	JDWPId debuggerId();
}
