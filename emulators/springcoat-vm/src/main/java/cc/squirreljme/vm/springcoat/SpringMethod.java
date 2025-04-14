// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.host.trips.JDWPTripBreakpoint;
import cc.squirreljme.runtime.cldc.util.SortedTreeMap;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.Method;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This contains and stores the definition of a single method.
 *
 * @since 2018/07/22
 */
public final class SpringMethod
	implements SpringMember
{
	/** The class this technically belongs to. */
	protected final ClassName inclass;
	
	/** The backing method and its information. */
	protected final Method method;
	
	/** The file this method is in. */
	protected final String infile;
	
	/** Breakpoints for the method. */
	private volatile Map<Integer, JDWPTripBreakpoint> _breakpoints;
	
	/** The line table (cached). */
	volatile int[] _lineTable;
	
	/** The method index. */
	protected final int methodIndex;
	
	/**
	 * Initializes the method representation.
	 *
	 * @param __ic The class this belongs to.
	 * @param __m The method to wrap.
	 * @param __if The file this is in.
	 * @param __dx The method index.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	SpringMethod(ClassName __ic, Method __m, String __if, int __dx)
		throws NullPointerException
	{
		if (__ic == null || __m == null)
			throw new NullPointerException("NARG");
		
		this.inclass = __ic;
		this.method = __m;
		this.infile = __if;
		this.methodIndex = __dx;
	}
	
	/**
	 * Returns the byte code of the method.
	 *
	 * @return The method byte code.
	 * @since 2018/09/03
	 */
	public final ByteCode byteCode()
	{
		return this.method.byteCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final MethodFlags flags()
	{
		return this.method.flags();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/03
	 */
	@Override
	public final ClassName inClass()
	{
		return this.inclass;
	}
	
	/**
	 * Returns the file this method is in.
	 *
	 * @return The file this method is in, may be {@code null}.
	 * @since 2018/09/20
	 */
	public final String inFile()
	{
		return this.infile;
	}
	
	/**
	 * Returns whether this method is abstract.
	 *
	 * @return Whether this method is abstract.
	 * @since 2018/09/03
	 */
	public final boolean isAbstract()
	{
		return this.method.flags().isAbstract();
	}
	
	/**
	 * Returns whether this is a constructor or not.
	 *
	 * @return Whether this is a constructor or not.
	 * @since 2018/09/03
	 */
	public final boolean isInstanceInitializer()
	{
		return this.method.isInstanceInitializer();
	}
	
	/**
	 * Returns if this method is static.
	 *
	 * @return {@code true} if the method is static.
	 * @since 2018/09/03
	 */
	public final boolean isStatic()
	{
		return this.method.flags().isStatic();
	}
	
	/**
	 * Returns whether this is a static initializer or not.
	 *
	 * @return Whether this is a static initializer or not.
	 * @since 2018/09/03
	 */
	public final boolean isStaticInitializer()
	{
		return this.method.isStaticInitializer();
	}
	
	/**
	 * Returns the name of this method.
	 *
	 * @return The name of this method.
	 * @since 2018/09/09
	 */
	public final MethodName name()
	{
		return this.method.name();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/09
	 */
	@Override
	public final MethodNameAndType nameAndType()
	{
		return this.method.nameAndType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/06
	 */
	@Override
	public final String toString()
	{
		return this.inclass + "::" + this.method.nameAndType().toString();
	}
	
	/**
	 * Returns the breakpoint mapping, potentially creating it.
	 * 
	 * @param __create Should the breakpoint map be created?
	 * @return The breakpoint map, will be {@code null} if it does not exist.
	 * @since 2021/04/25
	 */
	Map<Integer, JDWPTripBreakpoint> __breakpoints(boolean __create)
	{
		// If it exists, already do nothing else
		// If we are not going to create it, then stop here
		Map<Integer, JDWPTripBreakpoint> breakpoints = this._breakpoints;
		if (breakpoints != null || !__create)
			return breakpoints;
		
		// Lock because we want to use the same map always
		synchronized (this)
		{
			// If it exists between this point now or we do not need to
			// create it, then stop
			breakpoints = this._breakpoints;
			if (breakpoints != null)
				return breakpoints;
			
			// Create new one
			breakpoints = new SortedTreeMap<>();
			this._breakpoints = breakpoints;
		}
		
		return breakpoints;
	}
}

