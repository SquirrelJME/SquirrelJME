// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.brackets.RefLinkBracket;

/**
 * This contains the storage for reference links, these chain to each other.
 *
 * @see RefLinkBracket
 * @since 2020/05/30
 */
public final class RefLinkObject
	extends AbstractGhostObject
{
	/** The object this links. */
	volatile SpringObject _object;
	
	/** The next link in the chain. */
	volatile RefLinkObject _next;
	
	/** The previous link in the chain. */
	volatile RefLinkObject _prev;
}
