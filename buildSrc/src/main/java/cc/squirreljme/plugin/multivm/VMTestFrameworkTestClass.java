// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.Objects;

/**
 * Represents a test class.
 *
 * @since 2022/09/14
 */
public final class VMTestFrameworkTestClass
	implements Comparable<VMTestFrameworkTestClass>
{
	/** The class name this is in. */
	public String className;
	
	/** The variant. */
	public String variant;
	
	/** The normal test. */
	public String normal;
	
	/**
	 * Initializes the class name reference.
	 * 
	 * @param __testName The name of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/14
	 */
	public VMTestFrameworkTestClass(String __testName)
		throws NullPointerException
	{
		if (__testName == null)
			throw new NullPointerException("NARG");
		
		// Split off from the test and the class
		int lastAt = __testName.lastIndexOf('@');
		int lastSlash = __testName.lastIndexOf('.');
		
		// Determine the actual class name and variant
		this.normal = __testName;
		this.className = (lastAt >= 0 && lastAt > lastSlash ?
			__testName.substring(0, lastAt) : __testName);
		this.variant = (lastAt >= 0 && lastAt > lastSlash ?
			__testName.substring(lastAt + 1) : null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/16
	 */
	@Override
	public int compareTo(VMTestFrameworkTestClass __b)
	{
		// By class
		int rv = this.className.compareTo(__b.className);
		if (rv != 0)
			return rv;
		
		// By normal
		rv = this.normal.compareTo(__b.normal);
		if (rv != 0)
			return rv;
		
		// By variant
		String av = this.variant;
		String bv = __b.variant;
		if ((av == null) != (bv == null))
			return (av == null ? -1 : 1);
		return (av == null ? 0 : av.compareTo(bv));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/16
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		if (!(__o instanceof VMTestFrameworkTestClass))
			return false;
		
		VMTestFrameworkTestClass other = (VMTestFrameworkTestClass)__o;
		return Objects.equals(this.normal, other.normal) &&
			Objects.equals(this.className, other.className) &&
			Objects.equals(this.variant, other.variant);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/16
	 */
	@Override
	public int hashCode()
	{
		return Objects.hash(this.normal, this.className, this.variant);
	}
}
