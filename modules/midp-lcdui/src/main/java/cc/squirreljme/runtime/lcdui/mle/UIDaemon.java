// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.jvm.mle.callbacks.UIFormCallback;

/**
 * Represents a user-interface daemon.
 *
 * @since 2020/07/03
 */
@SuppressWarnings("InterfaceWithOnlyOneDirectInheritor")
public interface UIDaemon
	extends AutoCloseable, Runnable, UIFormCallback
{
}
