// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lcdui.display;

/**
 * Implements serial run.
 *
 * @since 2020/10/03
 */
class __SerialRun__
	implements Runnable
{
	/** Flagged run? */
	volatile boolean _flag;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/03
	 */
	@Override
	public void run()
	{
		synchronized (this)
		{
			this._flag = true;
			
			this.notifyAll();
		}
	}
}
