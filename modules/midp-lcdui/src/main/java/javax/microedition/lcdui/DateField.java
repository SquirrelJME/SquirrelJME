// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.mle.DisplayWidget;
import cc.squirreljme.runtime.lcdui.mle.UIBackend;
import java.util.Date;
import java.util.TimeZone;

@Api
public class DateField
	extends Item
{
	@Api
	public static final int DATE =
		1;
	
	@Api
	public static final int DATE_TIME =
		3;
	
	@Api
	public static final int TIME =
		2;
	
	/** The timezone to use. */
	private final TimeZone _zone;
	
	/**
	 * Initializes the date field with the default timezone.
	 *
	 * @param __l The label to use.
	 * @param __m The mode used.
	 * @throws IllegalArgumentException If the mode is not valid.
	 * @since 2019/05/17
	 */
	@Api
	public DateField(String __l, int __m)
		throws IllegalArgumentException
	{
		this(__l, __m, null);
	}
	
	/**
	 * Initializes the date field with the default timezone.
	 *
	 * @param __l The label to use.
	 * @param __m The mode used.
	 * @param __z The time zone to use, {@code null} is the default.
	 * @throws IllegalArgumentException If the mode is not valid.
	 * @since 2019/05/17
	 */
	@Api
	public DateField(String __l, int __m, TimeZone __z)
		throws IllegalArgumentException
	{
		super(__l);
		
		/* {@squirreljme.error EB1g Invalid date field mode. (The mode)} */
		if (__m != DateField.DATE && __m != DateField.DATE_TIME && __m != DateField.TIME)
			throw new IllegalArgumentException("EB1g " + __m);
		
		this._zone = __z;
	}
	
	@Api
	public Date getDate()
	{
		throw Debugging.todo();
	}
	
	@Api
	public int getInputMode()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setDate(Date __a)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void setInputMode(int __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/01/14
	 */
	@Override
	__CommonState__ __stateInit(UIBackend __backend)
		throws NullPointerException
	{
		return new __DateFieldState__(__backend, this);
	}
	
	/**
	 * Date field state.
	 * 
	 * @since 2023/01/14
	 */
	static class __DateFieldState__
		extends Item.__ItemState__
	{
		/**
		 * Initializes the backend state.
		 *
		 * @param __backend The backend used.
		 * @param __self Self widget.
		 * @since 2023/01/14
		 */
		__DateFieldState__(UIBackend __backend, DisplayWidget __self)
		{
			super(__backend, __self);
		}
	}
}


