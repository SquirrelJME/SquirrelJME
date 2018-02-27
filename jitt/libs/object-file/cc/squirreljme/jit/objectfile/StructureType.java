// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This interface is used to represent structures which may be used to store
 * information.
 *
 * @since 2018/02/26
 */
public interface StructureType
{
	/**
	 * The element which is at the previous structure entry.
	 *
	 * @return The previous structure entry.
	 * @since 2018/02/26
	 */
	public abstract StructureType previousStruct();
	
	/**
	 * Returns the used storage type.
	 *
	 * @return The storage type to use.
	 * @since 2018/02/26
	 */
	public abstract StorageType storageType();
}

