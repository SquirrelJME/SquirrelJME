// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.midlet;

import cc.squirreljme.runtime.cldc.debug.Debugging;

public final class MIDletIdentity
{
	public static final String IDENTIFIED_THIRD_PARTY =
		"IdentifiedThirdParty";
	
	public static final String MANUFACTURER =
		"Manufacturer";
	
	public static final String OPERATOR =
		"Operator";
	
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
	
	public String getClient()
	{
		throw Debugging.todo();
	}
	
	public String getName()
	{
		throw Debugging.todo();
	}
	
	public String getVendor()
	{
		throw Debugging.todo();
	}
	
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

