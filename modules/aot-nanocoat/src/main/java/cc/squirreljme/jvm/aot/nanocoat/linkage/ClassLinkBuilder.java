// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import cc.squirreljme.jvm.aot.nanocoat.common.JvmInvokeType;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.util.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.FieldReference;
import net.multiphasicapps.classfile.MethodReference;

/**
 * Represents the class linkage table.
 * 
 * @since 2023/06/03
 */
public class ClassLinkBuilder
	implements Iterable<Linkage>
{
	/** Linkage table map. */
	private final Map<Linkage, Integer> _indexMap =
		new LinkedHashMap<>();
	
	/** Linkage values. */
	private final List<Linkage> _table =
		new ArrayList<>();
	
	/**
	 * Initializes the link table.
	 * 
	 * @since 2023/06/03
	 */
	public ClassLinkBuilder()
	{
		// Zero is an invalid entry
		this._table.add(null);
	}
	
	/**
	 * Builds the class link.
	 *
	 * @return The resultant class link.
	 * @since 2023/08/29
	 */
	public ClassLink build()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Linkage for referencing a class object.
	 * 
	 * @param __className The class name.
	 * @return The linkage for the class object.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public Container<ClassObjectLinkage> classObject(String __className)
		throws NullPointerException
	{
		return this.classObject(new ClassName(__className));
	}
	
	/**
	 * Linkage for referencing a class object.
	 * 
	 * @param __className The class name.
	 * @return The linkage for the class object.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public Container<ClassObjectLinkage> classObject(ClassName __className)
		throws NullPointerException
	{
		if (__className == null)
			throw new NullPointerException("NARG");
		
		return this.put(ClassObjectLinkage.class,
			new ClassObjectLinkage(__className));
	}
	
	/**
	 * Creates or retrieves a linkage to access a field.
	 *
	 * @param __static Is the access static?
	 * @param __target The target field being access.
	 * @param __store Is the access writing the value?
	 * @return The container for the linkage.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public Container<FieldAccessLinkage> fieldAccess(
		boolean __static, FieldReference __target, boolean __store)
		throws NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		return this.put(FieldAccessLinkage.class,
			new FieldAccessLinkage(__static, __target, __store));
	}
	
	/**
	 * Potentially creates or returns a pre-existing method invocation.
	 *
	 * @param __type The type of invocation.
	 * @param __target The target method.
	 * @return The normal linkage.
	 * @throws IllegalArgumentException If the invocation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public Container<InvokeLinkage> invoke(
		JvmInvokeType __type, MethodReference __target)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null || __target == null)
			throw new NullPointerException("NARG");
		
		return this.put(InvokeLinkage.class,
			new InvokeLinkage(__type, __target));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/25
	 */
	@Override
	public Iterator<Linkage> iterator()
	{
		return UnmodifiableIterator.of(this._table);
	}
	
	/**
	 * Puts the input into the linkage map if it does not yet exist, otherwise
	 * returns the already contained linkage.
	 * 
	 * @param <L> The linkage to add.
	 * @param __type The input type.
	 * @param __input The input.
	 * @return The added or pre-existing linkage.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/03
	 */
	public <L extends Linkage> Container<L> put(Class<L> __type, L __input)
		throws NullPointerException
	{
		if (__type == null || __input == null)
			throw new NullPointerException("NARG");
		
		Map<Linkage, Integer> indexMap = this._indexMap;
		List<Linkage> table = this._table;
		
		// Needs to be added?
		Integer index = indexMap.get(__type.cast(__input));
		if (index == null)
		{
			index = table.size();
			indexMap.put(__input, index);
			table.add(__input);
		}
		
		// Setup container
		return new Container<>(index, __type.cast(table.get(index)));
	}
	
	/**
	 * Returns the number of linkages.
	 *
	 * @return The number of linkages.
	 * @since 2023/07/25
	 */
	public int size()
	{
		return this._table.size();
	}
	
	/**
	 * Obtain a linkage for a string.
	 * 
	 * @param __string The string to add.
	 * @return The linkage for the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public Container<StringLinkage> string(String __string)
		throws NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		return this.put(StringLinkage.class,
			new StringLinkage(__string));
	}
}
