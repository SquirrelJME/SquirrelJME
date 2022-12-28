// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.headless;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.fb.FBDisplay;
import cc.squirreljme.runtime.lcdui.mle.fb.FBUIBackend;

/**
 * This is an attachment for headless framebuffer setups.
 *
 * @since 2020/10/09
 */
public class HeadlessAttachment
	extends FBUIBackend
{
	public HeadlessAttachment(int __pf, int __w, int __h)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/23
	 */
	@Override
	protected FBDisplay[] queryDisplays()
	{
		throw Debugging.todo();
	}
}
