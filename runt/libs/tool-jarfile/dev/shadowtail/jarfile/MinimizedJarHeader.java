// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.jarfile;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * This represents the header for a minimized Jar file.
 *
 * @since 2019/04/27
 */
public final class MinimizedJarHeader
{
	/** Magic number for the JAR. */
	public static final int MAGIC_NUMBER =
		0x00456570;
}

