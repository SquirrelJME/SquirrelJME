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
import cc.squirreljme.runtime.lcdui.mle.LinkedDisplay;

/**
 * This is an attachment which interacts with the native framebuffer of the
 * system accordingly.
 *
 * @since 2020/10/09
 */
public class NativeFBAttachment
	implements FBAttachment
{
	/**
	 * Initializes the framebuffer attachment to a native display.
	 * 
	 * @param __nativeLink The link to interact with. 
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/12
	 */
	public NativeFBAttachment(LinkedDisplay __nativeLink)
		throws NullPointerException
	{
		if (__nativeLink == null)
			throw new NullPointerException("NARG");
			
		throw Debugging.todo();
	}
}
