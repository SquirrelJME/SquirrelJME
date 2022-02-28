// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.brackets;

/**
 * This is a wrapper for software enabled JARs.
 *
 * @since 2022/02/27
 */
public interface SoftJarWrapper
{
	/**
	 * Opens the specifies resource within the JAR.
	 * 
	 * @param __rc The resource to open.
	 * @return The opened resource or {@code null} if it does not exist.
	 * @since 2022/202/27
	 */
	SoftJarResourceInputStream openResource(String __rc);
}
