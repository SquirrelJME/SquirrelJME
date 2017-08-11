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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import net.multiphasicapps.squirreljme.jit.arch.DumpMachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.bin.FlatSectionCounter;
import net.multiphasicapps.squirreljme.jit.bin.FragmentBuilder;
import net.multiphasicapps.squirreljme.jit.bin.SectionCounter;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;
import net.multiphasicapps.squirreljme.jit.trans.DumpTranslator;
import net.multiphasicapps.squirreljme.jit.trans.TranslatorService;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @since 2017/05/29
 */
public abstract class JITConfig
{
	/** Translators available for usage. */
	private static final ServiceLoader<TranslatorService> _TRANSLATORS =
		ServiceLoader.<TranslatorService>load(TranslatorService.class);
	
	/** The default translator to use. */
	private static final JITConfigValue _DEFAULT_TRANSLATOR;
	
	/** Keys which are included by the JIT by default. */
	private static final JITConfigKey[] _DEFAULT_KEYS =
		new JITConfigKey[]
		{
			JITConfigKey.JIT_ADDRESSBITS,
			JITConfigKey.JIT_ARCH,
			JITConfigKey.JIT_DUMP_ASSEMBLER,
			JITConfigKey.JIT_DUMP_TRANSLATOR,
			JITConfigKey.JIT_PROFILE,
			JITConfigKey.JIT_TRANSLATOR,
		};
	
	/** Values stored within the configuration, untranslated. */
	private final Map<JITConfigKey, JITConfigValue> _values =
		new SortedTreeMap<>();
	
	/** String representation of this configuration. */
	private volatile Reference<String> _string;
	
	/** The translator service to use. */
	private volatile Reference<TranslatorService> _translator;
	
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
		
		// Obtain the set of keys which are valid to be used within the
		// configuration. The target may have duplicates which are not
		// processed multiple times.
		Set<JITConfigKey> dks = new LinkedHashSet<>();
		for (JITConfigKey dk : _DEFAULT_KEYS)
			if (dk != null)
				dks.add(dk);
		for (JITConfigKey dk : targetDefaultKeys())
			if (dk != null)
				dks.add(dk);
		
		// Only fill the option map with valid keys that are used to configure
		// the output system. Only use default keys and ignore other key
		// values
		Map<JITConfigKey, JITConfigValue> values = this._values;
		for (JITConfigKey dk : dks)
			values.put(dk, __o.get(dk));
		
		// Look for options such as unsafe class rewrites and add them
		for (Map.Entry<JITConfigKey, JITConfigValue> e : __o.entrySet())
		{
			JITConfigKey k = e.getKey();
			JITConfigValue v = e.getValue();
			
			// Class rename, always include them
			if (k != null && k.toString().startsWith("jit.rename."))
			{
				if (v != null)
					values.put(k, v);
			}
		}
		
