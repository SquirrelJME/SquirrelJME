// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.java.ClassCompiler;
import net.multiphasicapps.squirreljme.jit.rc.ResourceCompiler;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @since 2017/05/29
 */
public abstract class JITConfig
{
	/** Values stored within the configuration. */
	private final Map<JITConfigKey, JITConfigValue> _values =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the JIT configuration using the given option set.
	 *
	 * @param __o The options used for the JIT.
	 * @throws JITException If the options are not valid.
	 * @throws NullPointerException On null arguments or if the option map
	 * contains a null value.
	 * @since 2017/05/30
	 */
	protected JITConfig(Map<JITConfigKey, JITConfigValue> __o)
		throws JITException, NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Fill options into the value map
		Map<JITConfigKey, JITConfigValue> values = this._values;
		for (Map.Entry<JITConfigKey, JITConfigValue> e : __o.entrySet())
		{
			JITConfigKey k = e.getKey();
			JITConfigValue v = e.getValue();
			
			// Check
			if (k == null || v == null)
				throw new NullPointerException("NARG");
			
			values.put(k, v);
		}
		
		// {@squirreljme.error JI01 CPU architecture was not specified in the
		// JIT configuration.}
		if (values.get(new JITConfigKey("cpu.arch")) == null)
			throw new JITException("JI01");
	}
	
	/**
	 * Creates an instance of the compiler for the given class file.
	 *
	 * @param __is The stream containing the class data to compile.
	 * @param __ci The cluster the class is in.
	 * @param __lt The link table which is given the compiled class data.
	 * @return The compilation task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/29
	 */
	public final ClassCompiler compileClass(InputStream __is,
		ClusterIdentifier __ci, LinkTable __lt)
		throws NullPointerException
	{
		// Check
		if (__is == null || __ci == null || __lt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Compiles the specified resource and places it into the given link table.
	 *
	 * @param __is The stream containing the resource data.
	 * @param __n The name of the resource.
	 * @param __ci The cluster the resource is in.
	 * @param __lt The link table which is given the compiled resource data.
	 * @return The resource compiler task.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/02
	 */
	public final ResourceCompiler compileResource(InputStream __is,
		String __n, ClusterIdentifier __ci, LinkTable __lt)
		throws NullPointerException
	{
		// Check
		if (__is == null || __n == null || __ci == null || __lt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

