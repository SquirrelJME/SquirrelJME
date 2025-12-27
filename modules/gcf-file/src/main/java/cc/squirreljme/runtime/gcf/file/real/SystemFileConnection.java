// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf.file.real;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.gcf.file.AbstractFileConnection;
import java.nio.file.FileSystem;

/**
 * A connection to a real system filesystem.
 *
 * @since 2025/12/27
 */
@SquirrelJMEVendorApi
public class SystemFileConnection
	extends AbstractFileConnection
{
	@Override
	protected FileSystem attachedFileSystem()
	{
		throw Debugging.todo();
	}
}
