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
 * This is the user interface bridge which utilizes an IPC server hosted by
 * the actual display manager to use. The client permits access to the server
 * display manager across execution and security zones.
 *
 * It is possible to multiply forward this over IPCs across various processes
 * but that is not recommended due to the potential loss in speed.
 *
 * @since 2016/05/20
 */

package net.multiphasicapps.squirreljme.ui.ipc.client;

