// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.trans.naive;

import net.multiphasicapps.squirreljme.jit.arch.MachineCodeOutput;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedBasicBlock;
import net.multiphasicapps.squirreljme.jit.expanded.ExpandedByteCode;

/**
 * This is the naive translator which performs no optimizations and essentially
 * generates very unoptimized and slightly odd machine code for the purpose
 * of being as simple as possible. This translator for the most part acts as a
 * baseline implementation for the first generation code generators so that
 * anything that is needed can be built using a simple interface where
 * optimization is not a concern.
 *
 * @since 2017/08/07
 */
public class NaiveTranslator
	implements ExpandedByteCode
{
}

