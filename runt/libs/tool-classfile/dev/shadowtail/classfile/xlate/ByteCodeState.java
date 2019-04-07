// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the state of the byte code.
 *
 * @since 2019/04/06
 */
public final class ByteCodeState
{
	/** The current state of the stack. */
	public JavaStackState stack;
	
	/** The positions of all the stack information. */
	public Map<Integer, JavaStackState> stacks =
		new LinkedHashMap<>();
}

