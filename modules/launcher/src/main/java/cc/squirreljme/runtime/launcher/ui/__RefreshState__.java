// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

/**
 * The current refresh state.
 *
 * @since 2021/12/29
 */
final class __RefreshState__
{
	/** The message to use. */
	volatile String _message;
	
	/** The current at. */
	volatile int _at;
	
	/** The current total. */
	volatile int _total;
	
	/**
	 * Sets the message and other information.
	 * 
	 * @param __message The message used.
	 * @param __at The current at.
	 * @param __total The total count.
	 * @since 2021/12/29
	 */
	public void set(String __message, int __at, int __total)
	{
		this._message = __message;
		this._at = __at;
		this._total = __total;
	}
}
