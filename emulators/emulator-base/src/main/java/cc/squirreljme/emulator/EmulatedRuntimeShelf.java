// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;

/**
 * Emulated shelf for {@link RuntimeShelf}.
 *
 * @since 2021/08/08
 */
public class EmulatedRuntimeShelf
{
	/**
	 * Same as {@link RuntimeShelf#vmDescription(int)}.
	 *
	 * @param __type One of {@link VMDescriptionType}.
	 * @return The string for the given description or {@code null} if not
	 * set.
	 * @throws MLECallError If {@code __type} is not valid.
	 * @since 2020/06/17
	 */
	public static String vmDescription(int __type)
		throws MLECallError
	{
		switch (__type)
		{
			case VMDescriptionType.UNSPECIFIED:
				return null;
			
			case VMDescriptionType.VM_VERSION:
				return System.getProperty("java.vm.version");
			
			case VMDescriptionType.VM_NAME:
				return System.getProperty("java.vm.name");
			
			case VMDescriptionType.VM_VENDOR:
				return System.getProperty("java.vm.vendor");
			
			case VMDescriptionType.VM_EMAIL:
				return System.getProperty("java.vm.vendor.email");
			
			case VMDescriptionType.VM_URL:
				return System.getProperty("java.vm.vendor.url");
			
			case VMDescriptionType.EXECUTABLE_PATH:
				return null;
			
			case VMDescriptionType.OS_NAME:
				return System.getProperty("os.name");
			
			case VMDescriptionType.OS_VERSION:
				return System.getProperty("os.version");
			
			case VMDescriptionType.OS_ARCH:
				return System.getProperty("os.arch");
			
			case VMDescriptionType.VM_SECURITY_POLICY:
				return null;
				
			default:
				throw new MLECallError("Invalid " + __type);
		}
	}
}
