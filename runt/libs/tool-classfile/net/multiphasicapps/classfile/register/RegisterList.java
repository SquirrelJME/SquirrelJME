// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;

/**
 * This represents a list of registers.
 *
 * @since 2019/03/22
 */
public final class RegisterList
{
	/** The registers used. */
	private final int[] _registers;
	
	/** String form. */
	private Reference<String> _string;
	
	/**
	 * Initializes the list of registers.
	 *
	 * @param __r The registers to use.
	 * @since 2019/03/22
	 */
	public RegisterList(int... __r)
	{
		this._registers = (__r == null ? new int[0] : __r.clone());
	}
	
	/**
	 * Initializes the list of registers.
	 *
	 * @param __r The registers to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public RegisterList(Collection<Integer> __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		int n = __r.size();
		int[] regs = new int[n];
		Iterator<Integer> it = __r.iterator();
		for (int i = 0; i < n; i++)
			regs[i] = it.next();
		
		this._registers = regs;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/22
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			// Build list
			StringBuilder sb = new StringBuilder("R[");
			boolean comma = false;
			for (int i : this._registers)
			{
				if (comma)
					sb.append(", ");
				comma = true;
				
				sb.append(i);
			}
			sb.append(']');
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

