// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.scritchui.callbacks;

import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchComponentBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchInputMethodType;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * Base interface for input listeners.
 *
 * @since 2024/03/07
 */
@SquirrelJMEVendorApi
public interface ScritchInputListener
	extends ScritchListener
{
	/**
	 * This is called when an event occurs.
	 *
	 * @param __component The component the event occurred in.
	 * @param __type The type of event this is.
	 * @param __time The time the event occurred.
	 * @param __a Value 1.
	 * @param __b Value 2.
	 * @param __c Value 3.
	 * @param __d Value 4.
	 * @param __e Value 5.
	 * @param __f Value 6.
	 * @param __g Value 7.
	 * @param __h Value 8.
	 * @param __i Value 9.
	 * @param __j Value 10.
	 * @param __k Value 11.
	 * @param __l Value 12.
	 * @since 2024/06/29
	 */
	@SquirrelJMEVendorApi
	@ScritchEventLoop
	void inputEvent(
		@NotNull ScritchComponentBracket __component,
		@MagicConstant(valuesFromClass = ScritchInputMethodType.class)
			int __type, long __time, int __a, int __b, int __c, int __d,
		int __e, int __f, int __g, int __h,
		int __i, int __j, int __k, int __l);
}
