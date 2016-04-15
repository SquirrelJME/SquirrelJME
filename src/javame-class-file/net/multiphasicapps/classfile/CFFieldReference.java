// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

import java.io.DataInputStream;
import java.io.IOException;
import net.multiphasicapps.descriptors.FieldSymbol;

/**
 * This represents a field reference.
 *
 * @since 2016/03/15
 */
public final class CFFieldReference
	extends CFMemberReference<FieldSymbol>
{
	/**
	 * Initializes the field reference.
	 *
	 * @param __icp The owning constant pool.
	 * @param __dis Data source.
	 * @throws IOException On read errors.
	 * @since 2016/03/15
	 */
	CFFieldReference(CFConstantPool __icp,
		DataInputStream __dis)
		throws IOException
	{
		super(__icp, __dis, FieldSymbol.class);
	}
}

