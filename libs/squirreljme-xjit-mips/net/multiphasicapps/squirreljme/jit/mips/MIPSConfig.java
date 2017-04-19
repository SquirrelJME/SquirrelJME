// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.ArgumentAllocation;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigSerializer;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.squirreljme.jit.NativeType;
import net.multiphasicapps.squirreljme.jit.RegisterDictionary;
import net.multiphasicapps.squirreljme.jit.RegisterList;

/**
 * This is the configuration for the MIPS JIT.
 *
 * @since 2017/04/02
 */
public class MIPSConfig
	extends JITConfig
{
	/** The cached register dictionary. */
	private volatile Reference<RegisterDictionary> _rdict;
	
	/**
	 * Initializes the MIPS configuration.
	 *
	 * @param __kv The key/value pairs.
	 * @since 2017/04/02
	 */
	public MIPSConfig(String... __kv)
	{
		super(__kv);
	}
	
	/**
	 * Initializes the MIPS configuration.
	 *
	 * @param __kv The key/value pairs.
	 * @since 2017/04/02
	 */
	public MIPSConfig(Map<String, String> __kv)
	{
		super(__kv);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/16
	 */
	@Override
	public ArgumentAllocation[] entryAllocations(NativeType... __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// 
		RegisterDictionary rdict = registerDictionary();
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public RegisterDictionary registerDictionary()
	{
		Reference<RegisterDictionary> ref = this._rdict;
		RegisterDictionary rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._rdict = new WeakReference<>(
				(rv = new MIPSRegisterDictionary(this)));
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/04/02
	 */
	@Override
	public JITConfigSerializer serializer()
	{
		throw new todo.TODO();
	}
}

