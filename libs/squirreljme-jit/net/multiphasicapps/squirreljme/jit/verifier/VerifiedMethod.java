// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.verifier;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.cff.ByteCode;
import net.multiphasicapps.squirreljme.jit.cff.MethodHandle;

/**
 * This is a single method which has been verified.
 *
 * @since 2017/10/09
 */
public final class VerifiedMethod
{
	/**
	 * Verifies the specified method.
	 *
	 * @param __structs The structures for the class.
	 * @param __mi The method index.
	 * @param __vid The the index of this method in the sort list.
	 * @param __bc The byte code for this method.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the method could not be verified.
	 * @since 2017/10/14
	 */
	final static VerifiedMethod __verify(ClassStructures __structs,
		MethodHandle __mi, int __vid, ByteCode __bc)
		throws NullPointerException, VerificationException
	{
		if (__structs == null || __mi == null || __bc == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

