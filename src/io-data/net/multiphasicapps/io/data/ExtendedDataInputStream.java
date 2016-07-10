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

import java.io.DataInput;
import java.io.InputStream;
import java.io.IOException;

/**
 * This is an extended input stream which is better suited for general binary
 * data file reading compared to the standard {@link DataInputStream}.
 *
 * @since 2016/07/10
 */
public class ExtendedDataInputStream
	extends InputStream
	implements DataInput
{
}

