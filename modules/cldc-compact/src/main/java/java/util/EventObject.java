// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * The class which all event objects shall be derived from.
 *
 * @since 2019/01/20
 */
@Api
public class EventObject
{
	/** The source of the event. */
	private Object _source;
	
	/**
	 * Initializes the object.
	 *
	 * @param __s The source of the event.
	 * @throws IllegalArgumentException If no source was specified.
	 * @since 2019/01/20
	 */
	@Api
	@ImplementationNote("Throwing IllegalArgumentException is intended.")
	public EventObject(Object __s)
		throws IllegalArgumentException
	{
		if (__s == null)
			throw new IllegalArgumentException("NARG");
		
		this._source = __s;
	}
	
	/**
	 * Returns the source object.
	 *
	 * @return The source object.
	 * @since 2019/01/20
	 */
	@Api
	public Object getSource()
	{
		return this._source;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/01/20
	 */
	@Override
	public String toString()
	{
		// Matches what Java SE returns
		return this.getClass().getName() + "[source=" + this._source + "]";
	}
}
