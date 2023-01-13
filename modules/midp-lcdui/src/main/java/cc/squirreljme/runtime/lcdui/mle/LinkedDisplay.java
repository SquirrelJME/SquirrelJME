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
import cc.squirreljme.jvm.mle.constants.UIMetricType;

/**
 * Represents a display which is linked to a backend.
 *
 * @since 2023/01/12
 */
public final class LinkedDisplay
{
	/** The associated display. */
	public final UIDisplayBracket display;
	
	/** The associated backend. */
	public final UIBackend backend;
	
	/**
	 * Initializes the linked display.
	 * 
	 * @param __display The display used.
	 * @param __backend The backend used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/01/12
	 */
	public LinkedDisplay(UIDisplayBracket __display, UIBackend __backend)
		throws NullPointerException
	{
		if (__display == null || __backend == null)
			throw new NullPointerException("NARG");
		
		this.display = __display;
		this.backend = __backend;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/12
	 */
	@Override
	public int hashCode()
	{
		return this.backend.getClass().hashCode() ^
			this.backend.metric(this.display, UIMetricType.DISPLAY_ID);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (__o == this)
			return true;
		else if (!(__o instanceof LinkedDisplay))
			return false;
		
		LinkedDisplay o = (LinkedDisplay)__o;
		return this.backend.getClass() == o.backend.getClass() &&
			this.backend.equals(this.display, o.display);
	}
}
