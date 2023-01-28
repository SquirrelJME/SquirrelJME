// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;
import cc.squirreljme.runtime.cldc.annotation.Exported;

/**
 * Key events for
 * {@link UIFormCallback#eventKey(cc.squirreljme.jvm.mle.brackets.UIDrawableBracket, int, int, int)}.
 *
 * @since 2020/07/19
 */
@Exported
public interface UIKeyEventType
{
	/** Key pressed. */
	@Exported
	byte KEY_PRESSED =
		0;
	
	/** Key released. */
	@Exported
	byte KEY_RELEASE =
		1;
	
	/** Key repeated. */
	@Exported
	byte KEY_REPEATED =
		2;
	
	/** Command activated (special). */
	@Exported
	byte COMMAND_ACTIVATED =
		3;
	
	/** The number of key events. */
	@Exported
	byte NUM_KEY_EVENTS =
		4;
}
