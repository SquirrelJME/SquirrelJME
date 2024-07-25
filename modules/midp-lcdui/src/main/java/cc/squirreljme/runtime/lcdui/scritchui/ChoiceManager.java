// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.List;

/**
 * Manages choices for {@link Choice} implementations such as {@link List}
 * and {@link ChoiceGroup}.
 *
 * @since 2024/03/27
 */
@SquirrelJMEVendorApi
public final class ChoiceManager
{
	/** The type of list this is. */
	public final int type;
	
	/**
	 * Initializes the choice manager.
	 *
	 * @param __type The type of choice to use.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @since 2024/07/24
	 */
	public ChoiceManager(int __type)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB2k Invalid list type. (The list type)} */
		if (__type != Choice.IMPLICIT && __type != Choice.EXCLUSIVE &&
			__type != Choice.MULTIPLE)
			throw new IllegalArgumentException("EB2k " + __type);
		
		this.type = __type;
	}
}
