// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class contains the state of the linker, essentially it is the root
 * class which contains all of the other state needed to generate the output
 * executable. SquirrelJME operates in a purely static nature with no dynamic
 * function (that is simulated on top of the static state).
 *
 * @since 2017/06/15
 */
public class LinkerState
{
	/**
	 * This is a singular self reference which sub-classes refer to when they
	 * need to access the linker state. This allows the JIT to be garbage
	 * collected in the reference counted SquirrelJME. This reference here is
	 * valid because if the holding class instance is garbage collected the
	 * references being only weak would be invalidated.
	 */
	protected final Reference<LinkerState> selfref =
		new WeakReference<>(this);
	
	/** The dynamic code generation reference table. */
	protected final Dynamics dynamics =
		new Dynamics(this.selfref);
	
	/** This contains the packages which exist to the linker. */
	protected final PackageIdentifiers packages =
		new PackageIdentifiers(this.selfref);
	
	/** Sections which should exit in the output executable. */
	protected final Sections sections =
		new Sections(this.selfref);
}

