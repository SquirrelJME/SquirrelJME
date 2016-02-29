// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.nio.file;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public interface DirectoryStream<T>
	extends Closeable, Iterable<T>
{
	public abstract Iterator<T> iterator();
	
	public static interface Filter<T>
	{
		public abstract boolean accept(T __a)
			throws IOException;
	}
}

