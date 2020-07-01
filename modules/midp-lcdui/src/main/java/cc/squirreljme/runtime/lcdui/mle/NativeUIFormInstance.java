// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;

/**
 * This represents a native {@link UIFormBracket}.
 *
 * @since 2020/07/01
 */
public final class NativeUIFormInstance
	implements UIFormInstance
{
	/** The native display. */
	protected final UIFormBracket form;
	
	/**
	 * Wraps the form instance.
	 * 
	 * @param __form The form to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public NativeUIFormInstance(UIFormBracket __form)
		throws NullPointerException
	{
		if (__form == null)
			throw new NullPointerException("NARG");
		
		this.form = __form;
	}
}
