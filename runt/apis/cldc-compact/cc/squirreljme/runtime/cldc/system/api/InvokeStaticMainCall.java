// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * Interface for {@link SystemFunction#INVOKE_STATIC_MAIN}.
 *
 * @since 2018/03/14
 */
public interface InvokeStaticMainCall
	extends Call
{
	/**
	 * Invokes the static main method in the specified class.
	 *
	 * @param __c The class containing the method to invoke.
	 * @param __args Arguments to the main method.
	 * @since 2018/03/14
	 */
	public abstract void InvokeStaticMainCall(ClassType __c, String... __args);
}

