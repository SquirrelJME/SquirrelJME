// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

/**
 * This wraps an existing user interface and makes it accessible over the IPC
 * system so that user interfaces do not rely directly on implementation
 * dependent user interface. Also, the execution environment of programs may
 * be mixed between native, interpreter, and remote which means that objects
 * would have to be wrapped using complicated patches in native and interpreted
 * execution.
 *
 * It is possible to multiply forward this over IPCs across various processes
 * but that is not recommended due to the potential loss in speed.
 *
 * @since 2016/05/20
 */

package net.multiphasicapps.squirreljme.ui.ipc.server;

