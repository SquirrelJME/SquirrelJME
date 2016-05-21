// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui.ipc;

/**
 * This interface contains the service ID for the client and server IPC based
 * display managers.
 *
 * @since 2016/05/21
 */
public interface DMServiceID
{
	/**
	 * {@squirreljme.serviceid 1 uidisplaymanager This is the service
	 * indentifier for the UIDisplayManager IPC. This service is used as a
	 * routing protocol so that processes within the kernel can access and
	 * create user interfaces and such across execution environments.}
	 */
	public static final int SERVICE_ID =
		1;
}

