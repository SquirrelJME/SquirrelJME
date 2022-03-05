// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.jvm.manifest.JavaManifest;

/**
 * Represents a source for a manifest, if discovered.
 *
 * @see ManifestSourceType
 * @since 2022/03/04
 */
public final class ManifestSource
{
	/** Is the manifest missing? */
	public boolean isMissing;
	
	/** The actual manifest. */
	public JavaManifest manifest;
}
