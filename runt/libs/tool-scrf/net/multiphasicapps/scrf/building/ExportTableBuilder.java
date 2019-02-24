// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.building;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodFlags;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.scrf.ILCode;
import net.multiphasicapps.scrf.Exported;
import net.multiphasicapps.scrf.ExportedMethod;
import net.multiphasicapps.scrf.SummerFormatException;

/**
 * This is used for building the export tabled.
 *
 * @since 2019/02/24
 */
public final class ExportTableBuilder
{
	/** Exported objects. */
	private final Map<Integer, Exported> _exports =
		new LinkedHashMap<>();
	
	/** Next index. */
	private int _nextdx;
	
	/**
	 * Adds a single export.
	 *
	 * @param __e The export.
	 * @return {@code __e}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public final Exported add(Exported __e)
		throws NullPointerException
	{
		if (__e == null)
			throw new NullPointerException("NARG");
		
		// Map it
		int dx = this._nextdx++;
		this._exports.put(dx, __e);
		
		return __e;
	}
	
	/**
	 * Adds an exported method.
	 *
	 * @param __f The flags.
	 * @param __n The method name.
	 * @param __t The method type.
	 * @param __c The code, must be {@code null} if native/abstract.
	 * @throws NullPointerException On null arguments.
	 * @throws SummerFormatException If the native/abstract does not match
	 * {@code __c} being {@code null}.
	 * @since 2019/02/24
	 */
	public ExportedMethod addMethod(MethodFlags __f, MethodName __n,
		MethodDescriptor __t, ILCode __c)
		throws NullPointerException, SummerFormatException
	{
		if (__f == null || __n == null || __t == null)
			throw new NullPointerException("NARG");
		
		return (ExportedMethod)this.add(
			new ExportedMethod(__f, __n, __t, __c));
	}
}
