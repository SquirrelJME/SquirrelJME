// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.constants.UIItemPosition;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Represents a single form within a framebuffer display.
 *
 * @since 2022/07/23
 */
public final class FBUIForm
	implements UIFormBracket
{
	/** The display this form is attached to. */
	volatile FBDisplay _display;
	
	/**
	 * Sets the position of the given item in the form.
	 * 
	 * @param __item The item to set the position from.
	 * @param __pos The position to set.
	 * @throws MLECallError On null arguments or the position is not valid.
	 * @since 2022/07/26
	 */
	public void itemPosition(FBUIItem __item, int __pos)
	{
		if (__item == null)
			throw new MLECallError("NARG");
		if (__pos < UIItemPosition.MIN_VALUE)
			throw new MLECallError("IOOB");
		
		throw Debugging.todo();
	}
}
