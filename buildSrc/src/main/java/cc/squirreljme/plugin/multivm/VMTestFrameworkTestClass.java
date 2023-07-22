// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	
	/** The primary sub-variant, if any. */
	public String primarySubVariant;
	
	/** The secondary sub-variant, if any. */
	public String secondarySubVariant;
	
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
		int firstAt = __testName.indexOf('@');
		int lastDot = __testName.lastIndexOf('.',
			(firstAt < 0 ? __testName.length() : firstAt));
		
		// Determine the actual class name and variant
		this.normal = __testName;
		this.className = (firstAt >= 0 && firstAt > lastDot ?
			__testName.substring(0, firstAt) : __testName);
		this.variant = (firstAt >= 0 && firstAt > lastDot ?
			__testName.substring(firstAt + 1) : null);
		
		// If there is no variant, there is no possibility of of a sub-variant
		if (this.variant == null)
		{
			this.primarySubVariant = null;
			this.secondarySubVariant = null;
		}
		
		// Otherwise the sub-variants will be the left and right side of the
		// variant's at sign.
		else
		{
			int nextAt = this.variant.indexOf('@');
			
			// Only a primary sub-variant
			if (nextAt < 0)
			{
				this.primarySubVariant = this.variant;
				this.secondarySubVariant = null;
			}
			
			// Has both sub-variants
			else
			{
				this.primarySubVariant = this.variant.substring(0, nextAt);
				this.secondarySubVariant =
					this.variant.substring(nextAt + 1);
			}
		}
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
	
	/**
	 * {@inheritDoc}
	 * @since 2022/12/23
	 */
	@Override
	public String toString()
	{
		return String.format("Test[%s, %s, %s (%s, %s)]",
			this.normal, this.className, this.variant,
			this.primarySubVariant, this.secondarySubVariant);
	}
}
