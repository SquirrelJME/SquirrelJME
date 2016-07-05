// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.ServiceLoader;

/**
 * This is used to modify the the JIT for a given operating system.
 *
 * If a modifier for a given operating system and architecture (with variant)
 * exists then it will be given the JIT after it is constructed.
 *
 * This is used with the service loader.
 *
 * @since 2016/07/02
 */
@Deprecated
public abstract class JITOSModifier
{
	/** The architecture this modifies. */
	protected final String arch;
	
	/** The operating system this is for. */
	protected final String os;
	
	/**
	 * Initializes the base modifier for a given operating system.
	 *
	 * @param __arch The architecture to modify.
	 * @param __os The operating system this is for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	public JITOSModifier(String __arch, String __os)
		throws NullPointerException
	{
		// Check
		if (__arch == null || __os == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.arch = __arch;
		this.os = __os;
	}
	
	/**
	 * Returns the default endianess for the given OS.
	 *
	 * @return The default endianess to use.
	 * @since 2016/07/03
	 */
	public abstract JITCPUEndian defaultEndianess();
	
	/**
	 * Modifies the specified JIT to match better with this operating
	 * system.
	 *
	 * @param __jit The JIT to modify.
	 * @since 2016/07/03
	 */
	protected abstract void modify(JIT __jit);
	
	/**
	 * Returns the name of the architecture this modifies.
	 *
	 * @return The modifying architecture name.
	 * @since 2016/07/03
	 */
	public final String architectureName()
	{
		return this.arch;
	}
	
	/**
	 * Returns the default variant of a given architecture.
	 *
	 * If this is not overridden then {@code "generic"} is used.
	 *
	 * @return The default CPU variant.
	 * @since 2016/07/03
	 */
	public String defaultArchitectureVariant()
	{
		return "generic";
	}
	
	/**
	 * Returns the name of the operating system this modifies.
	 *
	 * @return The operating system this modifies.
	 * @since 2016/07/03
	 */
	public final String operatingSystemName()
	{
		return this.os;
	}
	
	/**
	 * Modifies the JIT for a given operating system.
	 *
	 * @param __jit the JIT to modify.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/03
	 */
	final void __modifyJIT(JIT __jit)
		throws NullPointerException
	{
		// Check
		if (__jit == null)
			throw new NullPointerException("NARG");
		
		modify(__jit);
	}
}

