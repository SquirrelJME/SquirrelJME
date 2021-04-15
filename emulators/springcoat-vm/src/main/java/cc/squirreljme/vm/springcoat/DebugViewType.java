// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jdwp.JDWPCommandException;
import cc.squirreljme.jdwp.JDWPState;
import cc.squirreljme.jdwp.JDWPValue;
import cc.squirreljme.jdwp.views.JDWPViewType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;

/**
 * A viewer around class types.
 *
 * @since 2021/04/10
 */
public class DebugViewType
	implements JDWPViewType
{
	/** The state of the debugger. */
	protected final Reference<JDWPState> state;
	
	/**
	 * Initializes the type viewer.
	 * 
	 * @param __state The state.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/04/10
	 */
	public DebugViewType(Reference<JDWPState> __state)
	{
		if (__state == null)
			throw new NullPointerException("NARG");
		
		this.state = __state;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public Object componentType(Object __which)
	{
		return DebugViewType.__from(__which).componentType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int fieldFlags(Object __which, int __fieldId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String fieldName(Object __which, int __fieldId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String fieldSignature(Object __which, int __fieldDx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int[] fields(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public int flags(Object __which)
	{
		return DebugViewType.__from(__which).flags().toJavaBits();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public Object[] interfaceTypes(Object __which)
	{
		return DebugViewType.__from(__which).interfaceClasses();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/10
	 */
	@Override
	public boolean isValid(Object __which)
	{
		return (__which instanceof SpringClass);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public boolean isValidField(Object __which, int __fieldDx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public boolean isValidMethod(Object __which, int __methodDx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public byte[] methodByteCode(Object __which, int __methodId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int methodFlags(Object __which, int __methodId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int[] methodLineTable(Object __which, int __methodId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int methodLocationCount(Object __which, int __methodId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String methodName(Object __which, int __methodId)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String methodSignature(Object __which, int __methodDx)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int[] methods(Object __which)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public boolean readValue(Object __which, int __index, JDWPValue __out)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public String signature(Object __which)
	{
		return DebugViewType.__from(__which).name.field().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/15
	 */
	@Override
	public String sourceFile(Object __which)
	{
		return DebugViewType.__from(__which).file.sourceFile();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/12
	 */
	@Override
	public Object superType(Object __which)
	{
		return DebugViewType.__from(__which).superclass;
	}
	
	/**
	 * Gets the class from the given value.
	 * 
	 * @param __which Which to convert from.
	 * @return The spring class of the given type.
	 * @since 2021/04/15
	 */
	private static SpringClass __from(Object __which)
	{
		// Missing the class?
		if (__which == null)
			throw JDWPCommandException.tossInvalidClass(__which, null);
		
		// Return cast form of it
		try
		{
			return ((SpringClass)__which);
		}
		catch (ClassCastException e)
		{
			throw JDWPCommandException.tossInvalidClass(__which, e);
		}
	}
}
