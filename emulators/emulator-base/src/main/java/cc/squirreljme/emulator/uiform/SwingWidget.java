// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.uiform;

import cc.squirreljme.jvm.mle.brackets.UIWidgetBracket;
import cc.squirreljme.jvm.mle.constants.UIWidgetProperty;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * This is a widget.
 *
 * @since 2020/09/21
 */
public interface SwingWidget
	extends UIWidgetBracket
{
	/**
	 * Sets the given property.
	 * 
	 * @param __id The {@link UIWidgetProperty} to set.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the property is not valid.
	 * @since 2020/09/13
	 */
	void property(int __id, int __newValue)
		throws MLECallError;
	
	/**
	 * Sets the given property.
	 * 
	 * @param __id The {@link UIWidgetProperty} to set.
	 * @param __newValue The new value to set.
	 * @throws MLECallError If the property is not valid.
	 * @since 2020/09/13
	 */
	void property(int __id, String __newValue);
	
	/**
	 * Returns the given property.
	 * 
	 * @param __intProp The {@link UIWidgetProperty} to get.
	 * @return The property.
	 * @throws MLECallError If not a valid property.
	 * @since 2020/09/21
	 */
	int propertyInt(int __intProp)
		throws MLECallError;
	
	/**
	 * Returns the given property.
	 * 
	 * @param __strProp The {@link UIWidgetProperty} to get.
	 * @return The property.
	 * @throws MLECallError If not a valid property.
	 * @since 2020/09/21
	 */
	String propertyStr(int __strProp)
		throws MLECallError;
}
