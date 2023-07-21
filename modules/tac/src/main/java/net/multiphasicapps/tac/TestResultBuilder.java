// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is used to build {@link TestResult} and is used by the code which
 * loads expected results and builds results.
 *
 * @since 2019/05/08
 */
@SquirrelJMEVendorApi
public final class TestResultBuilder
{
	/** Secondary test values. */
	private final Map<String, String> _secondary =
		new SortedTreeMap<>();
	
	/** Returned value. */
	private volatile String _rvalue;
	
	/** Thrown value. */
	private volatile String _tvalue;
	
	/**
	 * Builds the actual test result.
	 *
	 * @return The test result.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final TestResult build()
	{
		// Lock
		synchronized (this)
		{
			// Fallback return value
			String rvalue = this._rvalue;
			if (rvalue == null)
				rvalue = "ResultWasNotSpecified";
				
			// Fallback throw value
			String tvalue = this._tvalue;
			if (tvalue == null)
				tvalue = "ExceptionWasNotSpecified";
			
			// Build result
			return new TestResult(rvalue, tvalue, this._secondary);
		}
	}
	
	/**
	 * Gets the return value.
	 *
	 * @return The return value.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final String getReturn()
	{
		synchronized (this)
		{
			return this._rvalue;
		}
	}
	
	/**
	 * Returns the secondary value.
	 *
	 * @param __key The key to get.
	 * @return The value of the secondary or {@code null} if it was not set.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final String getSecondary(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			return this._secondary.get(__key.toLowerCase());
		}
	}
	
	/**
	 * Gets the exception value.
	 *
	 * @return The exception value.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final String getThrown()
	{
		synchronized (this)
		{
			return this._tvalue;
		}
	}
	
	/**
	 * Stores a secondary test value which has already been encoded.
	 *
	 * @param __key The test key.
	 * @param __val The test value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void putSecondaryEncoded(String __key, String __val)
		throws NullPointerException
	{
		if (__key == null || __val == null)
			throw new NullPointerException("NARG");
		
		// Make it thread safe
		Map<String, String> secondary = this._secondary;
		synchronized (this)
		{
			// Use formatted values
			secondary.put(__key.toLowerCase(), __val);
		}
		
		// Debug
		Debugging.debugNote("SET %s=%s", __key, __val);
	}
	
	/**
	 * Adds a secondary test value which is a result of something, the value
	 * is always encoded to a string form.
	 *
	 * @param __key The test key.
	 * @param __val The test value.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void putSecondaryValue(String __key, Object __val)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Encode the values
		this.putSecondaryEncoded(__key, DataSerialization.serialize(__val));
	}
	
	/**
	 * Sets the encoded return value.
	 *
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void setReturnEncoded(String __val)
		throws NullPointerException
	{
		if (__val == null)
			throw new NullPointerException("NARG");
		
		// Thread safe!
		synchronized (this)
		{
			this._rvalue = __val;
		}
	}
	
	/**
	 * Sets the return value.
	 *
	 * @param __val The value to use.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void setReturnValue(Object __val)
	{
		this.setReturnEncoded(DataSerialization.serialize(__val));
	}
	
	/**
	 * Sets the encoded thrown value.
	 *
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void setThrownEncoded(String __val)
		throws NullPointerException
	{
		if (__val == null)
			throw new NullPointerException("NARG");
		
		// Thread safe!
		synchronized (this)
		{
			this._tvalue = __val;
		}
	}
	
	/**
	 * Sets the thrown value.
	 *
	 * @param __val The value to use.
	 * @since 2019/05/09
	 */
	@SquirrelJMEVendorApi
	public final void setThrownValue(Object __val)
	{
		this.setThrownEncoded(DataSerialization.serialize(__val));
	}
}

