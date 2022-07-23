// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle.fb;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;

/**
 * A display for {@link FBUIBackend}.
 *
 * @since 2022/07/23
 */
public class UIFormFBDisplay
	extends FBDisplay
{
	/** The display bracket to use. */
	protected volatile UIDisplayBracket _display;
	
	/**
	 * Initializes the form display.
	 * 
	 * @param __uiDisplay The UI display to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/23
	 */
	public UIFormFBDisplay(UIDisplayBracket __uiDisplay)
		throws NullPointerException
	{
		if (__uiDisplay == null)
			throw new NullPointerException("NARG");
		
		this._display = __uiDisplay;
	}
}
