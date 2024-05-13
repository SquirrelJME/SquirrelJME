// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

/**
 * This is used to delay the refresh of the form within the event loop.
 *
 * @since 2020/10/09
 */
@Deprecated
final class __Refresher__
	implements Runnable
{
	/** The form to be refreshed. */
	private final SwingForm form;
	
	/**
	 * Initialize the callback.
	 * 
	 * @param __form The form to be refreshed.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/09
	 */
	public __Refresher__(SwingForm __form)
		throws NullPointerException
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		this.form = __form;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/09
	 */
	@Override
	public void run()
	{
		this.form.__refresh();
	}
}
