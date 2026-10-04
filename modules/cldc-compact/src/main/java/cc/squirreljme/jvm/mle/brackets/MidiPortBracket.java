// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;
import org.jetbrains.annotations.Debug;

/**
 * This represents a connected MIDI port.
 *
 * @see MidiShelf
 * @since 2022/04/20
 */
@GhostObject
@Debug.Renderer(text=GhostObject.INTELLIJ_RENDERER,
	hasChildren="false")
@SquirrelJMENativeApi(min = "0.4.0")
public interface MidiPortBracket
	extends Bracket
{
}
