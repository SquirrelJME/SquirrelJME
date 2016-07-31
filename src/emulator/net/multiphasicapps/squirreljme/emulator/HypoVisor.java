// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.emulator;

/**
 * This interface is passed to the emulator which specifies how certain
 * undefined behavior for a given system is defined. Essentially this class can
 * be seen as the trap manager for an emulator in that anything unknown by the
 * emulator is handled by this interface.
 *
 * @since 2016/07/30
 */
public interface HypoVisor
{
}

