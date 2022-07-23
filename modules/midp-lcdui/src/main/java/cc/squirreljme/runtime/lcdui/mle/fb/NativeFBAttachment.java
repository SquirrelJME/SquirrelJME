// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is an attachment which interacts with the native framebuffer of the
 * system accordingly.
 *
 * @since 2020/10/09
 */
public class NativeFBAttachment
	extends FBUIBackend
{	
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
