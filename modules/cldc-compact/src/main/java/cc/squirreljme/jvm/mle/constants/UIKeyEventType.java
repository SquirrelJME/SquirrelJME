// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.brackets.UIFormBracket;
import cc.squirreljme.jvm.mle.brackets.UIItemBracket;
import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;

/**
 * Key events for
 * {@link UIFormCallback#eventKey(UIFormBracket, UIItemBracket, int, int)}.
 *
 * @since 2020/07/19
 */
public interface UIKeyEventType
{
	/** Key pressed. */
	byte KEY_PRESSED =
		0;
	
	/** Key released. */
	byte KEY_RELEASE =
		1;
	
	/** Key repeated. */
	byte KEY_REPEATED =
		2;
	
	/** The number of key events. */
	byte NUM_KEY_EVENTS =
		3;
}
