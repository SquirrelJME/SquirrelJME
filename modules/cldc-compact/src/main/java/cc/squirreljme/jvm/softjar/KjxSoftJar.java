// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.softjar;

import cc.squirreljme.jvm.mle.brackets.SoftJarResourceInputStream;
import cc.squirreljme.jvm.mle.brackets.SoftJarWrapper;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This class represents an instance of a {@code .kjx} file.
 * 
 * The KJX format is simple and is in the following structure:
 *  - {@code uint8_t[3] magic}: Magic number.
 *  - {@code uint8_t jadStartPos}: Start position of the {@code .jad} file.
 *  - {@code uint8_t fileNameLen}: The length of the current KJX file name.
 *  - {@code uint8_t[fileNameLen] fileName}: The name of the current KJX file.
 *  - {@code uint16_t jadLen}: The length of the embedded JAD file.
 *  - {@code uint8_t jadNameLen}: The length of the JAD filename.
 *  - {@code uint8_t[jadNameLen] jadName}: The name of the JAD file.
 *  - {@code uint8_t[jadLen] jadContent}: The JAD contents.
 *  - {@code uint8_t[...rest...] jarData}: Remainder is the JAR data.
 *
 * @since 2022/02/27
 */
public class KjxSoftJar
	implements SoftJarWrapper
{
	/**
	 * {@inheritDoc}
	 * @since 2022/02/27
	 */
	@Override
	public SoftJarResourceInputStream openResource(String __rc)
	{
		throw Debugging.todo();
	}
}
