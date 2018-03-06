// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.util.Objects;

/**
 * This represents the name of a class or array within the virtual machine.
 *
 * This class is immutable.
 *
 * @since 2017/09/27
 */
public class ClassName
	implements Comparable<ClassName>
{
	/** The binary name of the class. */
	protected final BinaryName binary;
	
	/** The field type of the class (for arrays). */
	protected final FieldDescriptor field;
	
	/**
	 * Initializes the class name.
	 *
	 * @param __n The input string.
	 * @throws InvalidClassFormatException If it is not a valid class name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/27
	 */
	public ClassName(String __n)
		throws InvalidClassFormatException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Is an array?
		if (__n.startsWith("["))
		{
			this.binary = null;
			this.field = new FieldDescriptor(__n);
		}
		
		// Not an array
		else
		{
			this.binary = new BinaryName(__n);
			this.field = null;
		}
	}
	
	/**
	 * Returns the binary name for this class.
	 *
	 * @return The class binary name or {@code null} if this is an array or
	 * primitive type.
	 * @since 2018/03/06
	 */
	public BinaryName binaryName()
	{
		return this.binary;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int compareTo(ClassName __o)
	{
		BinaryName ab = this.binary;
		if (ab != null)
		{
			BinaryName bb = __o.binary;
			if (bb == null)
				return -1;
			return ab.compareTo(bb);
		}
		
		FieldDescriptor af = this.field,
			bf = __o.field;
		if (bf == null)
			return 1;
		return af.compareTo(bf);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof ClassName))
			return false;
		
		ClassName o = (ClassName)__o;
		return Objects.equals(this.binary, o.binary) &&
			Objects.equals(this.field, o.field);
	}
	
	/**
	 * Returns the field descriptor for this class.
	 *
	 * @return The class field descriptor.
	 * @since 2017/10/10
	 */
	public FieldDescriptor field()
	{
		BinaryName binary = this.binary;
		if (binary != null)
			return new FieldDescriptor("L" + binary + ";");
		return this.field;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public int hashCode()
	{
		BinaryName ab = this.binary;
		return (ab != null ? ab.hashCode() : this.field.hashCode());
	}
	
	/**
	 * Returns the package that this class is within. Primitive types and
	 * arrays are not part of any package.
	 *
	 * @return The package or {@code null} if it is not in the package.
	 * @since 2017/10/09
	 */
	public BinaryName inPackage()
	{
		BinaryName b = this.binary;
		if (b != null)
			return b.inPackage();
		return null;
	}
	
	/**
	 * Does this class refer to an array type?
	 *
	 * @return If this is an array type.
	 * @since 2017/10/08
	 */
	public boolean isArray()
	{
		FieldDescriptor field = this.field;
		return field != null && field.isArray();
	}
	
	/**
	 * Is the other class in the same package as this class?
	 *
	 * @param __b The other class to check.
	 * @return If the other class is in this same package.
	 * @since 2017/10/11
	 */
	public boolean isInSamePackage(ClassName __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		return Objects.equals(inPackage(), __b.inPackage());
	}
	
	/**
	 * Does this class refer to a primitive type?
	 *
	 * @return If this is a primitive type.
	 * @since 2017/10/08
	 */
	public boolean isPrimitive()
	{
		FieldDescriptor field = this.field;
		return field != null && field.isPrimitive();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/27
	 */
	@Override
	public String toString()
	{
		BinaryName binary = this.binary;
		if (binary != null)
			return binary.toString();
		return field.toString();
	}
}