		// {@squirreljme.error JI01 CPU architecture was not specified in the
		// JIT configuration.}
		if (get(JITConfigKey.JIT_ARCH) == null)
			throw new JITException("JI01");
	}
	
	/**
	 * Creates an instance of the native machine code output which writes to
	 * the specified fragment.
	 *
	 * @param __f The fragment to write instructions to.
	 * @return The output for native machine code which matches this given
	 * JIT.
	 * @throws JITException If the output could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public abstract MachineCodeOutput createMachineCodeOutput(
		FragmentBuilder __f)
		throws JITException, NullPointerException;
	
	/**
	 * Returns the set of keys which are provided by default for the target
	 * JIT configuration.
	 *
	 * @return The set of default keys.
	 * @since 2017/08/10
	 */
	protected abstract JITConfigKey[] targetDefaultKeys();
	
	/**
	 * Translates the value for the specified key and value pair which is
	 * specific to this JIT configuration.
	 *
	 * @param __k The input key.
	 * @param __v The input value, if this is {@code null} then it has not
	 * been set and may be set to a default value if applicable.
	 * @return The output value.
	 * @throws NullPointerException If the key is null.
	 * @since 2017/08/10
	 */
	protected abstract JITConfigValue targetTranslateValue(JITConfigKey __k,
		JITConfigValue __v)
		throws NullPointerException;
	
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
		
		// This will be wrapped by the translator
		MachineCodeOutput mco = createMachineCodeOutput(__f);
		
		// If dumping is enabled, wrap this output with a dumper
		if (getBoolean(JITConfigKey.JIT_DUMP_ASSEMBLER))
			mco = new DumpMachineCodeOutput(mco);
		
		// Has the translator been cached already?
		Reference<TranslatorService> ref = this._translator;
		TranslatorService translator = null;
		
		// Locate the translator and cache it, since it will be used multiple
		// times
		String want = getString(JITConfigKey.JIT_TRANSLATOR);
		if (ref == null || null == (translator = ref.get()))
		{
			// Locate one
			ServiceLoader<TranslatorService> translators = _TRANSLATORS;
			synchronized (translators)
			{
				for (TranslatorService sv : translators)
					if (want.equals(sv.name()))
					{
						translator = sv;
						break;
					}
			}
			
			// {@squirreljme.error JI20 The specified translator is not
			// valid. (The translator)}
			if (translator == null)
				throw new JITException(String.format("JI20", want));
			
			// Cache
			this._translator = new WeakReference<>(translator);
		}
		
		// Create instance, if dumping is enabled then dump anything sent to
		// this
		ExpandedByteCode rv = translator.createTranslator(mco);
		if (getBoolean(JITConfigKey.JIT_DUMP_TRANSLATOR))
			return new DumpTranslator(rv);
		return rv;
	}
	
	/**
	 * Obtains the value for the given key.
	 *
	 * @param __k The key to get.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public final JITConfigValue get(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// As a special condition, the architecture is never translated and
		// will never get a default value
		JITConfigValue rv = this._values.get(__k);
		if ("cpu.arch".equals(__k.toString()))
			return rv;
		
		// Internally translate the input value so it has a default value
		// where possible
		rv = __internalTranslate(__k, rv);
		
		// Only target tranlsate if it is not special
		if (__isSpecialKey(__k))
			return rv;
		return targetTranslateValue(__k, rv);
	}
	
	/**
	 * Obtains the value for the given key.
	 *
	 * @param __k The key to get.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public final boolean getBoolean(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Get
		JITConfigValue rv = get(__k);
		if (rv == null)
			return false;
		return Boolean.valueOf(rv.toString());
	}
	
	/**
	 * Obtains the value for the given key.
	 *
	 * @param __k The key to get.
	 * @return The value for the given key.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	public final String getString(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Get
		JITConfigValue rv = get(__k);
		if (rv == null)
			return "null";
		return rv.toString();
	}
	
	/**
	 * Returns a copy of the JIT configuration options.
	 *
	 * @return A copy of the JIT configuration options.
	 * @since 2017/08/09
	 */
	public final Map<JITConfigKey, JITConfigValue> options()
	{
		// The target map
		Map<JITConfigKey, JITConfigValue> rv = new LinkedHashMap<>();
		
		// Make sure all resulting values are translated!
		for (JITConfigKey k : this._values.keySet())
			rv.put(k, get(k));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/10
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = options().toString()));
		
		return rv;
	}
	
	/**
	 * Internally translates the key and value to a specified value, a default
	 * may be set if the input is null.
	 *
	 * @param __k The key to check.
	 * @param __v The input value, if this is {@code null} then it has not
	 * been set and may be set to a default value if applicable.
	 * @return The translated value, if it was translated.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/10
	 */
	private final JITConfigValue __internalTranslate(JITConfigKey __k,
		JITConfigValue __v)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Translate?
		switch (__k.toString())
		{
				// Translator, default to naive
				// May be replaced by the target if there is a more optimal
				// translator available
			case "jit.translator":
				if (__v == null)
					return _DEFAULT_TRANSLATOR;
				break;
				
				// Is profiling enabled?
			case "jit.profile":
				return JITConfigValue.matchesTrue(__v);
			
				// Unchanged
			default:
				break;
		}
		
		// Unchanged
		return __v;
	}
	
	/**
	 * Checks whether the given key is special, if it is then it will not
	 * be translated by the target.
	 *
	 * @param __k The key to check if it is special.
	 * @return Whether it is special or not.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/0810
	 */
	private final boolean __isSpecialKey(JITConfigKey __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Translate?
		switch (__k.toString())
		{
				// Special keys
			case "jit.profile":
				return true;
			
				// Not special
			default:
				return false;
		}
	}
}

