// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.exceptions.MLECallError;
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
	
	/** The API for calling ScritchUI functions. */
	@SquirrelJMEVendorApi
	protected final ScritchInterface scritchApi;
	
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
		this.scritchApi = __scritchApi;
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
		try
		{
			this.scritchApi.choice().choiceDelete(this._widget, __atIndex);
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
	
	/**
	 * Deletes all items within the choice.
	 *
	 * @since 2024/07/24
	 */
	@SquirrelJMEVendorApi
	public void deleteAll()
	{
		try
		{
			this.scritchApi.choice().choiceDeleteAll(this._widget);
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
	
	/**
	 * Returns the first selected index in this choice.
	 *
	 * @return The first selected index, returns {@code -1} if there is none.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	public int getSelectedIndex()
	{
		try
		{
			return this.scritchApi.choice().choiceGetSelectedIndex(this._widget);
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
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
		
		// Perform upsert operation
		try
		{
			// Setup executor
			__ExecChoiceUpsert__ upsert = new __ExecChoiceUpsert__(
				this.scritchApi, this._widget, true, __atIndex,
				__str, __img);
			
			// Wait for it to run and finish
			this.scritchApi.eventLoop().loopExecuteWait(upsert);
			
			// Return whatever result
			if (upsert._error != null)
				throw upsert._error;
			return upsert._result;
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
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
	@SquirrelJMEVendorApi
	public void set(int __atIndex, String __str, Image __img)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__str == null)
			throw new NullPointerException("NARG");
		
		if (__atIndex < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Perform upsert operation
		try
		{
			// Setup executor and storage
			__ExecChoiceUpsert__ upsert = new __ExecChoiceUpsert__(
				this.scritchApi, this._widget, false, __atIndex,
				__str, __img);
			
			// Wait for it to run and finish
			this.scritchApi.eventLoop().loopExecuteWait(upsert);
			
			// Did this fail?
			if (upsert._error != null)
				throw upsert._error;
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
	
	/**
	 * Sets whether the given item is enabled.
	 *
	 * @param __atIndex The index to set.
	 * @param __enabled If the item should be enabled.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * list bounds.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	public void setEnabled(int __atIndex, boolean __enabled)
		throws IndexOutOfBoundsException
	{
		if (__atIndex < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		try
		{
			this.scritchApi.choice().choiceSetEnabled(this._widget, __atIndex,
				__enabled);
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
	
	/**
	 * Sets whether the given item is selected.
	 *
	 * @param __atIndex The index to set.
	 * @param __selected If the item should be selected.
	 * @throws IndexOutOfBoundsException If the index is not within the
	 * list bounds.
	 * @since 2024/07/25
	 */
	@SquirrelJMEVendorApi
	public void setSelected(int __atIndex, boolean __selected)
		throws IndexOutOfBoundsException
	{
		if (__atIndex < 0)
			throw new IndexOutOfBoundsException("IOOB");
		
		try
		{
			this.scritchApi.choice().choiceSetSelected(this._widget, __atIndex,
				__selected);
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
	
	/**
	 * Sets all selected flags in the list.
	 *
	 * @param __flags The flags to set.
	 * @throws IllegalArgumentException If the flag set is smaller than the
	 * number of choice items.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	public void setSelectedFlags(boolean[] __flags)
		throws IllegalArgumentException, NullPointerException
	{
		if (__flags == null)
			throw new NullPointerException("NARG");
		
		// Perform flagging operation
		try
		{
			// Setup executor and storage
			__ExecChoiceSelectedFlags__ select =
				new __ExecChoiceSelectedFlags__(this.scritchApi, this._widget,
					__flags, this.type);
			
			// Wait for it to run and finish
			this.scritchApi.eventLoop().loopExecuteWait(select);
			
			// Did this fail?
			Throwable error = select._error;
			if (select._error != null)
			{
				if (error instanceof Error)
					throw (Error)error;
				else if (error instanceof RuntimeException)
					throw (RuntimeException)error;
				throw new RuntimeException(error);
			}
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
	
	/**
	 * Returns the number of choices available.
	 *
	 * @return The number of available choices.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	public int size()
	{
		try
		{
			return this.scritchApi.choice().choiceLength(this._widget);
		}
		catch (MLECallError __e)
		{
			throw __e.throwDistinct();
		}
	}
}
