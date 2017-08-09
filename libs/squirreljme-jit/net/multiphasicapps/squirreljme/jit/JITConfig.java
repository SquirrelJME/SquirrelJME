// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.InputStream;
import java.util.Map;
import java.util.LinkedHashMap;
import net.multiphasicapps.squirreljme.jit.bin.FlatSectionCounter;
import net.multiphasicapps.squirreljme.jit.bin.FragmentBuilder;
import net.multiphasicapps.squirreljme.jit.bin.SectionCounter;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @since 2017/05/29
 */
public abstract class JITConfig
{
	/** The default translator to use. */
	private static final JITConfigValue _DEFAULT_TRANSLATOR;
	
	/** Values stored within the configuration. */
	private final Map<JITConfigKey, JITConfigValue> _values =
		new SortedTreeMap<>();
	
	/**
	 * Initializes some settings.
	 *
	 * @since 2017/08/09
	 */
	static
	{
		// {@squirreljme.property
		// net.multiphasicapps.squirreljme.jit.translator=value
		// This sets the default translator for the expanded byte code engine
		// which may or may not perform optimizations.}
		_DEFAULT_TRANSLATOR = new JITConfigValue(System.getProperty(
			"net.multiphasicapps.squirreljme.jit.translator", "naive"));
	}
	
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
		
		// A translator is required
		if (values.get(JITConfigKey.JIT_TRANSLATOR) == null)
			values.put(JITConfigKey.JIT_TRANSLATOR, _DEFAULT_TRANSLATOR);
	}
	
	/**
	 * This creates a new section counter which is used to count text and data
	 * sections for placement in an output linked executable.
	 *
	 * By default a {@link FlatSectionCounter} is used, if an alternative is
	 * required then this may be replaced accordingly.
	 *
	 * @return The section counter for placing code and resources.
	 * @since 2017/06/28
	 */
	public SectionCounter createSectionCounter()
	{
		return new FlatSectionCounter();
	}
	
	/**
	 * Creates an {@link ExpandedByteCode} instance which may be attached to
	 * a native generation engine along with translators (for potential
	 * optimizations). 
	 *
	 * @param __f The output fragment where native instructions go.
	 * @return The expanded byte code engine which is used to generate the
	 * native machine code.
	 * @throws JITException If it could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public final ExpandedByteCode createExpandedByteCode(FragmentBuilder __f)
		throws JITException, NullPointerException
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns a copy of the JIT configuration options.
	 *
	 * @return A copy of the JIT configuration options.
	 * @since 2017/08/09
	 */
	public Map<JITConfigKey, JITConfigValue> options()
	{
		return new LinkedHashMap<>(this._values);
	}
}

