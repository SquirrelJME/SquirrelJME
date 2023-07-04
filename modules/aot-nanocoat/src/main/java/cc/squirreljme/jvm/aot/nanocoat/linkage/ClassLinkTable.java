// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat.linkage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.MethodReference;

/**
 * Represents the class linkage table.
 * 
 * @since 2023/06/03
 */
public class ClassLinkTable
{
	/** Linkage table map. */
	private final Map<Linkage, Integer> _indexMap =
		new LinkedHashMap<>();
	
	/** Linkage values. */
	private List<Linkage> _table =
		new ArrayList<>();
	
	/**
	 * Initializes the link table.
	 * 
	 * @since 2023/06/03
	 */
	public ClassLinkTable()
	{
		// Zero is an invalid entry
		this._table.add(null);
	}
	
	/**
	 * Potentially creates or returns a pre-existing normal method invocation.
	 * 
	 * @param __source The source method.
	 * @param __static Is this a static invocation?
	 * @param __target The target method.
	 * @return The normal linkage.
	 * @throws IllegalArgumentException If the invocation is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public Container<InvokeNormalLinkage> invokeNormal(
		MethodNameAndType __source, boolean __static, MethodReference __target)
		throws IllegalArgumentException, NullPointerException
	{
		if (__source == null || __target == null)
			throw new NullPointerException("NARG");
		
		return this.put(InvokeNormalLinkage.class,
			new InvokeNormalLinkage(__source, __static, __target));
	}
	
	/**
	 * Obtains a linkage for special invocations.
	 * 
	 * @param __source The source method performing the call.
	 * @param __target The target method.
	 * @return The special linkage.
	 * @throws IllegalArgumentException If the target is an interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/30
	 */
	public Container<InvokeSpecialLinkage> invokeSpecial(
		MethodNameAndType __source, MethodReference __target)
		throws IllegalArgumentException, NullPointerException
	{
		if (__source == null || __target == null)
			throw new NullPointerException("NARG");
		
		return this.put(InvokeSpecialLinkage.class,
			new InvokeSpecialLinkage(__source, __target));
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
}
