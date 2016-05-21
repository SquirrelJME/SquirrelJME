// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This represents a single unit of communication that is sent and received
 * over a {@link KIOSocket}.
 *
 * Once a datagram is sent, it cannot be modified or read from by the sender.
 *
 * Datagrams may also send arrays to act as a kind of shared memory along with
 * their normal packet data if needed.
 *
 * @since 2016/05/20
 */
public final class KIODatagram
{
}

