// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.mini;

/**
 * This class contains mapped debug information which is used later to
 * encode debugging information on minimization.
 *
 * @since 2019/04/26
 */
final class __MappedDebugInfo__
{
	/** Line table. */
	final short[] _lintable;
	
	/** Java operation table. */
	final byte[] _joptable;
	
	/** Java PC table. */
	final byte[] _jpctable;
	
	/**
	 * Initializes the debug information.
	 *
	 * @param __ln Line table.
	 * @param __jo Operation table.
	 * @param __jp Address table.
	 * @since 2019/04/26
	 */
	public __MappedDebugInfo__(short[] __ln, byte[] __jo, byte[] __jp)
	{
		this._lintable = (__ln == null ? new short[0] : __ln);
		this._joptable = (__jo == null ? new byte[0] : __jo);
		this._jpctable = (__jp == null ? new byte[0] : __jp);
	}
}

