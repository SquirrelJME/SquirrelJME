// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

@Api
public final class MIDletIdentity
{
	@Api
	public static final String IDENTIFIED_THIRD_PARTY =
		"IdentifiedThirdParty";
	
	@Api
	public static final String MANUFACTURER =
		"Manufacturer";
	
	@Api
	public static final String OPERATOR =
		"Operator";
	
	@Api
	public static final String UNIDENTIFIED_THIRD_PARTY =
		"UnidentifiedThirdParty";
	
	/**
	 * Internally initialized.
	 *
	 * @since 2016/08/30
	 */
	MIDletIdentity()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getClient()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getName()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getVendor()
	{
		throw Debugging.todo();
	}
	
	@Api
	public String getVersion()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/08/30
	 */
	@Override
	public String toString()
	{
		throw Debugging.todo();
	}
}

