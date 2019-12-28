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
 * This contains the index of system calls.
 *
 * @since 2019/05/23
 */
public interface SystemCallIndex
{
	/**
	 * Checks if the system call is supported.
	 *
	 * @param 1 The system call index to query.
	 * @return Zero if not supported, otherwise a non-zero value.
	 */
	public static final byte QUERY_INDEX =
		0;
	
	/**
	 * Gets the last error code.
	 *
	 * This value that stores the error state is thread-local and it may be
	 * stored with a precision of at least 16-bits.
	 *
	 * If the system call index is not valid then it is assumed to be
	 * {@link #QUERY_INDEX}.
	 *
	 * @param 1 The system call index to query.
	 * @return The last error code, will be zero if the last command succeeded.
	 */
	public static final byte ERROR_GET =
		1;
	
	/**
	 * Sets the last error code.
	 *
	 * This value that stores the error state is thread-local and it may be
	 * stored with a precision of at least 16-bits.
	 *
	 * If the system call index is not valid then it is assumed to be
	 * {@link #QUERY_INDEX}.
	 *
	 * @param 1 The system call index to query.
	 * @param 2 The value to set error register to.
	 * @return Zero on success
	 */
	public static final byte ERROR_SET =
		2;
	
	/**
	 * Current wall clock milliseconds.
	 *
	 * @return The current milliseconds time.
	 */
	public static final byte TIME_MILLI_WALL =
		3;
	
	/**
	 * Reserved for future use.
	 */
	public static final byte RESERVED_FOUR =
		4;
	
	/**
	 * Current monotonic clock nanoseconds (low).
	 *
	 * @return The monotonic nanoseconds time.
	 */
	public static final byte TIME_NANO_MONO =
		5;
	
	/**
	 * Reserved for future use.
	 */
	public static final byte RESERVED_SIX =
		6;
	
	/**
	 * VM Information: Free memory in bytes.
	 *
	 * @return The free memory amount in bytes.
	 */
	public static final byte VMI_MEM_FREE =
		7;
	
	/**
	 * VM Information: Used memory in bytes.
	 *
	 * @return The used memory amount in bytes.
	 */
	public static final byte VMI_MEM_USED =
		8;
	
	/**
	 * VM Information: Max memory in bytes.
	 *
	 * @return The max memory amount in bytes.
	 */
	public static final byte VMI_MEM_MAX =
		9;
	
	/**
	 * Suggests that the garbage collector should run, note that this may be
	 * a deferred operation and might not be immediate.
	 *
	 * @return Generally zero although any other value could be returned.
	 */
	public static final byte GARBAGE_COLLECT =
		10;
	
	/**
	 * Exits the VM with the given exit code.
	 *
	 * @param 1 The exit code to exit the process with.
	 * @return This generally does not return, if it does then the error code
	 * will likely specify why this failed.
	 */
	public static final byte EXIT =
		11;
	
	/**
	 * The API Level of the VM, this has been deprecated since the current
	 * SquirrelJME API specified in these system calls better handles various
	 * features.
	 *
	 * @return The API level of the virtual machine.
	 */
	@Deprecated
	public static final byte API_LEVEL =
		12;
	
	/**
	 * The pipe descriptor for stdin.
	 *
	 * @return The pipe descriptor for standard input.
	 */
	public static final byte PD_OF_STDIN =
		13;
	
	/**
	 * The pipe descriptor for stdout.
	 *
	 * @return The pipe descriptor for standard output.
	 */
	public static final byte PD_OF_STDOUT =
		14;
	
	/**
	 * The pipe descriptor for stderr.
	 *
	 * @return The pipe descriptor for standard error.
	 */
	public static final byte PD_OF_STDERR =
		15;
	
	/**
	 * Pipe descriptor: Write single byte.
	 *
	 * @param 1 The pipe descriptor.
	 * @param 2 The value of the byte to write, only the lowest 8-bits is used.
	 * @return The number of bytes written to the output, if this returns
	 * a value lower than zero then it indicates an error.
	 */
	public static final byte PD_WRITE_BYTE =
		16;
	
	/**
	 * Bulk sets the memory inside of a region, this follows the same pattern
	 * as C's {@code memset()} operation.
	 *
	 * @param 1 The address to set.
	 * @param 2 The value to set the region with.
	 * @param 3 The number of bytes to set.
	 * @return The number of bytes actually written, if this is zero then
	 * it is likely the system call is not supported.
	 */
	public static final byte MEM_SET =
		17;
	
	/**
	 * Bulk sets the memory inside of a region writing full integer values at
	 * a time which is generally faster, this follows the same pattern as C's
	 * {@code memset()} operation.
	 *
	 * @param 1 The address to set.
	 * @param 2 The value to set the region with.
	 * @param 3 The number of bytes to set, the lower 2-bits ({@code 0x3}) will
	 * be masked off so the length is always a multiple of four.
	 * @return The number of bytes actually written, if this is zero then
	 * it is likely the system call is not supported.
	 */
	public static final byte MEM_SET_INT =
		18;
	
	/**
	 * Get the height of the call stack.
	 *
	 * @return The height of the call stack.
	 */
	public static final byte CALL_STACK_HEIGHT =
		19;
	
