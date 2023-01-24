// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.util;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is a listener for any system events that occur.
 *
 * @since 2022/08/28
 */
public interface SystemEventListener
{
	/**
	 * Indicates that an event occurred on the system.
	 * 
	 * @param __option The same options as
	 * {@link System#getSystemState(String)}.
	 * @param __value The new value of the option.
	 * @since 2022/08/28
	 */
	@Api
	void systemEvent(String __option, String __value);
}
