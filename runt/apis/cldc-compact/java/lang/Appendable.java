// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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

