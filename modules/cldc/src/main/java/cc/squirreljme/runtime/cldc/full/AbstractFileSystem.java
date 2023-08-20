// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Set;

/**
 * Base file system to add additional methods needed by SquirrelJME.
 *
 * @since 2023/08/20
 */
@SquirrelJMEVendorApi
public abstract class AbstractFileSystem
	extends FileSystem
{
	/**
	 * Opens a channel to the file's data.
	 *
	 * @param __path The path of the file to open.
	 * @param __options The open options.
	 * @param __attribs The file attributes to open with.
	 * @return The channel to the open file.
	 * @throws IllegalArgumentException If the open options are not valid.
	 * @throws IOException On read/write errors.
	 * @throws SecurityException If opening the file is not permitted.
	 * @throws UnsupportedOperationException If the file system does not
	 * support opening the specific file.
	 * @since 2023/08/20
	 */
	public abstract FileChannel open(Path __path,
		Set<? extends OpenOption> __options, FileAttribute<?>... __attribs)
		throws IllegalArgumentException, IOException, SecurityException,
			UnsupportedOperationException;
}
