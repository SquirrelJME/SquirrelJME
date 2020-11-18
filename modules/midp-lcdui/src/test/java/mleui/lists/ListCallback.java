// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package mleui.lists;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Callback for list events.
 *
 * @since 2020/10/29
 */
public class ListCallback
	implements UIFormCallback
{
	/** Painted items. */
	volatile int _painted =
		-1;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void eventKey(UIFormBracket __form, UIItemBracket __item,
		int __event, int __keyCode, int __modifiers)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void eventMouse(UIFormBracket __form, UIItemBracket __item,
		int __event, int __button, int __x, int __y, int __modifiers)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void exitRequest(UIFormBracket __form)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void paint(UIFormBracket __form, UIItemBracket __item, int __pf,
		int __bw, int __bh, Object __buf, int __offset, int[] __pal, int __sx,
		int __sy, int __sw, int __sh, int __special)
	{
		synchronized (this)
		{
			int hob = Integer.highestOneBit(__special);
			if (hob >= 1 && hob < Integer.MAX_VALUE)
				this._painted |= hob;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __intProp, int __sub, int __old, int __new)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public void propertyChange(UIFormBracket __form, UIItemBracket __item,
		int __strProp, int __sub, String __old, String __new)
	{
	}
}
