// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.brackets;

import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.vm.springcoat.AbstractGhostObject;

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
	 * @param __rep The type this represents.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/01/03
	 */
	public UIWidgetObject(Class<? extends UIWidgetBracket> __rep)
		throws NullPointerException
	{
		super(__rep);
	}
	
	/**
	 * Returns the used widget.
	 * 
	 * @return The widget.
	 * @since 2020/09/20
	 */
	public abstract UIWidgetBracket widget();
}
