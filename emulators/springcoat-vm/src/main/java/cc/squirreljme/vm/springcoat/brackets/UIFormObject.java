// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * This wraps a native {@link UIFormBracket}.
 *
 * @since 2020/07/01
 */
public final class UIFormObject
	extends UIWidgetObject
{
	/** The form to wrap. */
	public final UIFormBracket form;
	
	/**
	 * Initializes the form object.
	 * 
	 * @param __machine The machine used.
	 * @param __form The form to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/07/01
	 */
	public UIFormObject(SpringMachine __machine, UIFormBracket __form)
		throws NullPointerException
	{
		super(__machine, UIFormBracket.class);
		
		if (__form == null)
			throw new NullPointerException("NARG");
		
		this.form = __form;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/20
	 */
	@Override
	public UIFormBracket widget()
	{
		return this.form;
	}
}
