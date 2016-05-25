// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar;

import java.nio.channels.SeekableByteChannel;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;

/**
 * This is the base class for any facility which needs to provide class units
 * as JAR files. This class must be extended and the sub-classes must implement
 * the required methods needed to locate {@link SeekableByteChannel}s.
 *
 * @since 2016/05/25
 */
public abstract class JarClassUnitProvider
	extends ClassUnitProvider
{
}

