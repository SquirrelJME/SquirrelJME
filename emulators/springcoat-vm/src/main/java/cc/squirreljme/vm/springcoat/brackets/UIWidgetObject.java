// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;
import cc.squirreljme.vm.springcoat.SpringMachine;

/**
 * Base class for widgets.
 *
 * @since 2020/09/20
 */
public abstract class UIWidgetObject
	extends AbstractGhostObject
{
	/**
	 * Initializes the base widget with the type it represents.
	 *
	 * @param __machine The machine used.
	 * @param __rep The type this represents.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/03
	 */
	public UIWidgetObject(SpringMachine __machine,
		Class<? extends UIWidgetBracket> __rep)
		throws NullPointerException
	{
		super(__machine, __rep);
	}
	
	/**
	 * Returns the used widget.
	 * 
	 * @return The widget.
	 * @since 2020/09/20
	 */
	public abstract UIWidgetBracket widget();
}
