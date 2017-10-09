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
import net.multiphasicapps.squirreljme.jit.JITInput;

/**
 * This contains the family tree of classes and is used to store the
 * inheritence information of classes. It only contains information on which
 * classes extend other classes.
 *
 * This class will handle and verify input of nodes within the tree as they
 * are demanded. As such with lazy initialization, information is slowly
 * built up.
 *
 * This class is thread safe.
 *
 * @since 2017/10/09
 */
public final class FamilyTree
{
	/** Lock for tree access. */
	final Object _lock =
		new Object();
	
	/** The input for the JIT. */
	protected final JITInput input;
	
	/**
	 * Initializes the family tree from the given base input classes.
	 *
	 * @param __i The class input to use for when the tree is generated.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the tree could not be verified.
	 * @since 2017/10/09
	 */
	public FamilyTree(JITInput __i)
		throws NullPointerException, VerificationException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.input = __i;
	}
}

