// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This is used to build {@link TestResult} and is used by the code which
 * loads expected results and builds results.
 *
 * @since 2019/05/08
 */
public final class TestResultBuilder
{
	/** Secondary test values. */
	private final Map<String, String> _secondary =
		new SortedTreeMap<>();
	
	/** Returned value. */
	private volatile String _rvalue =
		"ResultWasNotSpecified";
	
	/** Thrown value. */
	private volatile String _tvalue =
		"ExceptionWasNotSpecified";
	
	/**
	 * Builds the actual test result.
	 *
	 * @return The test result.
	 * @since 2019/05/09
	 */
	public final TestResult build()
	{
		// Lock
		synchronized (this)
		{
			return new TestResult(this._rvalue, this._tvalue, this._secondary);
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
			secondary.put(DataSerialization.encodeKey(__key), __val);
			
			// Debug
			todo.DEBUG.note("%s=%s", __key, __val);
		}
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
	public final void putSecondaryValue(String __key, Object __val)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Encode the values
		this.putSecondaryValue(__key, DataSerialization.serialize(__val));
	}
	
	/**
	 * Sets the encoded return value.
	 *
	 * @param __val The value to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/09
	 */
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
	public final void setThrownValue(Object __val)
	{
		this.setThrownEncoded(DataSerialization.serialize(__val));
	}
}

