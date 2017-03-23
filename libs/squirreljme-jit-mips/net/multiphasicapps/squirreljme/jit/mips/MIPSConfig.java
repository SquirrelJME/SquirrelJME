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

import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigSerializer;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This is the configuration that is used to configure the JIT for the MIPS
 * translation engine.
 *
 * @since 2017/02/02
 */
public class MIPSConfig
	extends JITConfig<MIPSConfig>
{
	/**
	 * Initializes the MIPS config.
	 *
	 * @param __kvp The key/value pairs.
	 * @since 2017/02/02
	 */
	public MIPSConfig(String... __kvp)
	{
		super(__kvp);
	}
	
	/**
	 * Initializes the MIPS config.
	 *
	 * @param __kvp The key/value pairs.
	 * @since 2017/02/02
	 */
	public MIPSConfig(Map<String, String> __kvp)
	{
		super(__kvp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/13
	 */
	@Override
	public int bits()
	{
		int rv = super.bits();
		
		// {@squirreljme.error AM02 The specified number of CPU bits is not
		// supported by the CPU revision.}
		if (!mipsRevision().supportsBits(rv))
			throw new JITException("AM02");
		
		return rv;
	}
	
	/**
	 * Returns {@code true} if branch delay slots are to be used.
	 *
	 * @return {@code true} if branch delay slots are used.
	 * @since 2017/03/22
	 */
	public boolean hasBranchDelaySlots()
	{
		// Force no delay slots
		if (Boolean.valueOf(internalValue("mips.nobranchdelayslots")))
			return false;
		
		// Force delay slots
		else if (Boolean.valueOf(internalValue("mips.usebranchdelayslots")))
			return true;
		
		// Depends on the revision
		return mipsRevision().hasBranchDelaySlots();
	}
	
	/**
	 * Returns {@code true} if load delay slots are to be used.
	 *
	 * @return {@code true} if load delay slots are used.
	 * @since 2017/03/22
	 */
	public boolean hasLoadDelaySlots()
	{
		// Force no delay slots
		if (Boolean.valueOf(internalValue("mips.noloaddelayslots")))
			return false;
		
		// Force delay slots
		else if (Boolean.valueOf(internalValue("mips.useloaddelayslots")))
			return true;
		
		// Depends on the revision
		return mipsRevision().hasLoadDelaySlots();
	}
	
	/**
	 * Are doubles 64-bit?
	 *
	 * @return {@code true} if doubles are 64-bit.
	 * @since 2017/03/21
	 */
	public boolean isLongDouble()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the revision of the MIPS CPU to target.
	 *
	 * @return The revision of the CPU to target, if not specified it defaults
	 * to the first revision.
	 * @since 2017/02/13
	 */
	public MIPSRevision mipsRevision()
	{
		String v = internalValue("mips.revision");
		if (v == null)
			return MIPSRevision.I;
		return MIPSRevision.of(v);
	}
	
	/**
	 * Returns {@code true} if this is at least a 6th revision MIPS.
	 *
	 * @return {@code true} if this uses 6th revision MIPS.
	 * @since 2017/03/23
	 */
	public boolean mipsSix()
	{
		return mipsRevision().ordinal() >= MIPSRevision.R6.ordinal();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/02
	 */
	@Override
	public MIPSConfigSerializer serializer()
	{
		return MIPSConfigSerializer.instance();
	}
}

