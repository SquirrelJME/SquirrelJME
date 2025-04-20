// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

import cc.squirreljme.jvm.mle.brackets.BucketBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import javax.microedition.rms.RecordStore;

/**
 * This contains the session specifically for {@link RecordStore}'s metadata.
 *
 * @since 2025/04/20
 */
@SquirrelJMEVendorApi
public class RecordStoreSession
	extends RecordSession
{
	@SquirrelJMEVendorApi
	public static final String AUTHENTICATION =
		"authentication";
	
	@SquirrelJMEVendorApi
	public static final String OTHER_WRITE =
		"otherWrite";
	
	@SquirrelJMEVendorApi
	public static final String PASSWORD =
		"password";
	
	/**
	 * Initializes the session.
	 *
	 * @param __bucket The bucket to access.
	 * @param __fileName The file name of the data.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public RecordStoreSession(BucketBracket __bucket, String __fileName)
		throws NullPointerException
	{
		super(__bucket, __fileName);
	}
	
	/**
	 * Returns the integer value for a given key or a default value.
	 *
	 * @param __key The key to get the value of.
	 * @param __default The default value to return if it is not set.
	 * @return The resultant integer value.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public int getInteger(String __key, int __default)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __key The key to set.
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public void set(String __key, int __val)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the given key to the specified value.
	 *
	 * @param __key The key to set.
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/20
	 */
	@SquirrelJMEVendorApi
	public void set(String __key, String __val)
		throws NullPointerException
	{
		throw Debugging.todo();
	}
}
