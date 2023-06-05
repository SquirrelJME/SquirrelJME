// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a C function.
 *
 * @since 2023/06/04
 */
public class CFunction
	implements CDeclarable, CDefinable
{
	/** The name of the function. */
	public final CIdentifier name;
	
	/** The return type. */
	public final CType returnType;
	
	/** Arguments to the function. */
	public final List<CVariable> arguments;
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	CFunction(CIdentifier __name, CType __rVal, CVariable... __args)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Check arguments for validity
		__args = __args.clone();
		for (CVariable var : __args)
			if (var == null)
				throw new NullPointerException("NARG");
		
		// Setup
		this.name = __name;
		this.returnType = __rVal;
		this.arguments = UnmodifiableList.of(Arrays.asList(__args));
	}
	
	/**
	 * Initializes the C function.
	 * 
	 * @param __name The function name.
	 * @param __rVal The return value, may be {@code null}.
	 * @param __args Arguments to the function.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/04
	 */
	public static CFunction of(CIdentifier __name, CType __rVal,
		CVariable... __args)
		throws NullPointerException
	{
		return new CFunction(__name, __rVal, __args);
	}
}
