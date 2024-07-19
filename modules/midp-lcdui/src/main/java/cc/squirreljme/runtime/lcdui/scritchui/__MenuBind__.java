// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

/**
 * Internal menu binding.
 *
 * @since 2024/07/19
 */
final class __MenuBind__
{
	/** This action for this menu bind. */
	public final MenuAction<?> action;
	
	/**
	 * Initializes the binding.
	 *
	 * @param __action The menu action bound to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/19
	 */
	__MenuBind__(MenuAction<?> __action)
		throws NullPointerException
	{
		if (__action == null)
			throw new NullPointerException("NARG");
		
		this.action = __action;
	}
}
