// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This represents the stage of an installation.
 *
 * @since 2016/06/24
 */
public enum SuiteInstallStage
{
	/** Installation finished. */
	DONE,
	
	/** Downloading document body. */
	DOWNLOAD_BODY,
	
	/** Downloading data. */
	DOWNLOAD_DATA,
	
	/** Downloading descriptor. */
	DOWNLOAD_DESCRIPTOR,
	
	/** Storing suite data. */
	STORING,
	
	/** Verifying the download. */
	VERIFYING,
	
	/** End. */
	;
}

