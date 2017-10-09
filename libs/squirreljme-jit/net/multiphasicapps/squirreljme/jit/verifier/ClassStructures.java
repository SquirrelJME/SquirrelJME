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

/**
 * This contains the structure of every class which is available to the
 * virtual machine.
 *
 * Like {@link FamilyTree} this class is lazily initialized.
 *
 * This class is thread safe.
 *
 * @since 2017/10/09
 */
public final class ClassStructures
{
	/** Lock for safety. */
	final Object _lock =
		new Object();
	
	/** The source class tree with inheritence information. */
	protected final FamilyTree tree;
	
	/**
	 * Initializes the class structures.
	 *
	 * @param __tree The family tree of classes.
	 * @throws NullPointerException On null arguments.
	 * @throws VerificationException If the class structure could not be
	 * verified.
	 * @since 2017/10/09
	 */
	public ClassStructures(FamilyTree __tree)
		throws NullPointerException, VerificationException
	{
		if (__tree == null)
			throw new NullPointerException("NARG");
		
		this.tree = __tree;
	}
}

