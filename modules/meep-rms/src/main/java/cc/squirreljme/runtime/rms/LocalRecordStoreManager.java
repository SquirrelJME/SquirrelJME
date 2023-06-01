// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.rms;

/**
 * This is a record store that uses local byte buffers to store data.
 * 
 * This will be used as a fall-back in the event a supported system is not
 * used or is available.
 *
 * @since 2023/02/16
 */
public class LocalRecordStoreManager
	extends RecordStoreManager
{
}
