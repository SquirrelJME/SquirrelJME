// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.UIDisplayBracket;

/**
 * This is a native instance of {@link UIDisplayBracket}.
 *
 * @since 2020/07/01
 */
public final class NativeUIDisplayInstance
	implements UIDisplayInstance
{
	/** The native display. */
	protected final UIDisplayBracket display;
	
	/**
	 * Wraps the native instance.
	 * 
	 * @param __display The display to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public NativeUIDisplayInstance(UIDisplayBracket __display)
		throws NullPointerException
	{
		if (__display == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
	}
}
