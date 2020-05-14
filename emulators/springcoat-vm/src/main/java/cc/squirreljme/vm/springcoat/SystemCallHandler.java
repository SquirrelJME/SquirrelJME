// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.AbstractSystemCallHandler;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.SystemCallError;
import cc.squirreljme.jvm.SystemCallException;
import cc.squirreljme.jvm.SystemCallIndex;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;

/**
 * This handles system calls within SpringCoat.
 *
 * @since 2020/03/13
 */
public final class SystemCallHandler
	extends AbstractSystemCallHandler
{
	/** The class for JVM function (un-pure handler). */
	private static final ClassName _JVM_FUNCTION_CLASS =
		new ClassName("cc/squirreljme/jvm/JVMFunction");
	
	/** name and type for unpure calls. */
	private static final MethodNameAndType _UNPURE_NAME_AND_TYPE =
		new MethodNameAndType("jvmSystemCall", "(SIIIIIIII)J");
	
	/**
	 * Dispatches the system call accordingly to either the un-pure handler
	 * within the VM or the pure handler.
	 *
	 * @param __thread The calling thread.
	 * @param __name The name of the method.
	 * @param __type The type of the method.
	 * @param __args The arguments for the call.
	 * @return The return value of the system call
	 * @since 2020/03/13
	 */
	public static Object dispatch(SpringThreadWorker __thread,
		MethodName __name, MethodDescriptor __type, Object... __args)
		throws NullPointerException
	{
		if (__thread == null || __name == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Basic system call parameters
		short sysCallId = ((Number)__args[0]).shortValue();
		boolean isPure = (__name.toString().indexOf('P') >= 0);
		
		// Dispatch the call
		long rv;
		switch (__args.length - 1)
		{
			case 0:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					0,
					0,
					0,
					0,
					0,
					0,
					0,
					0);
				break;
				
			case 1:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					0,
					0,
					0,
					0,
					0,
					0,
					0);
				break;
				
			case 2:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					0,
					0,
					0,
					0,
					0,
					0);
				break;
				
			case 3:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					((Number)__args[3]).intValue(),
					0,
					0,
					0,
					0,
					0);
				break;
				
			case 4:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					((Number)__args[3]).intValue(),
					((Number)__args[4]).intValue(),
					0,
					0,
					0,
					0);
				break;
				
			case 5:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					((Number)__args[3]).intValue(),
					((Number)__args[4]).intValue(),
					((Number)__args[5]).intValue(),
					0,
					0,
					0);
				break;
				
			case 6:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					((Number)__args[3]).intValue(),
					((Number)__args[4]).intValue(),
					((Number)__args[5]).intValue(),
					((Number)__args[6]).intValue(),
					0,
					0);
				break;
				
			case 7:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					((Number)__args[3]).intValue(),
					((Number)__args[4]).intValue(),
					((Number)__args[5]).intValue(),
					((Number)__args[6]).intValue(),
					((Number)__args[7]).intValue(),
					0);
				break;
				
			case 8:
				rv = SystemCallHandler.dispatch(__thread, isPure, sysCallId,
					((Number)__args[1]).intValue(),
					((Number)__args[2]).intValue(),
					((Number)__args[3]).intValue(),
					((Number)__args[4]).intValue(),
					((Number)__args[5]).intValue(),
					((Number)__args[6]).intValue(),
					((Number)__args[7]).intValue(),
					((Number)__args[8]).intValue());
				break;
			
			default:
				throw new SpringVirtualMachineException(
					"Invalid system call parameter count: " + __args.length);
		}
		
		// The return value type depends on our signature
		if (!__type.hasReturnValue())
			return null;
		else if (__type.returnValue().primitiveType() == PrimitiveType.INTEGER)
			return (int)rv;
		return rv;
	}
	
	/**
	 * Dispatches the system call accordingly to either the un-pure handler
	 * within the VM or the pure handler.
	 *
	 * @param __thread The calling thread.
	 * @param __pure Is this a pure system call?
	 * @param __sid The system call index.
	 * @param __a A.
	 * @param __b B.
	 * @param __c C.
	 * @param __d D.
	 * @param __e E.
	 * @param __f F.
	 * @param __g G.
	 * @param __h H.
	 * @return The result of the system call.
	 * @since 2020/03/14
	 */
	public static long dispatch(SpringThreadWorker __thread, boolean __pure,
		short __sid, int __a, int __b, int __c, int __d, int __e, int __f,
		int __g, int __h)
	{
		// Non-pure system calls are sent to the code within the JVM itself,
		// this way they can be patched or otherwise supported without needing
		// to be an actual part of the VM.
		if (!__pure)
			return ((Number)__thread.invokeMethod(true,
				SystemCallHandler._JVM_FUNCTION_CLASS,
				SystemCallHandler._UNPURE_NAME_AND_TYPE,
				(int)__sid, __a, __b, __c, __d, __e, __f, __g, __h))
				.longValue();
		
		// Use internal handler for system calls.
		return SystemCallHandler.handle(__thread,
			__sid, __a, __b, __c, __d,__e, __f, __g, __h);
	}
	
	/**
	 * Handles the system call logic.
	 *
	 * @param __thread The calling thread.
	 * @param __sid The system call index.
	 * @param __a A.
	 * @param __b B.
	 * @param __c C.
	 * @param __d D.
	 * @param __e E.
	 * @param __f F.
	 * @param __g G.
	 * @param __h H.
	 * @return The result of the system call.
	 * @since 2020/03/14
	 */
	private static long handle(SpringThreadWorker __thread, short __sid,
		int __a, int __b, int __c, int __d, int __e, int __f, int __g, int __h)
	{
		// The SID used for error access
		int eSid = (__sid < 0 || __sid >= SystemCallIndex.NUM_SYSCALLS ?
			0 : __sid);
		
		// We must always record the error state at the end of execution
		int error = SystemCallError.NO_ERROR;
		try
		{
			// Different system calls have different logic, but generally
			// they have simple functions.
			switch (__sid)
			{
				case SystemCallIndex.QUERY_INDEX:
					switch (__a)
					{
							// Supported system calls
						case SystemCallIndex.API_LEVEL:
						case SystemCallIndex.CHECK_EXEC_COMPATIBILITY:
						case SystemCallIndex.ERROR_GET:
						case SystemCallIndex.ERROR_SET:
						case SystemCallIndex.EXIT:
						case SystemCallIndex.FRAME_TASK_ID_GET:
						case SystemCallIndex.HW_THREAD:
						case SystemCallIndex.PD_OF_STDERR:
						case SystemCallIndex.QUERY_INDEX:
							return 1;
						
							// Not supported
						default:
							return 0;
					}
					
					// Current API level
				case SystemCallIndex.API_LEVEL:
					return Constants.API_LEVEL_2020_05_10;
					
					// ROM requests a check for compatibility
				case SystemCallIndex.CHECK_EXEC_COMPATIBILITY:
					if (__a >= Constants.API_LEVEL_2020_05_10)
						return 1;
					return 0;
					
					// Get error
				case SystemCallIndex.ERROR_GET:
					return __thread.thread._syscallerrors[(__a < 0 ||
						__a >= SystemCallIndex.NUM_SYSCALLS ? 0 : __a)];
				
					// Set error
				case SystemCallIndex.ERROR_SET:
					__thread.thread._syscallerrors[(__a < 0 ||
						__a >= SystemCallIndex.NUM_SYSCALLS ? 0 : __a)] = __b;
					return 0;
					
					// Exit the VM
				case SystemCallIndex.EXIT:
					__thread.machine.exit(__a);
					return 0;
					
					// Get current task ID
				case SystemCallIndex.FRAME_TASK_ID_GET:
					return __thread.thread.taskId();
					
					// Hardware thread access
				case SystemCallIndex.HW_THREAD:
					return __thread.machine.tasks.hardwareThreads.sysCall(
						__thread, __a, __b, __c, __d, __e, __f, __g, __h);
					
					// Pipe descriptor of standard output
				case SystemCallIndex.PD_OF_STDOUT:
					return 1;
					
					// Pipe descriptor of standard error
				case SystemCallIndex.PD_OF_STDERR:
					return 2;
					
					// Write byte to standard descriptor
				case SystemCallIndex.PD_WRITE_BYTE:
					switch (__a)
					{
						case 1: System.out.write(__b); break;
						case 2: System.err.write(__b); break;
						
						default:
							error = SystemCallError.PIPE_DESCRIPTOR_INVALID;
							return 0;
					}
					return 1;
				
					// System call not supported
				default:
					Debugging.debugNote("Un-handled sysCall#%d(%d, " +
						"%d, %d, %d, %d, %d, %d, %d)",
						__sid, __a, __b, __c, __d, __e, __f, __g, __h);
					
					error = SystemCallError.UNSUPPORTED_SYSTEM_CALL;
					return 0;
			}
		}
		
		// Possible this is thrown as well
		catch (SystemCallException e)
		{
			// For debugging
			e.printStackTrace();
			
			// Set error code
			error = e.code;
			__thread.thread._syscallerrors[eSid] = error;
			
			// Since an exception is thrown, just return zero
			return 0;
		}
		
		// Store the error state
		finally
		{
			__thread.thread._syscallerrors[eSid] = error;
		}
	}
	
	/* *
	 * Internal system call handling.
	 *
	 * @param __si System call index.
	 * @param __args Arguments.
	 * @return The result.
	 * @since 2019/05/23
	 * /
	public final long systemCall(short __si, int... __args)
	{
		// Make at least 8!
		if (__args == null)
			__args = new int[8];
		if (__args.length < 8)
			__args = Arrays.copyOf(__args, 8);
		
		// Error state for the last call of this type
		int[] errors = this.thread._syscallerrors;
		
		// Return value with error value, to set if any
		long rv;
		int err;
		
		// Depends on the system call type
		switch (__si)
		{
				// Check if system call is supported
			case SystemCallIndex.QUERY_INDEX:
				{
					err = 0;
					switch (__args[0])
					{
						case SystemCallIndex.API_LEVEL:
						case SystemCallIndex.CALL_STACK_HEIGHT:
						case SystemCallIndex.CALL_STACK_ITEM:
						case SystemCallIndex.ERROR_GET:
						case SystemCallIndex.ERROR_SET:
						case SystemCallIndex.EXIT:
						case SystemCallIndex.FATAL_TODO:
						case SystemCallIndex.GARBAGE_COLLECT:
						case SystemCallIndex.LOAD_STRING:
						case SystemCallIndex.PD_OF_STDERR:
						case SystemCallIndex.PD_OF_STDIN:
						case SystemCallIndex.PD_OF_STDOUT:
						case SystemCallIndex.PD_WRITE_BYTE:
						case SystemCallIndex.SLEEP:
						case SystemCallIndex.TIME_MILLI_WALL:
						case SystemCallIndex.TIME_NANO_MONO:
						case SystemCallIndex.VMI_MEM_FREE:
						case SystemCallIndex.VMI_MEM_MAX:
						case SystemCallIndex.VMI_MEM_USED:
							rv = 1;
							break;
						
						default:
							rv = 0;
							break;
					}
				}
				break;
				
				// Returns the height of the call stack
			case SystemCallIndex.CALL_STACK_HEIGHT:
				{
					rv = this.thread.frames().length;
					err = 0;
				}
				break;
				
				// Returns the given call stack item
			case SystemCallIndex.CALL_STACK_ITEM:
				{
					// Need to get all the stack frames first
					SpringThread.Frame[] frames = this.thread.frames();
					int numframes = frames.length;
					int curf = (numframes - __args[0]) - 1;
					
					// Out of range item
					if (curf < 0 || curf >= numframes)
					{
						rv = -1;
						err = SystemCallError.VALUE_OUT_OF_RANGE;
					}
					
					// Depends on the item
					else
					{
						// Reset
						rv = err = 0;
						
						// Get method we are in
						SpringMethod inmethod = frames[curf].method();
						
						// Depends on the item
						switch (__args[1])
						{
								// Class name
							case CallStackItem.CLASS_NAME:
								if (inmethod == null)
									err = SystemCallError.VALUE_OUT_OF_RANGE;
								else
									rv = this.uniqueStringId(
										inmethod.inClass().toString());
								break;

								// The method name.
							case CallStackItem.METHOD_NAME:
								if (inmethod == null)
									err = SystemCallError.VALUE_OUT_OF_RANGE;
								else
									rv = this.uniqueStringId(inmethod.
										nameAndType().name().toString());
								break;

								// The method type.
							case CallStackItem.METHOD_TYPE:
								if (inmethod == null)
									err = SystemCallError.VALUE_OUT_OF_RANGE;
								else
									rv = this.uniqueStringId(inmethod.
										nameAndType().type().toString());
								break;

								// The current file.
							case CallStackItem.SOURCE_FILE:
								if (inmethod == null)
									err = SystemCallError.VALUE_OUT_OF_RANGE;
								else
									rv = this.uniqueStringId(
										inmethod.inFile());
								break;

								// Source line.
							case CallStackItem.SOURCE_LINE:
								rv = frames[curf].lastExecutedPcSourceLine();
								break;

								// The PC address.
							case CallStackItem.PC_ADDRESS:
							case CallStackItem.JAVA_PC_ADDRESS:
								rv = frames[curf].lastExecutedPc();
								break;

							default:
								err = SystemCallError.VALUE_OUT_OF_RANGE;
								break;
						}
					}
				}
				break;
				
				// Get error
			case SystemCallIndex.ERROR_GET:
				{
					// If the ID is valid then a bad array access will be used
					int dx = __args[0];
					if (dx < 0 || dx >= SystemCallIndex.NUM_SYSCALLS)
						dx = SystemCallIndex.QUERY_INDEX;
					
					// Return the stored error code
					synchronized (errors)
					{
						rv = errors[dx];
					}
					
					// Always succeeds
					err = 0;
				}
				break;
				
				// Set error
			case SystemCallIndex.ERROR_SET:
				{
					// If the ID is valid then a bad array access will be used
					int dx = __args[0];
					if (dx < 0 || dx >= SystemCallIndex.NUM_SYSCALLS)
						dx = SystemCallIndex.QUERY_INDEX;
					
					// Return last error code, and set new one
					synchronized (errors)
					{
						rv = errors[dx];
						errors[dx] = __args[0];
					}
					
					// Always succeeds
					err = 0;
				}
				break;
				
				// Exit the VM
			case SystemCallIndex.EXIT:
				{
					// Tell everything to cleanup and exit
					this.thread.profiler.exitAll(System.nanoTime());
					this.machine.exit((Integer)__args[0]);
					
					rv = 0;
					err = 0;
				}
				break;
				
				// Fatal ToDo
			case SystemCallIndex.FATAL_TODO:
				// {@squirreljme.error BK33 Virtual machine code executed
				// a fatal Todo. (The To Do code)}
				rv = err = 0;
				throw new SpringVirtualMachineException("BK33 " + __args[1]);
				
				// Invoke the garbage collector
			case SystemCallIndex.GARBAGE_COLLECT:
				{
					Runtime.getRuntime().gc();
					
					rv = 0;
					err = 0;
				}
				break;
				
				// Loads a string
			case SystemCallIndex.LOAD_STRING:
				{
					rv = (__args[0] == 0 ? 0 :
						this.uniqueObjectToPointer((SpringObject)
						this.asVMObject(new ConstantValueString(
						this.uniqueString(__args[0])))));
					err = 0;
				}
				break;
			
				// Current wall clock milliseconds (low).
			case SystemCallIndex.TIME_MILLI_WALL:
				{
					rv = System.currentTimeMillis();
					err = 0;
				}
				break;

				// Current monotonic clock nanoseconds (low).
			case SystemCallIndex.TIME_NANO_MONO:
				{
					rv = System.nanoTime();
					err = 0;
				}
				break;
			
				// VM information: Memory free bytes
			case SystemCallIndex.VMI_MEM_FREE:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().freeMemory());
					err = 0;
				}
				break;
			
				// VM information: Memory used bytes
			case SystemCallIndex.VMI_MEM_USED:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().totalMemory());
					err = 0;
				}
				break;
			
				// VM information: Memory max bytes
			case SystemCallIndex.VMI_MEM_MAX:
				{
					rv = (int)Math.min(Integer.MAX_VALUE,
						Runtime.getRuntime().maxMemory());
					err = 0;
				}
				break;
				
				// API level
			case SystemCallIndex.API_LEVEL:
				{
					rv = ApiLevel.LEVEL_SQUIRRELJME_0_3_0_DEV;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard input
			case SystemCallIndex.PD_OF_STDIN:
				{
					rv = 0;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard output
			case SystemCallIndex.PD_OF_STDOUT:
				{
					rv = 1;
					err = 0;
				}
				break;
				
				// Pipe descriptor of standard error
			case SystemCallIndex.PD_OF_STDERR:
				{
					rv = 1;
					err = 0;
				}
				break;
				
				// Write single byte to PD
			case SystemCallIndex.PD_WRITE_BYTE:
				{
					// Depends on the stream
					int pd = __args[0];
					OutputStream os = (pd == 1 ? System.out :
						(pd == 2 ? System.err : null));
					
					// Write
					if (os != null)
					{
						try
						{
							os.write(__args[1]);
							
							// Okay
							rv = 1;
							err = 0;
						}
						
						// Failed
						catch (IOException e)
						{
							rv = -1;
							err = SystemCallError.PIPE_DESCRIPTOR_BAD_WRITE;
						}
					}
					
					// Failed
					else
					{
						rv = -1;
						err = SystemCallError.PIPE_DESCRIPTOR_INVALID;
					}
				}
				break;
				
				// Sleep
			case SystemCallIndex.SLEEP:
				try
				{
					Thread.sleep(__args[0], __args[1]);
					
					rv = 0;
					err = SystemCallError.NO_ERROR;
				}
				catch (InterruptedException e)
				{
					rv = 1;
					err = SystemCallError.INTERRUPTED;
				}
				break;
			
			default:
				// Returns no value but sets an error
				rv = -1;
				err = SystemCallError.UNSUPPORTED_SYSTEM_CALL;
				
				// If the ID is valid then a bad array access will be used
				if (__si < 0 || __si >= SystemCallIndex.NUM_SYSCALLS)
					__si = SystemCallIndex.QUERY_INDEX;
				break;
		}
		
		// Set error state as needed
		synchronized (errors)
		{
			errors[__si] = err;
		}
		
		// Use returning value
		return rv;
	}*/
}
