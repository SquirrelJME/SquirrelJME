// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.lang.ref.Reference;

/**
 * Fake base class.
 *
 * @since 2023/10/15
 */
abstract class __FakeBase__
{
	/** The reference to the test. */
	final Reference<TestFakeBackend> _ref;
	
	/** The name used. */
	protected final String name;
	
	/**
	 * Initializes the fake base.
	 *
	 * @param __name The name used.
	 * @param __ref The reference to point to.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/10/15
	 */
	public __FakeBase__(String __name, Reference<TestFakeBackend> __ref)
		throws NullPointerException
	{
		if (__name == null || __ref == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
		this._ref = __ref;
	}
	
	/**
	 * Returns the test reference.
	 *
	 * @return The fake backend.
	 * @since 2023/10/15
	 */
	final TestFakeBackend __ref()
	{
		TestFakeBackend rv = this._ref.get();
		if (rv == null)
			throw new IllegalStateException("GCGC");
		return rv;
	}
	
	/**
	 * Emits a secondary test value.
	 *
	 * @param __key The key to use.
	 * @param __value The value to set as.
	 * @since 2023/10/15
	 */
	final void __secondary(String __key, Object __value)
	{
		String key = String.format("%s-%s", this.name,
			__FakeBase__.__shorten(__key));
		this.__ref().secondary(key, __value);
	}
	
	/**
	 * Emits a secondary test value.
	 *
	 * @param __glob The glob to reference.
	 * @param __key The key to use.
	 * @param __value The value to set as.
	 * @since 2023/10/15
	 */
	final void __secondary(LinkGlob __glob, String __key, Object __value)
	{
		this.__secondary(
			String.format("%s-%s",
				((__FakeLinkGlob__)__glob).name,
				__FakeBase__.__shorten(__key)),
			__value);
	}
	
	/**
	 * Shortens the input key.
	 *
	 * @param __key The key to shorten.
	 * @return The resultant shortened key.
	 * @since 2023/10/15
	 */
	private static String __shorten(String __key)
	{
		if (__key.length() > 20)
		{
			int hash = __key.hashCode();
			return String.format("%c%d%c%s%s",
				__key.charAt(0),
				__key.length(),
				__key.charAt(__key.length() - 1),
				(hash < 0 ? "u" : ""),
				Integer.toString(hash & 0x7FFF_FFFF,
					Character.MAX_RADIX));
		}
		
		return __key;
	}
}
