// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This interface contains the various error codes for all of the system calls.
 *
 * @since 2019/05/23
 */
public final class SystemCallError
{
	/** No error, or success. */
	public static final byte NO_ERROR =
		0;
	
	/** The system call is not supported. */
	public static final byte UNSUPPORTED_SYSTEM_CALL =
		-1;
	
	/** The pipe descriptor is not valid. */
	public static final byte PIPE_DESCRIPTOR_INVALID =
		-2;
	
	/** Write error when writing to the pipe. */
	public static final byte PIPE_DESCRIPTOR_BAD_WRITE =
		-3;
	
	/** Value out of range. */
	public static final byte VALUE_OUT_OF_RANGE =
		-4;
	
	/** No frame buffer exists. */
	public static final byte NO_FRAMEBUFFER =
		-5;
	
	/** Permission denied. */
	public static final byte PERMISSION_DENIED =
		-6;
	
	/** Interrupted. */
	public static final byte INTERRUPTED =
		-7;
	
	/** Unknown error. */
	public static final byte UNKNOWN =
		-8;
	
	/** End of file reached. */
	public static final byte END_OF_FILE =
		-9;
	
	/** Error with IPC Call. */
	public static final byte IPC_ERROR =
		-10;
	
	/**
	 * Not used.
	 *
	 * @since 2019/05/23
	 */
	private SystemCallError()
	{
	}
	
	/**
	 * Checks if an error was set, if it was an exception is thrown.
	 *
	 * @param __si The system call to check.
	 * @throws SystemCallException If there was an error.
	 * @since 2020/01/12
	 */
	public static final void checkError(short __si)
		throws SystemCallException
	{
		int code = SystemCallError.getError(__si);
		if (code != SystemCallError.NO_ERROR)
			throw new SystemCallException(__si, code);
	}
	
	/**
	 * Returns the error state.
	 *
	 * @param __si The system call index.
	 * @return The error, 0 will be on success.
	 * @since 2019/05/23
	 */
	public static final int getError(short __si)
	{
		return Assembly.sysCallV(SystemCallIndex.ERROR_GET, __si);
	}
	
	/**
	 * Converts the error to a string.
	 *
	 * @param __err The input error.
	 * @return The resulting string.
	 * @since 2020/01/12
	 */
	public static final String toString(int __err)
	{
		switch (__err)
		{
			case SystemCallError.NO_ERROR:					return "NoError";
			case SystemCallError.UNSUPPORTED_SYSTEM_CALL:	return "UnsupportedSystemCall";
			case SystemCallError.PIPE_DESCRIPTOR_INVALID:	return "PDInvalid";
			case SystemCallError.PIPE_DESCRIPTOR_BAD_WRITE:	return "PDBadWrite";
			case SystemCallError.VALUE_OUT_OF_RANGE:		return "ValueOutOfRange";
			case SystemCallError.NO_FRAMEBUFFER:			return "NoFramebuffer";
			case SystemCallError.PERMISSION_DENIED:			return "PermissionDenied";
			case SystemCallError.INTERRUPTED:				return "Interrupted";
			case SystemCallError.UNKNOWN:					return "Unknown";
			case SystemCallError.END_OF_FILE:				return "EndOfFile";
			case SystemCallError.IPC_ERROR:					return "IPCError";
			
				// Some Other ID?
			default:
				return "ERROR" + __err;
		}
	}
}

