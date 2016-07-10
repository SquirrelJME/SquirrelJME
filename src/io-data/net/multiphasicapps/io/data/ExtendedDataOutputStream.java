// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.data;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an extended output stream which is better suited to writing
 * general binaries compared to the standard {@link DataOutputStream}.
 *
 * @since 2016/07/10
 */
public class ExtendedDataOutputStream
	extends OutputStream
	implements DataOutput
{
}

