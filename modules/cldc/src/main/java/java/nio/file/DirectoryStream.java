// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.runtime.cldc.annotation.Api;
import java.io.Closeable;
import java.io.IOException;

@Api
public interface DirectoryStream<T>
	extends Closeable, Iterable<T>
{
	@Api
	interface Filter<T>
	{
		@Api
		boolean accept(T __a)
			throws IOException;
	}
}

