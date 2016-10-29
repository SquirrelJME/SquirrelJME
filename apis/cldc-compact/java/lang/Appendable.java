// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.lang;

import java.io.IOException;

public interface Appendable
{
	public abstract Appendable append(CharSequence __a)
		throws IOException;
	
	public abstract Appendable append(CharSequence __a, int __b, int __c)
		throws IOException;
	
	public abstract Appendable append(char __a)
		throws IOException;
}

