// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This represents the stage of an installation.
 *
 * @since 2016/06/24
 */
@Api
public enum SuiteInstallStage
{
	/** Installation finished. */
	@Api
	DONE,
	
	/** Downloading document body. */
	@Api
	DOWNLOAD_BODY,
	
	/** Downloading data. */
	@Api
	DOWNLOAD_DATA,
	
	/** Downloading descriptor. */
	@Api
	DOWNLOAD_DESCRIPTOR,
	
	/** Storing suite data. */
	@Api
	STORING,
	
	/** Verifying the download. */
	@Api
	VERIFYING,
	
	/** End. */
	;
}

