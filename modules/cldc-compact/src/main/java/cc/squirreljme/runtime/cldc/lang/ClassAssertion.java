// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.lang;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class assertion management.
 *
 * @since 2025/06/19
 */
@SquirrelJMEVendorApi
public class ClassAssertion
{
	/** This is the prefix that is used for assertion checks. */
	public static final String ASSERTION_PREFIX =
		"cc.squirreljme.runtime.noassert.";
	
	/** Has the assertion status been checked already? */
	private static final Set<Class<?>> _checkedAssert =
		new HashSet<>();
	
	/** Is this class being asserted? */
	private static final Map<Class<?>, Boolean> _useAssert =
		new HashMap<>();
	
	/**
	 * Returns whether assertions should be enabled in the specified
	 * class, this is used internally by the virtual machine to determine if
	 * assertions should fail or not.
	 *
	 * In SquirrelJME, this defaults to returning {@code true}. To disable
	 * assertions for a class or an entire package then the following system
	 * property may be specified to disable them:
	 * {@code cc.squirreljme.noassert.(package)(.class)=true}.
	 *
	 * @param __class The class to refer to.
	 * @return In SquirrelJME this returns by default {@code true}, otherwise
	 * this may return {@code false} if they are disabled for a class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/13
	 */
	@SquirrelJMEVendorApi
	public static boolean desiredAssertionStatus(Class<?> __class)
		throws NullPointerException
	{
		Set<Class<?>> checkedAssert = ClassAssertion._checkedAssert;
		Map<Class<?>, Boolean> useAssert = ClassAssertion._useAssert;
		synchronized (ClassAssertion.class)
		{
			// If assertions have been checked, they do not have to be rechecked
			if (checkedAssert.contains(__class))
				return useAssert.get(__class);
			
			// Otherwise check it
			return ClassAssertion.__checkAssertionStatus(__class);
		}
	}
	
	/**
	 * This checks whether assertions should be **disabled** for this class (or
	 * for the entire package).
	 *
	 * @param __class The class to refer to.
	 * @return The assertions status to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/09
	 */
	private static boolean __checkAssertionStatus(Class<?> __class)
		throws NullPointerException
	{
		Set<Class<?>> checkedAssert = ClassAssertion._checkedAssert;
		Map<Class<?>, Boolean> useAssert = ClassAssertion._useAssert;
		synchronized (ClassAssertion.class)
		{
			// Default to true
			boolean rv = true;
			
			// Determine class name
			String cn = __class.getName();
			String prop = ClassAssertion.ASSERTION_PREFIX + cn;
			
			// Disabled for this class?
			if (Boolean.getBoolean(prop))
				rv = false;
				
			// Disabled for this package?
			else
			{
				// Find last dot, if there is none then this is just the default
				// package so never bother checking the package
				int ld = cn.lastIndexOf('.');
				if (ld > 0 && Boolean.getBoolean(
					prop.substring(0, prop.length() - (cn.length() - ld))))
					rv = false;
			}
			
			// Set as marked
			checkedAssert.add(__class);
			useAssert.put(__class, rv);
			return rv;
		}
	}
}
