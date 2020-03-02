// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.lib;

import cc.squirreljme.jvm.boot.io.BinaryBlob;

/**
 * This class manages the parsing of the standard individual constant pools
 * to the combined run-time and static dual-pool setup.
 *
 * @see ClassPoolParser
 * @since 2019/10/12
 */
public final class ClassDualPoolParser
{
	/** The class level static pool. */
	protected final AbstractPoolParser classpool;
	
	/** The run-time pool. */
	protected final AbstractPoolParser runtimepool;
	
	/**
	 * Initializes the dual class pool parser.
	 *
	 * @param __cl The static class pool.
	 * @param __rt The run-time class pool.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/12
	 */
	public ClassDualPoolParser(AbstractPoolParser __cl,
		AbstractPoolParser __rt)
		throws NullPointerException
	{
		if (__cl == null || __rt == null)
			throw new NullPointerException("NARG");
		
		this.classpool = __cl;
		this.runtimepool = __rt;
	}
	
	/**
	 * Returns the given pool count.
	 *
	 * @param __rt Use the run-time pool?
	 * @return The given pool count.
	 * @since 2019/12/14
	 */
	public final int count(boolean __rt)
	{
		return this.count(__rt, false);
	}
	
	/**
	 * Returns the given pool count.
	 *
	 * @param __rt Use the run-time pool?
	 * @param __ft Forward?
	 * @return The given pool count.
	 * @since 2019/12/14
	 */
	public final int count(boolean __rt, boolean __ft)
	{
		return this.pool(__rt).count(__ft);
	}
	
	/**
	 * Returns the decoded entry as any object.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/25
	 */
	public final Object entry(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.entry(__rt, __dx, false);
	}
	
	/**
	 * Returns the decoded entry as any object.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @param __ft Would the entry be forwarded through to the actual
	 * pool implementation?
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public final Object entry(boolean __rt, int __dx, boolean __ft)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		// The zero index is always null!
		if (__dx == 0)
			return null;
		
		// Need these pools
		AbstractPoolParser sp = this.classpool,
			rp = this.runtimepool,
			ap = (__rt ? rp : sp);
		
		// Get pool entry properties
		int etype = ap.entryType(__dx, __ft);
		short[] eparts = ap.entryParts(__dx, __ft);
		int enumparts = eparts.length;
		BinaryBlob eblob = ap.entryData(__dx, __ft);
		
		// Depends on the entry type
		switch (etype)
		{
				// String, skip hash and length copies
			case ClassPoolConstants.TYPE_STRING:
				return eblob.readUTF(4);
				
				// Name of class
			case ClassPoolConstants.TYPE_CLASSNAME:
				return new PoolClassName(
					(String)this.entry(false, eparts[0] & 0xFFFF, true),
					(PoolClassName)this.entry(false, eparts[1] & 0xFFFF,
						true));
					
				// Names of multiple classes
			case ClassPoolConstants.TYPE_CLASSNAMES:
				PoolClassName[] rvpn = new PoolClassName[enumparts];
				for (int i = 0; i < enumparts; i++)
					rvpn[i] = (PoolClassName)this.entry(false,
						eparts[i] & 0xFFFF, true);
				return rvpn;
				
				// Method descriptor
			case ClassPoolConstants.TYPE_METHOD_DESCRIPTOR:
				PoolClassName[] mdargs = new PoolClassName[enumparts - 3];
				for (int i = 0, n = enumparts - 3; i < n; i++)
					mdargs[i] = (PoolClassName)this.entry(false,
						eparts[i + 3] & 0xFFFF, true);
				return new PoolMethodDescriptor(
					(String)this.entry(false, eparts[0] & 0xFFFF, true),
					(PoolClassName)this.entry(false, eparts[2] & 0xFFFF, true),
					mdargs);
				
				// Class information pointer
			case ClassPoolConstants.TYPE_CLASS_INFO_POINTER:
				return new PoolClassInfoPointer(
					((PoolClassName)this.entry(false, eparts[0] & 0xFFFF,
						true)).toString());
					
				// Class pool pointer
			case ClassPoolConstants.TYPE_CLASS_POOL_POINTER:
				return new PoolClassPoolPointer(
					((PoolClassName)this.entry(false, eparts[0] & 0xFFFF,
						true)).toString());
				
				// Noted string
			case ClassPoolConstants.TYPE_NOTED_STRING:
				return new PoolNotedString(
					this.entryData(false, eparts[0] & 0xFFFF, true));
			
				// Unknown
			default:
				throw new todo.TODO("Pool " + etype);
		}
	}
	
	/**
	 * Returns the decoded entry as a class name.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/25
	 */
	public final PoolClassName entryAsClassName(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (PoolClassName)this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the decoded entry as class names.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/01
	 */
	public final PoolClassName[] entryAsClassNames(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (PoolClassName[])this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the decoded entry as a method descriptor.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/14
	 */
	public final PoolMethodDescriptor entryAsMethodDescriptor(boolean __rt,
		int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (PoolMethodDescriptor)this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the decoded entry as a noted string.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/14
	 */
	public final PoolNotedString entryAsNotedString(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (PoolNotedString)this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the decoded entry as a class info pointer.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/15
	 */
	public final PoolClassInfoPointer entryAsClassInfoPointer(boolean __rt,
		int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (PoolClassInfoPointer)this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the decoded entry as a class pool pointer.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/15
	 */
	public final PoolClassPoolPointer entryAsClassPoolPointer(boolean __rt,
		int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (PoolClassPoolPointer)this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the decoded entry as a string.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The value.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/11/24
	 */
	public final String entryAsString(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return (String)this.entry(__rt, __dx);
	}
	
	/**
	 * Returns the data to the given entry.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @param __ft Would the entry be forwarded through to the actual
	 * pool implementation?
	 * @return The data to the entry.
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/01
	 */
	public final BinaryBlob entryData(boolean __rt, int __dx, boolean __ft)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.pool(__rt).entryData(__dx, __ft);
	}
	
	/**
	 * Returns the type of the given entry.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @return The type of the entry
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/14
	 */
	public final int entryType(boolean __rt, int __dx)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.entryType(__rt, __dx, false);
	}
	
	/**
	 * Returns the type of the given entry.
	 *
	 * @param __rt Read from the run-time pool?
	 * @param __dx The index to read.
	 * @param __ft Would the entry be forwarded through to the actual
	 * pool implementation?
	 * @return The type of the entry
	 * @throws IndexOutOfBoundsException If the given entry is out of bounds.
	 * @throws InvalidClassFormatException If the pool is not valid.
	 * @since 2019/12/01
	 */
	public final int entryType(boolean __rt, int __dx, boolean __ft)
		throws IndexOutOfBoundsException, InvalidClassFormatException
	{
		return this.pool(__rt).entryType(__dx, __ft);
	}
	
	/**
	 * Returns the given pool.
	 *
	 * @param __rt Use the run-time pool?
	 * @return The given pool.
	 * @since 2019/12/14
	 */
	public final AbstractPoolParser pool(boolean __rt)
	{
		return (__rt ? this.runtimepool : this.classpool); 
	}
}

