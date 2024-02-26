// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

import cc.squirreljme.jvm.mle.MidiShelf;
import cc.squirreljme.jvm.mle.annotation.GhostObject;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This represents a MIDI device to be used in {@link MidiShelf}, it contains
 * transmitters and receivers via {@link MidiPortBracket}.
 *
 * @see MidiPortBracket
 * @see MidiShelf
 * @since 2022/04/21
 */
@GhostObject
@SquirrelJMEVendorApi
public interface MidiDeviceBracket
{
}