	/**
	 * Gets the specified call stack item.
	 *
	 * @param 1 The number of frames from the top of the call stack to get the
	 * items for, zero will be the top-most item.
	 * @param 2 The item to obtain as specified in {@link CallStackItem}.
	 * @return The value of the item, if it is undefined or not supported
	 * then zero will be returned.
	 */
	public static final byte CALL_STACK_ITEM =
		20;
	
	/**
	 * Returns the string of the given pointer.
	 *
	 * @param 1 The pointer to the modified UTF encoded string.
	 * @return An instance of {@link String}.
	 */
	public static final byte LOAD_STRING =
		21;
	
	/**
	 * Fatal ToDo hit.
	 *
	 * @param 1 The code to use for the To Do.
	 * @return This should not return unless it is not supported.
	 */
	public static final byte FATAL_TODO =
		22;
	
	/**
	 * This is used to indicate that the supervisor booted correctly and that
	 * execution control is normal.
	 *
	 * @return Generally zero as no value is intended to be returned.
	 */
	public static final byte SUPERVISOR_BOOT_OKAY =
		23;
	
	/**
	 * Get, set, or change a property of the framebuffer, the properties which
	 * are defined are specified in {@link FramebufferProperty}.
	 *
	 * @param 1 The frame buffer property to select.
	 * @param ... Undefined, this depends on the property selected.
	 * @return Whatever value the frame buffer property will return.
	 */
	public static final byte FRAMEBUFFER_PROPERTY =
		24;
	
	/**
	 * Returns the native byte order of the system the virtual machine is
	 * running on.
	 *
	 * @return Non-zero if little endian, otherwise zero will be big endian.
	 */
	public static final byte BYTE_ORDER_LITTLE =
		25;
	
	/**
	 * Returns the pointer to the option JAR data.
	 *
	 * @param 1 The option JAR slot to request.
	 * @return A pointer to the data or zero if there is no option JAR defined
	 * in the requested slot.
	 */
	public static final byte OPTION_JAR_DATA =
		26;
	
	/**
	 * Returns the size of the option JAR data.
	 *
	 * @param 1 The option JAR slot to request.
	 * @return The size of the specified option JAR or zero if there is no
	 * option JAR defined in the requested slot.
	 */
	public static final byte OPTION_JAR_SIZE =
		27;
	
	/**
	 * Loads the specified class.
	 *
	 * @param 1 The Modified UTF specifying the class name.
	 * @return The pointer to the loaded class info, will be zero on failure.
	 */
	public static final byte LOAD_CLASS_UTF =
		28;
	
	/**
	 * Loads the specified class.
	 *
	 * @param 1 A byte array encoded in UTF-8 which contains the class name.
	 * @return The pointer to the loaded class info, will be zero on failure.
	 */
	public static final byte LOAD_CLASS_BYTES =
		29;
	
	/**
	 * Sets the value of a supervisor property.
	 *
	 * Supervisor properties are local to a thread.
	 *
	 * Only the supervisor is allowed to set these properties.
	 *
	 * @param 1 The supervisor property to set.
	 * @param 2 The new value of the property.
	 * @return A non-zero value if this was successful.
	 */
	public static final byte SUPERVISOR_PROPERTY_SET =
		30;
	
	/**
	 * Gets the value of a supervisor property.
	 *
	 * Supervisor properties are local to a thread.
	 *
	 * @param 1 The supervisor property to get.
	 * @return The value of that property, error should be checked to ensure
	 * that it did not fail.
	 */
	public static final byte SUPERVISOR_PROPERTY_GET =
		31;
	
	/**
	 * Sets the task ID of the current thread frame.
	 *
	 * Only the supervisor is allowed to set tihus.
	 *
	 * @param 1 The task ID to set.
	 * @return A non-zero value if this was successful.
	 */
	public static final byte FRAME_TASK_ID_SET =
		32;
	
	/**
	 * Gets the value of a thread register.
	 *
	 * @return The value of the task ID.
	 */
	public static final byte FRAME_TASK_ID_GET =
		33;
	
	/**
	 * Perform a feedback operation.
	 *
	 * @param 1 The type of feedback to perform.
	 * @param 2 The duration of the feedback.
	 * @return Non-zero on success.
	 */
	public static final byte DEVICE_FEEDBACK =
		34;
	
	/**
	 * Sleep for the given number of nanoseconds.
	 *
	 * @param 1 The number of milliseconds to sleep for.
	 * @param 2 The number of nanoseconds to sleep for.
	 * @return Returns zero unless sleep was interrupted.
	 */
	public static final byte SLEEP =
		35;
	
	/**
	 * If the framebuffer is shared with the console, this tells the console
	 * printer to not send messages to the screen as it will corrupt the
	 * display on the screen.
	 */
	public static final byte SQUELCH_FB_CONSOLE =
		36;
	
	/**
	 * Perform IPC call.
	 *
	 * @param 1 The task to call, {@code 0} is the supervisor.
	 * @param 2 The IPC identifier which specifies which service this was
	 * associated with.
	 * @param ... Any arguments to the call.
	 * @return The value returned from the remote call.
	 */
	public static final byte IPC_CALL =
		37;
	
	/**
	 * The number of system calls that are defined in this run-time.
	 *
	 * One must NEVER utilize this value in a system call as it will have
	 * unintended consequences of requesting future API values.
	 */
	public static final byte NUM_SYSCALLS =
		38;
}

