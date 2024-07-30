// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchViewBracket;
import cc.squirreljme.jvm.mle.scritchui.callbacks.ScritchSizeSuggestListener;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Size suggestions for scrolling views.
 *
 * @since 2024/07/29
 */
class __ExecViewSizeSuggest__
	implements ScritchSizeSuggestListener
{
	/** The ScritchUI API to use. */
	protected final ScritchInterface scritchApi;
	
	/**
	 * Initializes the size suggestion handler.
	 *
	 * @param __scritchApi The ScritchUI API.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/29
	 */
	__ExecViewSizeSuggest__(ScritchInterface __scritchApi)
		throws NullPointerException
	{
		if (__scritchApi == null)
			throw new NullPointerException("NARG");
		
		this.scritchApi = __scritchApi;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/07/29
	 */
	@Override
	public void sizeSuggest(ScritchViewBracket __view,
		ScritchComponentBracket __subComponent, int __w, int __h)
		throws NullPointerException
	{
		if (__view == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote("sizeSuggest(%s, %s, %d, %d)",
			__view, __subComponent, __w, __h);
		
		// Just pass this through directly as we only care for this
		// single component being wrapped
		this.scritchApi.view().setArea(__view,
			__w, __h);
		this.scritchApi.container().setBounds(__view, __subComponent,
			0, 0, __w, __h);
	}
}
