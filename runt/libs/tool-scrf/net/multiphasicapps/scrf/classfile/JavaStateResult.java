// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.classfile;

import net.multiphasicapps.classfile.JavaType;
import net.multiphasicapps.scrf.RegisterLocation;

/**
 * This contains the result of the operation when a push or pop has been
 * performed. Since pushing/popping is complicated and will involve
 * information on the type and registers, this is needed to
 * simplify the design of the processor.
 *
 * @since 2019/02/23
 */
public final class JavaStateResult
{
	/** The Java type which is involved here. */
	public final JavaType type;
	
	/** The register used. */
	public final RegisterLocation register;
	
	/**
	 * Initializes the result.
	 *
	 * @param __jt The Java type.
	 * @param __rl The register location.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/16
	 */
	public JavaStateResult(JavaType __jt, RegisterLocation __rl)
		throws NullPointerException
	{
		if (__jt == null || __rl == null)
			throw new NullPointerException("NARG");
		
		this.type = __jt;
		this.register = __rl;
	}
}

