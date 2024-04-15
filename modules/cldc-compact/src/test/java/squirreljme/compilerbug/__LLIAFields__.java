// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.compilerbug;

import net.multiphasicapps.tac.TestRunnable;

/**
 * This exists to store fields for the linked list iterator compiler bug test
 * but Jasmin is being stupidly impossible with fields.
 *
 * @since 2021/06/16
 */
@SuppressWarnings("unused")
abstract class __LLIAFields__
	extends TestRunnable
{
	int _vdx;
	int _atmod;
    FakeLinkedList list;
	__Link__ _next;
	__Link__ _last;
}
