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
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is used to access the configuration which is needed by the JIT during
 * the JIT compilation set.
 *
 * @since 2017/05/29
 */
public abstract class JITConfig
{
	/** The number of bits used for addresses. */
	public static final JITConfigKey JIT_ADDRESSBITS =
		new JITConfigKey("jit.addressbits");
	
	/** The architecture that is being targetted. */
	public static final JITConfigKey JIT_ARCH =
		new JITConfigKey("jit.arch");
	
	/** Should the assembler be dumped? */
	public static final JITConfigKey JIT_DUMP_ASM =
		new JITConfigKey("jit.dump.assembler");
	
	/** Should the output fragments be dumped? */
	public static final JITConfigKey JIT_DUMP_FRAGMENT =
		new JITConfigKey("jit.dump.fragment");
	
	/** Should the high level IL be dumped? */
	public static final JITConfigKey JIT_DUMP_HIL =
		new JITConfigKey("jit.dump.hil");
	
	/** Should the low level IL be dumped? */
	public static final JITConfigKey JIT_DUMP_LIL =
		new JITConfigKey("jit.dump.lil");
	
	/** Should profiling information be included? */
	public static final JITConfigKey JIT_PROFILE =
		new JITConfigKey("jit.profile");
	
	/** Keys which are included by the JIT by default. */
	private static final JITConfigKey[] _DEFAULT_KEYS =
		new JITConfigKey[]
		{
			JIT_ADDRESSBITS,
			JIT_ARCH,
			JIT_DUMP_ASM,
			JIT_DUMP_FRAGMENT,
			JIT_DUMP_HIL,
			JIT_DUMP_LIL,
			JIT_PROFILE,
		};
	
	/** Values stored within the configuration, untranslated. */
	private final Map<JITConfigKey, JITConfigValue> _values =
		new SortedTreeMap<>();
	
	/** String representation of this configuration. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes some settings.
	 *
	 * @since 2017/08/09
	 */
	static
	{
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
	 * an internal buffer which may then return the required bytes.
	 *
	 * @param __fd The destination for the created fragment.
	 * @return The output for native machine code which matches this given
	 * JIT.
	 * @throws JITException If the output could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/09
	 */
	public abstract MachineCodeOutput createMachineCodeOutput(
		FragmentDestination __fd)
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
		return rv.isTrue();
	}
	
	/**
	 * Returns the int value of the specified key.
	 *
	 * @param __k The key to get the value of.
	 * @return The value of the given key.
	 * @throws NumberFormatException If the value could not be parsed.
	 * @since 2017/08/19
	 */
	public final int getInteger(JITConfigKey __k)
		throws NumberFormatException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI25 The specified key is null. (The key)}
		JITConfigValue rv = get(__k);
		if (rv == null)
			throw new NumberFormatException(String.format("JI25 %s", __k));
		return rv.toInteger();
	}
	
	/**
	 * Obtains a int value or returns a default value.
	 *
	 * @param __k The key to get.
	 * @param __def The default value to return if the value is not parsed as
	 * the requested type.
	 * @return The decoded value or {@code __def}.
	 * @since 2017/08/19
	 */
	public final int getInteger(JITConfigKey __k, int __def)
	{
		// Could fail
		try
		{
			return getInteger(__k);
		}
		
		// Not a number
		catch (NumberFormatException e)
		{
			return __def;
		}
	}
	
	/**
	 * Returns the long value of the specified key.
	 *
	 * @param __k The key to get the value of.
	 * @return The value of the given key.
	 * @throws NumberFormatException If the value could not be parsed.
	 * @since 2017/08/19
	 */
	public final long getLong(JITConfigKey __k)
		throws NumberFormatException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI26 The specified key is null. (The key)}
		JITConfigValue rv = get(__k);
		if (rv == null)
			throw new NumberFormatException(String.format("JI26 %s", __k));
		return rv.toLong();
	}
	
	/**
	 * Obtains a long value or returns a default value.
	 *
	 * @param __k The key to get.
	 * @param __def The default value to return if the value is not parsed as
	 * the requested type.
	 * @return The decoded value or {@code __def}.
	 * @since 2017/08/19
	 */
	public final long getLong(JITConfigKey __k, long __def)
	{
		// Could fail
		try
		{
			return getLong(__k);
		}
		
		// Not a number
		catch (NumberFormatException e)
		{
			return __def;
		}
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

