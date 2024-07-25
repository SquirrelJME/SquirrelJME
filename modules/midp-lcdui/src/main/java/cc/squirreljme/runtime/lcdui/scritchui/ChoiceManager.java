// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchChoiceBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Image;
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
	@SquirrelJMEVendorApi
	public final int type;
	
	/** The widget to manage. */
	@SquirrelJMEVendorApi
	final ScritchChoiceBracket _widget;
	
	/**
	 * Initializes the choice manager.
	 *
	 * @param __type The type of choice to use.
	 * @param __scritchApi The ScritchUI API interface.
	 * @param __widget The ScritchUI choice widget to access the choices for.
	 * @throws IllegalArgumentException If the type is not valid.
	 * @since 2024/07/24
	 */
	@SquirrelJMEVendorApi
	public ChoiceManager(int __type, ScritchInterface __scritchApi,
		ScritchChoiceBracket __widget)
		throws IllegalArgumentException, NullPointerException
	{
		if (__scritchApi == null || __widget == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB2k Invalid list type. (The list type)} */
		if (__type != Choice.IMPLICIT && __type != Choice.EXCLUSIVE &&
			__type != Choice.MULTIPLE)
			throw new IllegalArgumentException("EB2k " + __type);
		
		this.type = __type;
		this._widget = __widget;
	}
	
	/**
	 * Deletes the specified item in the choice.
	 *
	 * @param __atIndex The index to delete at.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @since 2024/07/24
	 */
	@SquirrelJMEVendorApi
	public void delete(int __atIndex)
		throws IndexOutOfBoundsException
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Deletes all items within the choice.
	 *
	 * @since 2024/07/24
	 */
	@SquirrelJMEVendorApi
	public void deleteAll()
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Inserts the given item at the given index.
	 *
	 * @param __atIndex The index to add at, {@link Integer#MAX_VALUE} is an
	 * alias for the end index.
	 * @param __str The string to insert.
	 * @param __img The image to insert.
	 * @return The index where it was inserted.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException If no string was specified.
	 * @since 2024/07/24
	 */
	@SquirrelJMEVendorApi
	public int insert(int __atIndex, String __str, Image __img)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		if (__atIndex < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Sets the specified item at the given index.
	 *
	 * @param __atIndex The index to set at.
	 * @param __str The string to set.
	 * @param __img The image to set.
	 * @throws IndexOutOfBoundsException If the index is not valid.
	 * @throws NullPointerException If no string was specified.
	 * @since 2024/07/24
	 */
	public void set(int __atIndex, String __str, Image __img)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		if (__atIndex < 0)
			throw new IndexOutOfBoundsException("IOOB");
			
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
}
