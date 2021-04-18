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
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchFieldException;
import cc.squirreljme.vm.springcoat.exceptions.SpringNoSuchMethodException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.lang.ref.Reference;
import net.multiphasicapps.classfile.ByteCode;

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
		return DebugViewType.__class(__which).componentType();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int fieldFlags(Object __which, int __fieldDx)
	{
		return DebugViewType.__field(__which, __fieldDx).flags().toJavaBits();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String fieldName(Object __which, int __fieldDx)
	{
		return DebugViewType.__field(__which, __fieldDx).nameAndType()
			.name().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String fieldSignature(Object __which, int __fieldDx)
	{
		return DebugViewType.__field(__which, __fieldDx).nameAndType()
			.type().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int[] fields(Object __which)
	{
		SpringField[] fields = DebugViewType.__class(__which).fieldLookup();
		
		int n = fields.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = fields[i].index;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public int flags(Object __which)
	{
		return DebugViewType.__class(__which).flags().toJavaBits();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public Object[] interfaceTypes(Object __which)
	{
		return DebugViewType.__class(__which).interfaceClasses();
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
		try
		{
			DebugViewType.__field(__which, __fieldDx);
			return true;
		}
		catch (SpringNoSuchFieldException |JDWPCommandException ignored)
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public boolean isValidMethod(Object __which, int __methodDx)
	{
		try
		{
			DebugViewType.__method(__which, __methodDx);
			return true;
		}
		catch (SpringNoSuchMethodException|JDWPCommandException ignored)
		{
			return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public byte[] methodByteCode(Object __which, int __methodDx)
	{
		// If there is no method byte code then ignore
		ByteCode byteCode = DebugViewType.__method(__which, __methodDx).method.byteCode();
		if (byteCode == null)
			return null;
		
		return byteCode.rawByteCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int methodFlags(Object __which, int __methodDx)
	{
		return DebugViewType.__method(__which, __methodDx)
			.flags().toJavaBits();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int[] methodLineTable(Object __which, int __methodDx)
	{
		SpringMethod springMethod = DebugViewType.__method(__which,
			__methodDx);
		
		// Pre-cached?
		int[] lineTable = springMethod._lineTable;
		if (lineTable != null)
			return lineTable.clone();
		
		// If there is no method byte code then ignore
		ByteCode byteCode = springMethod.method.byteCode();
		if (byteCode == null)
			return null;
		
		// Otherwise map each unique address to a line number
		int[] addrs = byteCode.validAddresses();
		int n = addrs.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = byteCode.lineOfAddress(addrs[i]);
		
		// Cache it and return a safe copy of it
		springMethod._lineTable = rv;
		return rv.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int methodLocationCount(Object __which, int __methodDx)
	{
		// If there is no method byte code then ignore
		ByteCode byteCode = DebugViewType.__method(__which, __methodDx)
			.method.byteCode();
		if (byteCode != null)
			return byteCode.instructionCount();
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String methodName(Object __which, int __methodDx)
	{
		return DebugViewType.__method(__which, __methodDx).name().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public String methodSignature(Object __which, int __methodDx)
	{
		return DebugViewType.__method(__which, __methodDx).nameAndType()
			.type().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public int[] methods(Object __which)
	{
		SpringMethod[] methods = DebugViewType.__class(__which).methodLookup();
		
		int n = methods.length;
		int[] rv = new int[n];
		for (int i = 0; i < n; i++)
			rv[i] = methods[i].methodIndex;
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/14
	 */
	@Override
	public boolean readValue(Object __which, int __index, JDWPValue __out)
	{
		SpringFieldStorage storage;
		try
		{
			storage = DebugViewType.__class(__which).classLoader().machine()
				.lookupStaticField(DebugViewType.__field(__which, __index));
		}
		catch (SpringVirtualMachineException ignored)
		{
			return false;
		}
		
		// Read value
		__out.set(storage.get());
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/11
	 */
	@Override
	public String signature(Object __which)
	{
		return DebugViewType.__class(__which).name.field().toString();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/15
	 */
	@Override
	public String sourceFile(Object __which)
	{
		return DebugViewType.__class(__which).file.sourceFile();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2021/04/12
	 */
	@Override
	public Object superType(Object __which)
	{
		return DebugViewType.__class(__which).superclass;
	}
	
	/**
	 * Gets the class from the given value.
	 * 
	 * @param __which Which to convert from.
	 * @return The spring class of the given type.
	 * @since 2021/04/15
	 */
	private static SpringClass __class(Object __which)
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
	
	/**
	 * Looks up the given field.
	 * 
	 * @param __which Which class to read from.
	 * @param __fieldDx The field index.
	 * @return The method for the given index.
	 * @since 2021/04/16
	 */
	private static SpringField __field(Object __which, int __fieldDx)
	{
		try
		{
			return DebugViewType.__class(__which).lookupField(__fieldDx);
		}
		catch (SpringNoSuchMethodException e)
		{
			throw JDWPCommandException.tossInvalidField(
				__which, __fieldDx, e);
		}
	}
	
	/**
	 * Looks up the given method.
	 * 
	 * @param __which Which class to read from.
	 * @param __methodDx The method index.
	 * @return The method for the given index.
	 * @since 2021/04/15
	 */
	private static SpringMethod __method(Object __which, int __methodDx)
	{
		try
		{
			return DebugViewType.__class(__which).lookupMethod(__methodDx);
		}
		catch (SpringNoSuchMethodException e)
		{
			throw JDWPCommandException.tossInvalidMethod(
				__which, __methodDx, e);
		}
	}
}
