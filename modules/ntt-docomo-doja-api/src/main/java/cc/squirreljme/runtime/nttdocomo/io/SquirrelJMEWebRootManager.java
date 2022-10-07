// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.io;

/**
 * This acts as a virtual web root HTTP server that is used for DoJa based
 * titles to access their web based resources and otherwise. Since these titles
 * themselves have content on a remote server end, this allows that to still
 * run accordingly.
 * 
 * This will keep HTTP state accordingly and also handle any potential CGI
 * requests for any files that should be served over CGI for example.
 * 
 * Software expects this to be HTTP compatible accordingly.
 *
 * @since 2022/10/07
 */
public class SquirrelJMEWebRootManager
{
}
