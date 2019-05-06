// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.lang.ApiLevel;

/**
 * This is used to package API calls which are either {@link Object[]} if they
 * use in-VM boxed types, or {@link int[]} if they consist of raw pointers
 * and such.
 *
 * @since 2019/05/06
 */
public final class UniversalPackage
{
	/** Packaged as boxed values. */
	public static final int PACKAGE_BOXED =
		0;
	
	/** Packaged as integer values. */
	public static final int PACKAGE_INTEGER =
		1;
	
	/** Value packaged as integer. */
	public static final int VALUE_INTEGER =
		0;
	
	/** Value package as long. */
	public static final int VALUE_LONG =
		1;
	
	/** Value packaged as float. */
	public static final int VALUE_FLOAT =
		2;
	
	/** Value packages as double. */
	public static final int VALUE_DOUBLE =
		3;
	
	/** Value packaged as object. */
	public static final int VALUE_OBJECT =
		4;
	
	/** Cached package type used for encoding and decoding. */
	static final int _PACKAGE_TYPE =
		UniversalAPI.packageType();
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/06
	 */
	private UniversalPackage()
	{
	}
	
	/**
	 * Decodes the given package to boxed values.
	 *
	 * @param __pkg The package to decode.
	 * @return The result of the unpackage.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final Object[] decode(Object __pkg)
		throws NullPointerException
	{
		if (__pkg == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Encodes the given values to a package.
	 *
	 * @param __args The arguments to package.
	 * @return The resulting package.
	 * @since 2019/05/06
	 */
	public static final Object encode(Object... __args)
	{
		throw new todo.TODO();
	}
}

