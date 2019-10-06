/* ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// --------------------------------------------------------------------------*/

/**
 * SquirrelJME RatufaCoat Source.
 *
 * @since 2019/06/02
 */

#include "sjmerc.h"
#include "sjmecon.h"
#include "sjmebsqf.h"
#include "sjmevdef.h"

/** Sets the error code. */
void sjme_seterror(sjme_error* error, sjme_jint code, sjme_jint value)
{
	if (error != NULL)
	{
		error->code = code;
		error->value = value;
	}
}

/**
 * Decodes an integer value from operations which could be unaligned.
 *
 * @param vmem Virtual memory.
 * @param ptr The pointer to read from.
 * @param error Error flag.
 * @return The resulting read value.
 * @since 2019/06/16
 */
sjme_jint sjme_opdecodejint(sjme_vmem* vmem, sjme_vmemptr* ptr,
	sjme_error* error)
{
	sjme_jint rv;
	
	/* Read all values. */
	rv = (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF)) << 24;
	rv |= (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF)) << 16;
	rv |= (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF)) << 8;
	rv |= (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF));
	
	return rv;
}

/**
 * Decodes a short value from operations which could be unaligned.
 *
 * @param vmem Virtual memory.
 * @param ptr The pointer to read from.
 * @param error Error flag.
 * @return The resulting read value.
 * @since 2019/06/16
 */
sjme_jint sjme_opdecodejshort(sjme_vmem* vmem, sjme_vmemptr* ptr,
	sjme_error* error)
{
	sjme_jint rv;
	
	/* Read all values. */
	rv = (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF)) << 8;
	rv |= (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF));
	
	/* Sign extend? */
	if (rv & SJME_JINT_C(0x8000))
		rv |= SJME_JINT_C(0xFFFF0000);
	
	return rv;
}

/**
 * Decodes a variable unsigned int operation argument.
 *
 * @param vmem Virtual memory.
 * @param ptr The pointer to read from.
 * @param error Error flag.
 * @return The resulting decoded value.
 * @since 2019/06/09
 */
sjme_jint sjme_opdecodeui(sjme_vmem* vmem, sjme_vmemptr* ptr,
	sjme_error* error)
{
	sjme_jint rv;
	
	/* Read single byte value from pointer. */
	rv = (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
		SJME_JINT_C(0xFF));
	
	/* Encoded as a 15-bit value? */
	if ((rv & SJME_JINT_C(0x80)) != 0)
	{
		rv = (rv & SJME_JINT_C(0x7F)) << SJME_JINT_C(8);
		rv |= (sjme_vmmreadp(vmem, SJME_VMMTYPE_BYTE, ptr, error) &
			SJME_JINT_C(0xFF));
	}
	
	/* Use read value. */
	return rv;
}

/**
 * Decodes register from the virtual machine.
 *
 * @param vmem Virtual memory.
 * @param ptr The pointer to read from.
 * @param error Error flag.
 * @return The resulting register value.
 * @since 2019/06/25
 */
sjme_jint sjme_opdecodereg(sjme_vmem* vmem, sjme_vmemptr* ptr,
	sjme_error* error)
{
	sjme_jint rv;
	
	/* Decode register. */
	rv = sjme_opdecodeui(vmem, ptr, error);
	
	/* Keep within register bound. */
	if (rv < 0 || rv >= SJME_MAX_REGISTERS)
	{
		sjme_seterror(error, SJME_ERROR_REGISTEROVERFLOW, rv);
		
		return 0;
	}
	
	/* Return it. */
	return rv;
}

/**
 * Decodes a relative jump offset.
 *
 * @param vmem Virtual memory.
 * @param ptr The pointer to read from.
 * @param error Error flag.
 * @return The resulting relative jump.
 * @since 2019/06/13
 */
sjme_jint sjme_opdecodejmp(sjme_vmem* vmem, sjme_vmemptr* ptr,
	sjme_error* error)
{
	sjme_jint rv;
	
	/* Decode value. */
	rv = sjme_opdecodeui(vmem, ptr, error);
	
	/* Negative branch? */
	if ((rv & SJME_JINT_C(0x00004000)) != 0)
		return rv | SJME_JINT_C(0xFFFF8000);
	return rv;
}

/** Draws single character onto the console. */
void sjme_console_drawplate(sjme_jvm* jvm, sjme_jint x, sjme_jint y,
	sjme_jbyte ch, sjme_error* error)
{
	sjme_jint r, c, cv, i, fontw, fonth, xform;
	sjme_vmemptr sp;
	sjme_jbyte* mp;
	sjme_jbyte bits;
	sjme_jint bpp, pq, at, mask;
	
	/* Check. */
	if (jvm == NULL)
		return;
	
	/* Ignore if out of bounds. */
	if (x < 0 || y < 0 || x >= jvm->conw || y >= jvm->conh)
		return;
	
	/* Font dimensions. */
	fontw = sjme_font.charwidths[0];
	fonth = sjme_font.pixelheight;
	
	/* Normalize to screen space. */
	x = x * fontw;
	y = y * fonth;
	
	/* Character data to draw. */
	if (sjme_font.isvalidchar[ch] == 0)
		ch = 0;
	mp = &sjme_font.charbmp[((sjme_jint)ch) * fonth * sjme_font.bytesperscan];
	
	/* Drawing format for the data value? */
	bpp = jvm->fbinfo->bitsperpixel;
	switch (bpp)
	{
		case 1:
		case 2:
		case 4:
		case 8:
			xform = SJME_VMMTYPE_BYTE;
			mask = (SJME_JINT_C(1) << bpp) - 1;
			break;
			
		case 16:
			xform = SJME_VMMTYPE_SHORT;
			mask = SJME_JINT_C(0xFFFF);
			break;
		
		case 32:
			xform = SJME_VMMTYPE_INTEGER;
			mask = SJME_JINT_C(0xFFFFFFFF);
			break;
	}
	
	/* Draw rows. */
	for (r = 0; r < fonth; r++)
	{
		/* Determine screen position. */
		sp = jvm->framebuffer->fakeptr +
			((x * jvm->fbinfo->bitsperpixel) / 8) +
			((y + r) * (jvm->fbinfo->scanlenbytes));
		
		/* Clear pixel queue. */
		pq = 0;
		at = 0;
		
		/* Draw all pixel scans. */
		c = 0;
		for (cv = 0; cv < sjme_font.bytesperscan; cv++, mp++)
		{
			/* Get character bits */
			bits = *mp;
			
			/* Draw all of them. */
			for (i = 0; i < 8 && c < fontw; i++, c++)
			{
				/* Shift the queue up from the last run. */
				pq <<= bpp;
				
				/* Mask it if the color is set? */
				if ((bits & sjme_drawcharbitmask[i]) != 0)
					pq |= mask;
				
				/* Queued bits go up. */
				at += bpp;
				
				/* Only write when there is at least 8! */
				if (at >= 8)
				{
					/* Write. */
					sjme_vmmwritep(jvm->vmem, xform, &sp, pq, error);
					
					/* Cut down. */
					pq = (((pq & sjme_sh_umask[bpp])) >> bpp);
					at -= bpp;
				}
			}
		}
		
		/* Force draw any pixels left over. */
		if (at >= bpp)
			sjme_vmmwritep(jvm->vmem, xform, &sp, pq, error);
	}
}

/** Writes to the console screen and to the native method as well. */
sjme_jint sjme_console_pipewrite(sjme_jvm* jvm,
	sjme_jint (*writefunc)(sjme_jint b), sjme_jbyte* buf, sjme_jint off,
	sjme_jint len, sjme_error* error)
{
	sjme_jbyte b, donewline;
	sjme_jint i, code;
	
	/* There must be a JVM! */
	if (jvm == NULL)
		return -1;
	
	/* Write all the bytes to the output. */
	for (i = 0; i < len; i++, off++)
	{
		/* Read byte. */
		b = buf[off];
		
		/* Draw to the console in supervisor boot mode. */
		if (jvm->supervisorokay == 0 && jvm->fbinfo != NULL)
		{
			/* Carriage return? */
			donewline = 0;
			if (b == '\r')
				jvm->conx = 0;
			
			/* Newline? */
			else if (b == '\n')
				donewline = 1;
			
			/* Draw character? */
			else
			{
				/* Draw it. */
				sjme_console_drawplate(jvm, jvm->conx, jvm->cony, b, error);
				
				/* Move cursor up. */
				jvm->conx++;
				
				/* New line to print on? */
				if (jvm->conx >= jvm->conw)
					donewline = 1;
			}
			
			/* Doing a new line? */
			if (donewline != 0)
			{
				/* Move the cursor to the start of the next line. */
				jvm->conx = 0;
				jvm->cony++;
				
				/* Too much text on the screen? Move it up! */
				if (jvm->cony >= jvm->conh)
				{
					/* Move framebuffer up. */
					memmove(
						SJME_POINTER_OFFSET_LONG(jvm->fbinfo->pixels, 0),
						SJME_POINTER_OFFSET_LONG(jvm->fbinfo->pixels,
							sjme_font.pixelheight *
							(jvm->fbinfo->scanlenbytes)),
						(jvm->fbinfo->height - sjme_font.pixelheight) *
							(jvm->fbinfo->scanlenbytes));
					
					/* Wipe bytes at the bottom. */
					memset(
						SJME_POINTER_OFFSET_LONG(jvm->fbinfo->pixels,
							(jvm->fbinfo->height - sjme_font.pixelheight) *
								(jvm->fbinfo->scanlenbytes)), 0,
						sjme_font.pixelheight * (jvm->fbinfo->scanlenbytes));
					
					/* Move the cursor up one line. */
					jvm->cony--;
				}
			}
			
			/* Always flush in debug mode to force screen updates. */
			if (jvm->fbinfo->flush != NULL)
				jvm->fbinfo->flush();
		}
		
		/* Forward to pipe? */
		if (writefunc != NULL)
		{
			code = writefunc(b);
			if (code < 0)
				return (i == 0 ? code : i);
		}
	}
	
	/* Return written bytes. */
	return len;
}

/**
 * Handles system calls.
 *
 * @param jvm The JVM.
 * @param cpu The CPU.
 * @param error Error state.
 * @param callid The system call type.
 * @param args Arguments to the call.
 * @return The result of the call.
 * @since 2019/06/09
 */
sjme_jint sjme_syscall(sjme_jvm* jvm, sjme_cpu* cpu, sjme_error* error,
	sjme_jshort callid, sjme_jint* args)
{
	sjme_jint* syserr;
	sjme_jint ia, ib, ic;
	sjme_jbyte ba;
	sjme_vmemptr pa;
	sjme_cpustate* cpustate;
	
	/* Called wrong? */
	if (jvm == NULL || cpu == NULL || args == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return 0;
	}
	
	/* Start here. */
	cpustate = &cpu->state;
	
	/* Calculate index to set for system call errors. */
	syserr = ((callid < 0 || callid >= SJME_SYSCALL_NUM_SYSCALLS) ?
		&cpu->syscallerr[SJME_SYSCALL_QUERY_INDEX] : &cpu->syscallerr[callid]);
	
	/* Depends on the system call. */
	switch (callid)
	{
			/* Query support for system call. */
		case SJME_SYSCALL_QUERY_INDEX:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			switch (args[0])
			{
				case SJME_SYSCALL_BYTE_ORDER_LITTLE:
				case SJME_SYSCALL_CALL_STACK_HEIGHT:
				case SJME_SYSCALL_CALL_STACK_ITEM:
				case SJME_SYSCALL_ERROR_GET:
				case SJME_SYSCALL_ERROR_SET:
				case SJME_SYSCALL_FRAMEBUFFER_PROPERTY:
				case SJME_SYSCALL_TIME_HI_MILLI_WALL:
				case SJME_SYSCALL_TIME_HI_NANO_MONO:
				case SJME_SYSCALL_TIME_LO_MILLI_WALL:
				case SJME_SYSCALL_TIME_LO_NANO_MONO:
				case SJME_SYSCALL_MEM_SET:
				case SJME_SYSCALL_MEM_SET_INT:
				case SJME_SYSCALL_OPTION_JAR_DATA:
				case SJME_SYSCALL_OPTION_JAR_SIZE:
				case SJME_SYSCALL_PD_OF_STDERR:
				case SJME_SYSCALL_PD_OF_STDOUT:
				case SJME_SYSCALL_PD_WRITE_BYTE:
				case SJME_SYSCALL_SUPERVISOR_BOOT_OKAY:
				case SJME_SYSCALL_SUPERVISOR_PROPERTY_GET:
				case SJME_SYSCALL_SUPERVISOR_PROPERTY_SET:
				case SJME_SYSCALL_FRAME_TASK_ID_GET:
				case SJME_SYSCALL_FRAME_TASK_ID_SET:
					return SJME_JINT_C(1);
			}
			return SJME_JINT_C(0);
			
			/* Is the byte order little endian? */
		case SJME_SYSCALL_BYTE_ORDER_LITTLE:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
#if defined(SJME_LITTLE_ENDIAN)
			return 1;
#else
			return 0;
#endif
			
			/* Height of the call stack. */
		case SJME_SYSCALL_CALL_STACK_HEIGHT:
			/* Count trace depth. */
			ia = 0;
			while (cpustate != NULL)
			{
				/* Increase the count. */
				ia++;
				
				/* Go to deeper depth. */
				cpustate = cpustate->parent;
			}
			
			/* Does not generate errors. */
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return ia;
			
			/* Item within the call stack. */
		case SJME_SYSCALL_CALL_STACK_ITEM:
			/* Find the CPU frame to use. */
			ia = args[0];
			while (ia > 0)
			{
				/* End of CPU? */
				if (cpustate == NULL)
				{
					*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
					return 0;
				}
				
				/* Drop down. */
				cpustate = cpustate->parent;
				ia--;
			}
			
			/* Depends on the requested item.*/
			switch (args[1])
			{
					/* The class name. */
				case SJME_CALLSTACKITEM_CLASS_NAME:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugclassname;
					
					/* The method name. */
				case SJME_CALLSTACKITEM_METHOD_NAME:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugmethodname;
					
					/* The method type. */
				case SJME_CALLSTACKITEM_METHOD_TYPE:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugmethodtype;
					
					/* Source file. */
				case SJME_CALLSTACKITEM_SOURCE_FILE:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugsourcefile;
					
					/* Source line. */
				case SJME_CALLSTACKITEM_SOURCE_LINE:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugline;
					
					/* The PC address. */
				case SJME_CALLSTACKITEM_PC_ADDRESS:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->pc;
					
					/* Java operation. */
				case SJME_CALLSTACKITEM_JAVA_OPERATION:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugjop;
					
					/* Java PC address. */
				case SJME_CALLSTACKITEM_JAVA_PC_ADDRESS:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->debugjpc;
					
					/* Current Task ID. */
				case SJME_CALLSTACKITEM_TASK_ID:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return cpustate->taskid;
					
					/* Unknown. */
				default:
					*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
					return 0;
			}
			return 0;
			
			/* Get error state. */
		case SJME_SYSCALL_ERROR_GET:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			
			ia = args[0];
			if (ia < 0 || ia >= SJME_SYSCALL_NUM_SYSCALLS)
				ia = SJME_SYSCALL_QUERY_INDEX;
			
			return cpu->syscallerr[ia];
			
			/* Set error state, return old one. */
		case SJME_SYSCALL_ERROR_SET:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			
			ia = args[0];
			if (ia < 0 || ia >= SJME_SYSCALL_NUM_SYSCALLS)
				ia = SJME_SYSCALL_QUERY_INDEX;
			
			ib = cpu->syscallerr[ia];
			cpu->syscallerr[ia] = args[1];
			return ib;
			
			/* Gets/sets property of the framebuffer. */
		case SJME_SYSCALL_FRAMEBUFFER_PROPERTY:
			/* No framebuffer is defined? */
			if (jvm->fbinfo == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_NO_FRAMEBUFFER;
				return 0;
			}
			
			/* Depends on the property. */
			switch (args[0])
			{
					/* Framebuffer address. */
				case SJME_FRAMEBUFFER_PROPERTY_ADDRESS:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->framebuffer->fakeptr;
					
					/* Width of the framebuffer. */
				case SJME_FRAMEBUFFER_PROPERTY_WIDTH:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->width;
					
					/* Height of the framebuffer. */
				case SJME_FRAMEBUFFER_PROPERTY_HEIGHT:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->height;
					
					/* Scanline length of the framebuffer. */
				case SJME_FRAMEBUFFER_PROPERTY_SCANLEN:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->scanlen;
					
					/* Flush the framebuffer. */
				case SJME_FRAMEBUFFER_PROPERTY_FLUSH:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					if (jvm->fbinfo->flush != NULL)
						jvm->fbinfo->flush();
					return 0;
					
					/* Frame-buffer format. */
				case SJME_FRAMEBUFFER_PROPERTY_FORMAT:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->format;
					
					/* Scanline length in bytes. */
				case SJME_FRAMEBUFFER_PROPERTY_SCANLEN_BYTES:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->scanlenbytes;
					
					/* Bytes per pixel. */
				case SJME_FRAMEBUFFER_PROPERTY_BYTES_PER_PIXEL:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->bitsperpixel / 8;
					
					/* The number of pixels. */
				case SJME_FRAMEBUFFER_PROPERTY_NUM_PIXELS:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->numpixels;
					
					/* Bits per pixels. */
				case SJME_FRAMEBUFFER_PROPERTY_BITS_PER_PIXEL:
					*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
					return jvm->fbinfo->bitsperpixel;
				
					/* Unknown property, but there is a framebuffer. */
				default:
					*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
					return 0;
			}
			return 0;
			
			/* Get Task ID. */
		case SJME_SYSCALL_FRAME_TASK_ID_GET:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return cpustate->taskid;
			
			/* Set Task ID. */
		case SJME_SYSCALL_FRAME_TASK_ID_SET:
			cpustate->taskid = args[0];
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return SJME_JINT_C(1);
			
			/* Set memory to byte value. */
		case SJME_SYSCALL_MEM_SET:
			/* Get address to wipe. */
			pa = args[0];
			
			/* The value to store. */
			ic = args[1] & SJME_JINT_C(0xFF);
			
			/* Wipe these values! */
			ib = args[2];
			for (ia = 0; ia < ib; ia++)
				sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_BYTE, pa, ia, ic, error);
			
			/* Is okay. */
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return ib;
		
			/* Set memory in integer values. */
		case SJME_SYSCALL_MEM_SET_INT:
			/* Get address to wipe. */
			pa = args[0];
			
			/* The value to store, is full integer. */
			ic = args[1];
			
			/* Wipe these values! */
			ib = args[2] & ~SJME_JINT_C(3);
			for (ia = 0; ia < ib; ia += 4)
				sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_INTEGER, pa, ia, ic,
					error);
			
			/* Is okay. */
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return ib;
			
			/* Return pointer to the OptionJAR. */
		case SJME_SYSCALL_OPTION_JAR_DATA:
			if (jvm->optionjar == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
				return SJME_JINT_C(0);
			}
			
			/* Is available. */
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return jvm->optionjar->fakeptr;
		
			/* Return size of the OptionJAR. */
		case SJME_SYSCALL_OPTION_JAR_SIZE:
			if (jvm->optionjar == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
				return SJME_JINT_C(0);
			}
			
			/* Is available. */
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return jvm->optionjar->size;
			
			/* Pipe descriptor of standard error. */
		case SJME_SYSCALL_PD_OF_STDERR:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return SJME_JINT_C(SJME_PIPE_FD_STDERR);
			
			/* Pipe descriptor of standard output. */
		case SJME_SYSCALL_PD_OF_STDOUT:
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return SJME_JINT_C(SJME_PIPE_FD_STDOUT);
			
			/* Write single byte to a stream. */
		case SJME_SYSCALL_PD_WRITE_BYTE:
			ia = SJME_JINT_C(-1);
			
			/* The byte to write. */
			ba = (sjme_jbyte)args[1];
			
			/* Depends on the pipe target. */
			switch (args[0])
			{
					/* Standard output. */
				case SJME_PIPE_FD_STDOUT:
					ia = sjme_console_pipewrite(jvm,
						(jvm->nativefuncs != NULL &&
						jvm->nativefuncs->stdout_write != NULL ?
						jvm->nativefuncs->stdout_write : NULL), &ba, 0, 1,
						error);
					break;
				
					/* Standard error. */
				case SJME_PIPE_FD_STDERR:
					ia = sjme_console_pipewrite(jvm,
						(jvm->nativefuncs != NULL &&
						jvm->nativefuncs->stderr_write != NULL ?
						jvm->nativefuncs->stderr_write : NULL), &ba, 0, 1,
						error);
					break;
					
					/* Unknown descriptor. */
				default:
					*syserr = SJME_SYSCALL_ERROR_PIPE_DESCRIPTOR_INVALID;
					return SJME_JINT_C(-1);
			}
			
			/* Write error? */
			if (ia < 0)
			{
				*syserr = SJME_SYSCALL_ERROR_PIPE_DESCRIPTOR_INVALID;
				return SJME_JINT_C(-1);
			}
			
			/* Success. */
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return SJME_JINT_C(1);
			
			/* The supervisor booted okay! */
		case SJME_SYSCALL_SUPERVISOR_BOOT_OKAY:
			jvm->supervisorokay = 1;
			*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
			return SJME_JINT_C(0);
			
			/* Get supervisor property. */
		case SJME_SYSCALL_SUPERVISOR_PROPERTY_GET:
			ia = args[0];
			if (ia < 0 || ia >= SJME_SUPERPROP_NUM_PROPERTIES)
			{
				*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
				return SJME_JINT_C(0);
			}
			else
			{
				*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
				return cpu->supervisorprops[ia];
			}
			
			/* Set supervisor property. */
		case SJME_SYSCALL_SUPERVISOR_PROPERTY_SET:
			ia = args[0];
			if (ia < 0 || ia >= SJME_SUPERPROP_NUM_PROPERTIES)
			{
				*syserr = SJME_SYSCALL_ERROR_VALUE_OUT_OF_RANGE;
				return SJME_JINT_C(0);
			}
			else
			{
				*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
				return cpu->supervisorprops[ia] = args[1];
			}
			
			/* Returns the high millisecond wall clock. */
		case SJME_SYSCALL_TIME_HI_MILLI_WALL:
			if (jvm->nativefuncs == NULL ||
				jvm->nativefuncs->millitime == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_UNSUPPORTED_SYSTEM_CALL;
				return SJME_JINT_C(0);
			}
			else
			{
				*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
				jvm->nativefuncs->millitime(&ia);
				return ia;
			}
			
			/* Returns the low nanosecond wall clock. */
		case SJME_SYSCALL_TIME_HI_NANO_MONO:
			if (jvm->nativefuncs == NULL ||
				jvm->nativefuncs->nanotime == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_UNSUPPORTED_SYSTEM_CALL;
				return SJME_JINT_C(0);
			}
			else
			{
				*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
				jvm->nativefuncs->nanotime(&ia);
				return ia;
			}
			
			/* Returns the low millisecond wall clock. */
		case SJME_SYSCALL_TIME_LO_MILLI_WALL:
			if (jvm->nativefuncs == NULL ||
				jvm->nativefuncs->millitime == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_UNSUPPORTED_SYSTEM_CALL;
				return SJME_JINT_C(0);
			}
			else
			{
				*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
				return jvm->nativefuncs->millitime(&ia);
			}
		
			/* Returns the low nanosecond monotonic clock. */
		case SJME_SYSCALL_TIME_LO_NANO_MONO:
			if (jvm->nativefuncs == NULL ||
				jvm->nativefuncs->nanotime == NULL)
			{
				*syserr = SJME_SYSCALL_ERROR_UNSUPPORTED_SYSTEM_CALL;
				return SJME_JINT_C(0);
			}
			else
			{
				*syserr = SJME_SYSCALL_ERROR_NO_ERROR;
				return jvm->nativefuncs->nanotime(&ia);
			}
		
			/* Unknown or unsupported system call. */
		default:
			*syserr = SJME_SYSCALL_ERROR_UNSUPPORTED_SYSTEM_CALL;
			return SJME_JINT_C(0);
	}
}

/** Divides two numbers. */
sjme_jint_div sjme_div(sjme_jint anum, sjme_jint aden)
{
	/* From Wikipedia (http://en.wikipedia.org/wiki/Division_%28digital%29) */
	/* if D == 0 then throw DivisionByZeroException end*/
	/* Q := 0 # initialize quotient and remainder to Zero  */
	/* R := 0                                              */
	/* for i = n-1...0 do  # " where n is no of bits "     */
	/*   R := R << 1       # left-shift R by 1 bit         */
	/*   R(0) := N(i)      # set the least-significant bit */
	/*              # of R equal to bit i of the numerator */
	/*   if R >= D then                                    */
	/*     R = R - D                                       */
	/*     Q(i) := 1                                       */
	/*   end                                               */
	/* end                                                 */
	sjme_jint_div rv = {0, 0};
	struct
	{
		sjme_juint quot;
		sjme_juint rem;
	} interm = {0, 0};
	sjme_juint i;
	sjme_jbyte isneg;
	
	/* Disallow division by zero */
	if (aden == 0)
		return rv;
	
	/* Negative? */
	isneg = 0;
	if ((anum < 0 && aden >= 0) || (anum >= 0 && aden < 0))
		isneg |= 1;
	
	/* Force Positive */
	anum = (anum < 0 ? -anum : anum);
	aden = (aden < 0 ? -aden : aden);
	
	/* Perform Math */
	for (i = SJME_JUINT_C(31);; i--)
	{
		interm.rem <<= SJME_JUINT_C(1);
		interm.rem &= SJME_JUINT_C(0xFFFFFFFE);
		interm.rem |= (((sjme_juint)anum) >> i) & SJME_JUINT_C(1);
		
		if (interm.rem >= (sjme_juint)aden)
		{
			interm.rem -= (sjme_juint)aden;
			interm.quot |= (SJME_JUINT_C(1) << i);
		}
		
		if (i == 0)
			break;
	}
	
	/* Restore Integers */
	rv.quot = interm.quot;
	rv.rem = interm.rem;
	
	/* Make Negative */
	if (isneg & 1)
		rv.quot = -rv.quot;
	
	/* Return */
	return rv;
}

/**
 * Executes single CPU state.
 *
 * @param jvm JVM state.
 * @param cpu CPU state.
 * @param error Execution error.
 * @param cycles The number of cycles to execute, a negative value means
 * forever.
 * @return The number of remaining cycles.
 * @since 2019/06/08
 */
sjme_jint sjme_cpuexec(sjme_jvm* jvm, sjme_cpu* cpu, sjme_error* error,
	sjme_jint cycles)
{
	sjme_jint op, enc;
	sjme_vmemptr nextpc;
	sjme_vmemptr tempp;
	sjme_jint* r;
	sjme_jint ia, ib, ic, id, ie;
	sjme_cpustate* oldcpu;
	
	/* Invalid argument? */
	if (jvm == NULL || cpu == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return cycles;
	}
	
	/* Quick register access. */
	r = cpu->state.r;
	
	/* Near-Infinite execution loop. */
	for (;;)
	{
		/* Check if we ran out of cycles. */
		if (cycles >= 0)
		{
			if (cycles == 0)
				break;
			if ((--cycles) <= 0)
				break;
		}
		
		/* Increase total instruction count. */
		jvm->totalinstructions++;
		
		/* The zero register always must be zero. */
		r[0] = 0;
		
		/* Seed next PC address. */
		nextpc = cpu->state.pc;
		
		/* Read operation and determine encoding. */
		op = (sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_BYTE, &nextpc, error) &
			SJME_JINT_C(0xFF));
		enc = ((op >= SJME_ENC_SPECIAL_A) ? op : (op & SJME_ENC_MASK));
		
		/* Temporary debug. */
#if defined(SJME_DEBUG)
		fprintf(stderr,
			"ti=%d tk%d pc=%p op=%X cl=%s mn=%s mt=%s ln=%d jo=%x ja=%d\n",
			jvm->totalinstructions,
			cpu->state.taskid,
			cpu->state.pc,
			(unsigned int)op,
			sjme_vmmresolve(jvm->vmem, cpu->state.debugclassname, 2, NULL),
			sjme_vmmresolve(jvm->vmem, cpu->state.debugmethodname, 2, NULL),
			sjme_vmmresolve(jvm->vmem, cpu->state.debugmethodtype, 2, NULL),
			(int)cpu->state.debugline,
			(unsigned int)cpu->state.debugjop,
			(int)cpu->state.debugjpc);
#endif
		
		/* Depends on the operation. */
		switch (enc)
		{
				/* Compare two register values. */
			case SJME_ENC_IF_ICMP:
				{
					/* Values to compare. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					ib = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Target PC address. */
					ic = sjme_opdecodejmp(jvm->vmem, &nextpc, error);
					tempp = cpu->state.pc + ic;
					
					/* Check depends. */
					ic = 0;
					switch (op & SJME_ENC_COMPARE_MASK)
					{
						case SJME_COMPARETYPE_EQUALS:
							if (ia == ib)
								ic = 1;
							break;
							
						case SJME_COMPARETYPE_NOT_EQUALS:
							if (ia != ib)
								ic = 1;
							break;
							
						case SJME_COMPARETYPE_LESS_THAN:
							if (ia < ib)
								ic = 1;
							break;
							
						case SJME_COMPARETYPE_LESS_THAN_OR_EQUALS:
							if (ia <= ib)
								ic = 1;
							break;
							
						case SJME_COMPARETYPE_GREATER_THAN:
							if (ia > ib)
								ic = 1;
							break;
							
						case SJME_COMPARETYPE_GREATER_THAN_OR_EQUALS:
							if (ia >= ib)
								ic = 1;
							break;
							
						case SJME_COMPARETYPE_TRUE:
							ic = 1;
							break;
							
						case SJME_COMPARETYPE_FALSE:
							ic = 0;
							break;
					}
					
					/* Branch success? */
					if (ic != 0)
						nextpc = tempp;
				}
				break;
				
				/* Math. */
			case SJME_ENC_MATH_REG_INT:
			case SJME_ENC_MATH_CONST_INT:
				{
					/* A Value. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* B value. */
					if (enc == SJME_ENC_MATH_CONST_INT)
						ib = sjme_opdecodejint(jvm->vmem, &nextpc, error);
					else
						ib = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Perform the math. */
					switch (op & SJME_ENC_MATH_MASK)
					{
						case SJME_MATH_ADD:
							ic = ia + ib;
							break;
							
						case SJME_MATH_SUB:
							ic = ia - ib;
							break;
							
						case SJME_MATH_MUL:
							ic = ia * ib;
							break;
							
						case SJME_MATH_DIV:
							ic = sjme_div(ia, ib).quot;
							break;
							
						case SJME_MATH_REM:
							ic = sjme_div(ia, ib).rem;
							break;
							
						case SJME_MATH_NEG:
							ic = -ia;
							break;
							
						case SJME_MATH_SHL:
							/* Shift is truncated. */
							ib = (ib & SJME_JINT_C(0x1F));
							
							/* Shifting values off the type is undefined, */
							/* so only keep the part of the value which is */
							/* not shifted off! */
							if (ib == 0)
								ic = ia;
							else
								ic = ((ia & sjme_sh_lmask[ib]) << ib);
							break;
							
						case SJME_MATH_SHR:
						case SJME_MATH_USHR:
							/* Shift is truncated. */
							ib = (ib & SJME_JINT_C(0x1F));
							
							/* Shifting values off the type is undefined, */
							/* so only keep the part of the value which is */
							/* not shifted off! */
							if (ib == 0)
								ic = ia;
							else
								ic = (((ia & sjme_sh_umask[ib])) >> ib);
							
							/* Mask in or mask out the dragged sign bit. */
							if (((ia & SJME_JINT_C(0x80000000)) != 0) &&
								((op & SJME_ENC_MATH_MASK) == SJME_MATH_SHR))
								ic |= sjme_sh_umask[ib];
							else
								ic &= sjme_sh_lmask[31 - ib];
							break;
							
						case SJME_MATH_AND:
							ic = ia & ib;
							break;
							
						case SJME_MATH_OR:
							ic = ia | ib;
							break;
							
						case SJME_MATH_XOR:
							ic = ia ^ ib;
							break;
							
						case SJME_MATH_SIGNX8:
							if (ia & SJME_JINT_C(0x80))
								ic = (ia | SJME_JINT_C(0xFFFFFF00));
							else
								ic = (ia & SJME_JINT_C(0x000000FF));
							break;
							
						case SJME_MATH_SIGNX16:
							if (ia & SJME_JINT_C(0x8000))
								ic = (ia | SJME_JINT_C(0xFFFF0000));
							else
								ic = (ia & SJME_JINT_C(0x0000FFFF));
							break;
						
						case SJME_MATH_CMPL:
						case SJME_MATH_CMPG:
							ic = (ia < ib ? SJME_JINT_C(-1) :
								(ia > ib ? SJME_JINT_C(1) : SJME_JINT_C(0)));
							break;
					}
					
					/* Store result. */
					r[sjme_opdecodereg(jvm->vmem, &nextpc, error)] = ic;
				}
				break;
				
				/* Memory (native byte order). */
			case SJME_ENC_MEMORY_OFF_REG:
			case SJME_ENC_MEMORY_OFF_ICONST:
			case SJME_ENC_MEMORY_OFF_REG_JAVA:
			case SJME_ENC_MEMORY_OFF_ICONST_JAVA:
				{
					/* Destination/source register. */
					ic = sjme_opdecodereg(jvm->vmem, &nextpc, error);
					
					/* The address and offset to access. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					if (enc == SJME_ENC_MEMORY_OFF_ICONST ||
						enc == SJME_ENC_MEMORY_OFF_ICONST_JAVA)
						ib = sjme_opdecodejint(jvm->vmem, &nextpc, error);
					else
						ib = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					tempp = ia;
					
					/* Java types? */
					id = 0;
					if (enc == SJME_ENC_MEMORY_OFF_REG_JAVA ||
						enc == SJME_ENC_MEMORY_OFF_ICONST_JAVA)
						switch (op & SJME_MEM_DATATYPE_MASK)
						{
							case SJME_DATATYPE_BYTE:
								id = SJME_VMMTYPE_BYTE;
								break;
							
							case SJME_DATATYPE_CHARACTER:
							case SJME_DATATYPE_SHORT:
								id = SJME_VMMTYPE_JAVASHORT;
								break;
							
							case SJME_DATATYPE_OBJECT:
							case SJME_DATATYPE_INTEGER:
							case SJME_DATATYPE_FLOAT:
								id = SJME_VMMTYPE_JAVAINTEGER;
								break;
						}
					
					/* Native types? */
					else
						switch (op & SJME_MEM_DATATYPE_MASK)
						{
							case SJME_DATATYPE_BYTE:
								id = SJME_VMMTYPE_BYTE;
								break;
							
							case SJME_DATATYPE_CHARACTER:
							case SJME_DATATYPE_SHORT:
								id = SJME_VMMTYPE_SHORT;
								break;
							
							case SJME_DATATYPE_OBJECT:
							case SJME_DATATYPE_INTEGER:
							case SJME_DATATYPE_FLOAT:
								id = SJME_VMMTYPE_INTEGER;
								break;
						}
						
					/* Load value */
					if ((op & SJME_MEM_LOAD_MASK) != 0)
					{
						/* Read. */
						r[ic] = sjme_vmmread(jvm->vmem, id, tempp, ib, error);
						
						/* Mask character? */
						if ((op & SJME_MEM_DATATYPE_MASK) ==
							SJME_DATATYPE_CHARACTER)
							r[ic] = r[ic] & SJME_JINT_C(0xFFFF);
							
#if defined(SJME_DEBUG)
						fprintf(stderr, "r[%d] = *(%08x + %d) = %d/%08x\n",
							(int)ic, (int)tempp, (int)ib,
							(int)r[ic], (int)r[ic]);
#endif
					}
					
					/* Store value */
					else
					{
						sjme_vmmwrite(jvm->vmem, id, tempp, ib, r[ic], error);
						
#if defined(SJME_DEBUG)
						fprintf(stderr, "*(%08x + %d) = r[%d] = %d/%08x\n",
							(int)tempp, (int)ib, (int)ic,
							(int)r[ic], (int)r[ic]);
#endif
					}
				}
				break;
				
				/* Atomic compare, get, and set. */
			case SJME_OP_ATOMIC_COMPARE_GET_AND_SET:
				{
					/* Check. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Get. */
					ib = sjme_opdecodereg(jvm->vmem, &nextpc, error);
					
					/* Set. */
					ic = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Address. */
					id = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Offset. */
					ie = sjme_opdecodeui(jvm->vmem, &nextpc, error);
					
					/* Perform the operation. */
					r[ib] = sjme_vmmatomicintcheckgetandset(
						jvm->vmem, ia, ic, id, ie, error);
				}
				break;
				
				/* Atomic decrement and get. */
			case SJME_OP_ATOMIC_INT_DECREMENT_AND_GET:
				{
					/* Target register. */
					id = sjme_opdecodereg(jvm->vmem, &nextpc, error);
					
					/* Load address and offset. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					ib = sjme_opdecodeui(jvm->vmem, &nextpc, error);
					
					/* Modify the value accordingly. */
					r[id] = sjme_vmmatomicintaddandget(jvm->vmem, ia, ib,
						SJME_JINT_C(-1), error);
				}
				break;
				
				/* Atomic increment. */
			case SJME_OP_ATOMIC_INT_INCREMENT:
				{
					/* Load address and offset. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					ib = sjme_opdecodeui(jvm->vmem, &nextpc, error);
					
					/* Modify the value accordingly. */
					sjme_vmmatomicintaddandget(jvm->vmem, ia, ib,
						SJME_JINT_C(1), error);
				}
				break;
				
				/* Breakpoint. */
			case SJME_OP_BREAKPOINT:
				sjme_seterror(error, SJME_ERROR_CPUBREAKPOINT,
					jvm->totalinstructions);
				
				return cycles;
			
				/* Copy value. */
			case SJME_OP_COPY:
				{
					ia = sjme_opdecodereg(jvm->vmem, &nextpc, error);
					ib = sjme_opdecodereg(jvm->vmem, &nextpc, error);
					
					r[ib] = r[ia];
				}
				break;
			
				/* Debug entry. */
			case SJME_OP_DEBUG_ENTRY:
				{
					tempp = r[SJME_POOL_REGISTER];
					
					/* Get pointers to the real values. */
					cpu->state.debugclassname = sjme_vmmread(jvm->vmem,
						SJME_VMMTYPE_INTEGER, tempp, sjme_opdecodeui(jvm->vmem,
						&nextpc, error) * SJME_JINT_C(4), error);
					cpu->state.debugmethodname = sjme_vmmread(jvm->vmem,
						SJME_VMMTYPE_INTEGER, tempp, sjme_opdecodeui(jvm->vmem,
						&nextpc, error) * SJME_JINT_C(4), error);
					cpu->state.debugmethodtype = sjme_vmmread(jvm->vmem,
						SJME_VMMTYPE_INTEGER, tempp, sjme_opdecodeui(jvm->vmem,
						&nextpc, error) * SJME_JINT_C(4), error);
					cpu->state.debugsourcefile = sjme_vmmread(jvm->vmem,
						SJME_VMMTYPE_INTEGER, tempp, sjme_opdecodeui(jvm->vmem,
						&nextpc, error) * SJME_JINT_C(4), error);
				}
				break;
				
				/* Exit method. */
			case SJME_OP_DEBUG_EXIT:
				break;
				
				/* Debug point. */
			case SJME_OP_DEBUG_POINT:
				{
					cpu->state.debugline =
						sjme_opdecodeui(jvm->vmem, &nextpc, error);
					cpu->state.debugjop =
						(sjme_opdecodeui(jvm->vmem, &nextpc, error) &
						SJME_JINT_C(0xFF));
					cpu->state.debugjpc =
						sjme_opdecodeui(jvm->vmem, &nextpc, error);
				}
				break;
				
				/* If equal to constant? */
			case SJME_OP_IFEQ_CONST:
				{
					/* A value. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* B value. */
					ib = sjme_opdecodejint(jvm->vmem, &nextpc, error);
					
					/* Target PC address. */
					ic = sjme_opdecodejmp(jvm->vmem, &nextpc, error);
					tempp = cpu->state.pc + ic;
					
					/* Jump on equals? */
					if (ia == ib)
						nextpc = tempp;
				}
				break;
				
				/* Invoke method. */
			case SJME_OP_INVOKE:
				{
					/* Allocate to store old CPU state. */
					oldcpu = sjme_malloc(sizeof(*oldcpu));
					if (oldcpu == NULL)
					{
						sjme_seterror(error, SJME_ERROR_NOMEMORY,
							sizeof(*oldcpu));
						
						return cycles;
					}
					
					/* Copy and store state. */
					*oldcpu = cpu->state;
					cpu->state.parent = oldcpu;
					
					/* Setup CPU state for invoke run, move pool up. */
					for (ia = SJME_LOCAL_REGISTER_BASE;
						ia < SJME_MAX_REGISTERS; ia++)
						r[ia] = 0;
					r[SJME_POOL_REGISTER] = oldcpu->r[SJME_NEXT_POOL_REGISTER];
					r[SJME_NEXT_POOL_REGISTER] = 0;
					
					/* Inherit the task ID. */
					cpu->state.taskid = oldcpu->taskid;
					
					/* The address to execute. */
					ia = oldcpu->r[
						sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Load in register list (wide). */
					ib = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_BYTE, &nextpc,
						error);
					if ((ib & SJME_JINT_C(0x80)) != 0)
					{
						/* Skip back and read lower value. */
						nextpc--;
						ib = sjme_opdecodejshort(jvm->vmem, &nextpc, error);
						
						/* Read values. */
						for (ic = 0; ic < ib; ic++)
							r[SJME_ARGBASE_REGISTER + ic] = oldcpu->r[
								sjme_opdecodejshort(jvm->vmem, &nextpc,
									error)];
					}
					
					/* Narrow format list. */
					else
					{
						/* Read values. */
						for (ic = 0; ic < ib; ic++)
							r[SJME_ARGBASE_REGISTER + ic] =
								oldcpu->r[sjme_vmmreadp(jvm->vmem,
									SJME_VMMTYPE_BYTE, &nextpc, error)];
					}
					
#if defined(SJME_DEBUG)
					fprintf(stderr, "Invoke %08x (", (int)ia);
					for (ic = 0; ic < ib; ic++)
					{
						if (ic > 0)
							fprintf(stderr, ", ");
						fprintf(stderr, "%d/%08x",
							(int)r[SJME_ARGBASE_REGISTER + ic],
							(int)r[SJME_ARGBASE_REGISTER + ic]);
					}
					fprintf(stderr, ")\n");
#endif
					
					/* Old PC address resumes where this read ended. */
					oldcpu->pc = nextpc;
					
					/* Our next PC becomes the target address. */
					nextpc = ia;
					cpu->state.pc = nextpc;
				}
				break;
				
				/* Load value from integer array. */
			case SJME_OP_LOAD_FROM_INTARRAY:
				{
					/* Destination register. */
					ic = sjme_opdecodereg(jvm->vmem, &nextpc, error);
					
					/* Address and index */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					ib = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Load from array. */
					r[ic] = sjme_vmmread(jvm->vmem, SJME_VMMTYPE_INTEGER,
						ia, SJME_ARRAY_BASE_SIZE + (ib * SJME_JINT_C(4)),
						error);
				}
				break;
				
				/* Load value from constant pool. */
			case SJME_OP_LOAD_POOL:
				{
					/* The index to read from. */
					ia = sjme_opdecodeui(jvm->vmem, &nextpc, error);
					
					/* Write into destination register. */
					r[(ib = sjme_opdecodereg(jvm->vmem, &nextpc, error))] = 
						sjme_vmmread(jvm->vmem, SJME_VMMTYPE_INTEGER,
							r[SJME_POOL_REGISTER], (ia * SJME_JINT_C(4)),
							error);
					
#if defined(SJME_DEBUG)
					fprintf(stderr, "Load pool %d -> %d/%08x\n",
						(int)ia, (int)r[ib], (int)r[ib]);
#endif
				}
				break;
				
				/* Return from method. */
			case SJME_OP_RETURN:
				{
					/* Get parent CPU state. */
					oldcpu = cpu->state.parent;
					
					/* Exit must be done through an exit system call! */
					if (oldcpu == NULL)
					{
						sjme_seterror(error, SJME_ERROR_THREADRETURN,
							jvm->totalinstructions);
						
						return cycles;
					}
					
					/* Copy global values back. */
					for (ia = 0; ia < SJME_LOCAL_REGISTER_BASE; ia++)
						oldcpu->r[ia] = cpu->state.r[ia];
					
					/* Completely restore the old state. */
					cpu->state = *oldcpu;
					
					/* Restore continuing PC address. */
					nextpc = cpu->state.pc;
					
					/* Free the parent as it is not needed. */
					sjme_free(oldcpu);
					
#if defined(SJME_DEBUG)
					fprintf(stderr, "Return: %d/%08x\n",
						(int)r[SJME_RETURN_REGISTER],
						(int)r[SJME_RETURN_REGISTER]);
#endif
				}
				break;
				
				/* System call. */
			case SJME_OP_SYSTEM_CALL:
				{
					/* Clear system call arguments. */
					for (ia = 0; ia < SJME_MAX_SYSCALLARGS; ia++)
						cpu->syscallargs[ia] = 0;
					
					/* Load call type. */
					ia = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
					
					/* Load call arguments. */
					ic = sjme_opdecodeui(jvm->vmem, &nextpc, error);
					for (ib = 0; ib < ic; ib++)
					{
						/* Get value. */
						id = r[sjme_opdecodereg(jvm->vmem, &nextpc, error)];
						
						/* Set but never exceed the system call limit. */
						if (ib < SJME_MAX_SYSCALLARGS)
							cpu->syscallargs[ib] = id;
					}
					
					/* Call it and place result into the return register. */
					if (cpu->state.taskid == 0)
					{
						r[SJME_RETURN_REGISTER] = sjme_syscall(jvm, cpu, error,
							ia, cpu->syscallargs);
					}
					
					/* Otherwise perform supervisor handling of the call. */
					else
					{
						/* Allocate to store old CPU state. */
						oldcpu = sjme_malloc(sizeof(*oldcpu));
						if (oldcpu == NULL)
						{
							sjme_seterror(error, SJME_ERROR_NOMEMORY,
								sizeof(*oldcpu));
							
							return cycles;
						}
						
						/* Copy and store state. */
						*oldcpu = cpu->state;
						cpu->state.parent = oldcpu;
						
						/* Old PC address resumes where this ended. */
						oldcpu->pc = nextpc;
						
						/* Go back to the supervisor task. */
						cpu->state.taskid = 0;
						
						/* Setup arguments for this call. */
						r[SJME_POOL_REGISTER] = cpu->supervisorprops[
							SJME_SUPERPROP_TASK_SYSCALL_METHOD_POOL_POINTER];
						r[SJME_STATIC_FIELD_REGISTER] = cpu->supervisorprops[
							SJME_SUPERPROP_TASK_SYSCALL_STATIC_FIELD_POINTER];
						r[SJME_ARGBASE_REGISTER + 0] =
							oldcpu->taskid;
						r[SJME_ARGBASE_REGISTER + 1] =
							oldcpu->r[SJME_STATIC_FIELD_REGISTER];
						r[SJME_ARGBASE_REGISTER + 2] =
							ia;
						
						/* And arguments for it as well. */
						for (ie = 0; ie < SJME_MAX_SYSCALLARGS; ie++)
							r[SJME_ARGBASE_REGISTER + 3 + ie] =
								cpu->syscallargs[ie];
						
						/* Our next PC is the handler address. */
						nextpc = cpu->supervisorprops[
							SJME_SUPERPROP_TASK_SYSCALL_METHOD_HANDLER];
						cpu->state.pc = nextpc;
					}
					
					/* Stop if an error was set. */
					if (error->code != SJME_ERROR_NONE)
						return cycles;
				}
				break;
			
				/* Invalid operation. */
			default:
				sjme_seterror(error, SJME_ERROR_INVALIDOP, op);
				
				return cycles;
		}
		
		/* Check for error. */
		if (error != NULL)
			if (error->code != SJME_ERROR_NONE)
				return cycles;
		
		/* Set next PC address. */
		cpu->state.pc = nextpc;
	}
	
	/* Return remaining cycles. */
	return cycles;
}

/** Prints the error to the console output. */
void sjme_printerror(sjme_jvm* jvm, sjme_error* error)
{
	sjme_jint i, z;
	sjme_jint ec;
	sjme_jbyte b;
	sjme_jbyte hex[8];
	sjme_jint (*po)(sjme_jint b);
	
	/* Get output console. */
	po = (jvm->nativefuncs != NULL ? jvm->nativefuncs->stderr_write : NULL);
	
	/* Write the failure message. */
	sjme_console_pipewrite(jvm, po, sjme_execfailmessage, 0,
		sizeof(sjme_execfailmessage) / sizeof(sjme_jbyte), error);
	
	/* Read in hex bytes, for both forms. */
	for (z = 0; z < 2; z++)
	{
		/* Form hex value. */
		ec = (z == 0 ? error->code : error->value);
		for (i = 0; i < 8; i++)
		{
			b = (ec >> (4 * i)) & SJME_JINT_C(0xF);
			hex[7 - i] = (b < 10 ? 48 : (b - 10) + 97);
		}
		
		/* Print hex. */
		sjme_console_pipewrite(jvm, po,
			hex, 0, sizeof(hex) / sizeof(sjme_jbyte), error);
		
		/* Extra space? */
		if (z == 0)
		{
			b = 32;
			sjme_console_pipewrite(jvm, po, &b, 0, 1, error);
		}
	}
	
	/* End newline. */
	b = 13;
	sjme_console_pipewrite(jvm, po, &b, 0, 1, error);
	b = 10;
	sjme_console_pipewrite(jvm, po, &b, 0, 1, error);
	
	/* Always flush the screen on error. */
	if (jvm->fbinfo->flush != NULL)
		jvm->fbinfo->flush();
}

/** Executes code running within the JVM. */
sjme_jint sjme_jvmexec(sjme_jvm* jvm, sjme_error* error, sjme_jint cycles)
{
	sjme_jint threadid;
	sjme_cpu* cpu;
	sjme_error xerror;
	
	/* Fallback error state. */
	if (error == NULL)
	{
		memset(&xerror, 0, sizeof(xerror));
		error = &xerror;
	}
	
	/* Do nothing. */
	if (jvm == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return 0;
	}
	
	/* Run cooperatively threaded style CPU. */
	for (threadid = jvm->fairthreadid;;
		threadid = ((threadid + 1) & SJME_THREAD_MASK))
	{
		/* Have we used all our execution cycles? */
		if (cycles >= 0)
		{
			if (cycles == 0)
				break;
			if ((--cycles) <= 0)
				break;
		}
		
		/* Ignore CPUs which are not turned on. */
		cpu = &jvm->threads[threadid];
		if (cpu->threadstate == SJME_THREAD_STATE_NONE)
			continue;
		
		/* Execute CPU engine. */
		cycles = sjme_cpuexec(jvm, cpu, error, cycles);
		
		/* CPU fault, stop! */
		if (error->code != SJME_ERROR_NONE)
			break;
	}
	
	/* Start next run on the CPU that was last executing. */
	jvm->fairthreadid = (threadid & SJME_THREAD_MASK);
	
	/* Print error state to console? */
	if (error->code != SJME_ERROR_NONE)
	{
		/* Force error to be on-screen. */
		jvm->supervisorokay = 0;
		sjme_printerror(jvm, error);
	}
	
	/* Returning remaining number of cycles. */
	return cycles;
}

/**
 * Attempts to load a built-in ROM file.
 *
 * @param nativefuncs Native functions.
 * @param outromsize Output ROM size.
 * @param error Error flag.
 * @return The loaded ROM data or {@code NULL} if no ROM was loaded.
 * @since 2019/06/07
 */
void* sjme_loadrom(sjme_nativefuncs* nativefuncs, sjme_jint* outromsize,
	sjme_error* error)
{
	void* rv;
	sjme_nativefilename* fn;
	sjme_nativefile* file;
	sjme_jint romsize, readat, readcount;
	sjme_error xerror;
	
	/* Set error if missing. */
	if (error == NULL)
	{
		memset(&xerror, 0, sizeof(xerror));
		error = &xerror;
	}
	
	/* Need native functions. */
	if (nativefuncs == NULL || nativefuncs->nativeromfile == NULL ||
		nativefuncs->fileopen == NULL || nativefuncs->filesize == NULL ||
		nativefuncs->fileread == NULL)
	{
		sjme_seterror(error, SJME_ERROR_NOFILES, 0);
		
		return NULL;
	}
	
	/* Load file name used for the native ROM. */
	fn = nativefuncs->nativeromfile();
	if (fn == NULL)
	{
		sjme_seterror(error, SJME_ERROR_NONATIVEROM, 0);
		
		return NULL;
	}
	
	/* Set to nothing. */
	rv = NULL;
	
	/* Open ROM. */
	file = nativefuncs->fileopen(fn, SJME_OPENMODE_READ, error);
	if (file != NULL)
	{
		/* Need ROM size. */
		romsize = nativefuncs->filesize(file, error);
		
		/* Allocate ROM into memory. */
		rv = sjme_malloc(romsize);
		if (rv != NULL)
		{
			/* Read whatever is possible. */
			for (readat = 0; readat < romsize;)
			{
				/* Read into raw memory. */
				readcount = nativefuncs->fileread(file,
					SJME_POINTER_OFFSET_LONG(rv, readat), romsize - readat,
					error);
				
				/* EOF or error? */
				if (readcount < 0)
				{
					/* End of file reached? */
					if (error->code == SJME_ERROR_ENDOFFILE)
					{
						/* Reached early EOF?? */
						if (readat < romsize)
						{
							sjme_seterror(error, SJME_ERROR_EARLYEOF, 0);
							
							/* Failed */
							sjme_free(rv);
							return NULL;
						}
						
						/* Otherwise clear. */
						else
							sjme_seterror(error, SJME_ERROR_NONE, 0);
						
						break;
					}
					
					/* Otherwise fail */
					else
					{
						/* Force error to be set. */
						sjme_seterror(error, SJME_ERROR_READERROR, 0);
						
						/* Free resources. */
						sjme_free(rv);
						rv = NULL;
						break;
					}
				}
				
				/* Read count goes up. */
				readat += readcount;
			}
		}
		
		/* Just set error. */
		else
			sjme_seterror(error, SJME_ERROR_NOMEMORY, romsize);
		
		/* Close when done. */
		if (nativefuncs->fileclose != NULL)
			nativefuncs->fileclose(file, NULL);
	}
	
	/* Free file name when done. */
	if (nativefuncs->freefilename != NULL)
		nativefuncs->freefilename(fn);
	
	/* Output ROM size? */
	if (outromsize != NULL)
		*outromsize = romsize;
	
	/* Whatever value was used, if possible */
	return rv;
}

/**
 * Initializes the BootRAM, loading it from ROM.
 *
 * @param jvm The Java VM to initialize.
 * @param error Error flag.
 * @return Non-zero on success.
 * @since 2019/06/07
 */
sjme_jint sjme_initboot(sjme_jvm* jvm, sjme_error* error)
{
	sjme_vmemptr rp;
	sjme_vmemptr bootjar;
	sjme_jint bootoff, i, n, seedop, seedaddr, seedvalh, seedvall, seedsize;
	sjme_jint bootjaroff, vrambase, vrombase, qq;
	sjme_cpu* cpu;
	sjme_error xerror;
	
	/* Invalid arguments. */
	if (jvm == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return 0;
	}
	
	/* Force error to be set. */
	if (error == NULL)
	{
		memset(&xerror, 0, sizeof(xerror));
		error = &xerror;
	}
	
	/* Determine the address the VM sees for some memory types. */
	vrambase = jvm->ram->fakeptr;
	vrombase = jvm->rom->fakeptr;
	
	/* Set initial CPU (the first). */
	cpu = &jvm->threads[0];
	cpu->threadstate = SJME_THREAD_STATE_RUNNING;
	
	/* Set boot pointer to start of ROM. */
	rp = jvm->rom->fakeptr;
	
	/* Check ROM magic number. */
	if ((qq = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error))
		!= SJME_ROM_MAGIC_NUMBER)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDROMMAGIC, qq);
		
		return 0;
	}
	
	/* Ignore numjars, tocoffset, bootjarindex. */
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	
	/* Read and calculate BootJAR position. */
	bootjaroff = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp,
		error);
	rp = bootjar = vrombase + bootjaroff;
	
	/* Check JAR magic number. */
	if ((qq = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error))
		!= SJME_JAR_MAGIC_NUMBER)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDROMMAGIC, qq);
		
		return 0;
	}
	
	/* Ignore numrc, tocoffset, manifestoff, manifestlen. */
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	
	/* Read boot offset for later. */
	bootoff = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	
	/* Ignore bootsize. */
	sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	
	/* Seed initial CPU state. */
	cpu->state.r[SJME_POOL_REGISTER] = vrambase + sjme_vmmreadp(jvm->vmem,
		SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	cpu->state.r[SJME_STATIC_FIELD_REGISTER] = vrambase +
		sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	cpu->state.pc = (bootjar + sjme_vmmreadp(jvm->vmem,
		SJME_VMMTYPE_JAVAINTEGER, &rp, error));
	
	/* Load system call handler information. */
	
	sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, jvm->syscallsfp, 0, vrambase + sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error), error);
	sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, jvm->syscallcode, 0, bootjar + sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error), error);
	sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, jvm->syscallpool, 0, vrambase + sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error), error);
	
	/* Bootstrap entry arguments. */
	/* (int __rambase, int __ramsize, int __rombase, int __romsize, */
	/* int __confbase, int __confsize) */
	cpu->state.r[SJME_ARGBASE_REGISTER + 0] = jvm->ram->fakeptr;
	cpu->state.r[SJME_ARGBASE_REGISTER + 1] = jvm->ram->size;
	cpu->state.r[SJME_ARGBASE_REGISTER + 2] = jvm->rom->fakeptr;
	cpu->state.r[SJME_ARGBASE_REGISTER + 3] = jvm->rom->size;
	cpu->state.r[SJME_ARGBASE_REGISTER + 4] = jvm->config->fakeptr;
	cpu->state.r[SJME_ARGBASE_REGISTER + 5] = jvm->config->size;
	
#if defined(SJME_DEBUG)
	fprintf(stderr, "RAM=%08x+%d ROM=%08x+%d CFG=%08x+%d\n",
		(int)jvm->ram->fakeptr, (int)jvm->ram->size,
		(int)jvm->rom->fakeptr, (int)jvm->rom->size,
		(int)jvm->config->fakeptr, (int)jvm->config->size);
#endif
	
	/* Address where the BootRAM is read from. */
	rp = bootjar + bootoff;
	
	/* Copy initial base memory bytes, which is pure big endian. */
	n = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	for (i = 0; i < n; i++)
		sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_BYTE, vrambase, i,
			sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_BYTE, &rp, error), error);
	
	/* Load all seeds, which restores natural byte order. */
	n = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error);
	for (i = 0; i < n; i++)
	{
		/* Read seed information. */
		seedop = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_BYTE, &rp, error);
		seedsize = (seedop >> SJME_JINT_C(4)) & SJME_JINT_C(0xF);
		seedop = (seedop & SJME_JINT_C(0xF));
		seedaddr = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp,
			error);
		
		/* Wide value. */
		if (seedsize == 8)
		{
			seedvalh = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp,
				error);
			seedvall = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp,
				error);
		}
		
		/* Narrow value. */
		else
			seedvalh = sjme_vmmreadp(jvm->vmem, sjme_vmmsizetojavatype(
				seedsize, error), &rp, error);
		
		/* Make sure the seed types are correct. */
		if ((seedsize != 1 && seedsize != 2 &&
			seedsize != 4 && seedsize != 8) || 
			(seedop != 0 && seedop != 1 && seedop != 2) ||
			(seedsize == 8 && seedop != 0))
		{
			sjme_seterror(error, SJME_ERROR_INVALIDBOOTRAMSEED,
				seedop | (seedsize << SJME_JINT_C(4)));
			
			return 0;
		}
		
		/* Offset value if it is in RAM or JAR ROM. */
		if (seedop == 1)
			seedvalh += vrambase;
		else if (seedop == 2)
			seedvalh += bootjar;
		
		/* Write long value. */
		if (seedsize == 8)
		{
#if defined(SJME_BIG_ENDIAN)
			sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_INTEGER,
				vrambase, seedaddr, seedvalh, error);
			sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_INTEGER,
				vrambase + 4, seedaddr, seedvall, error);
#else
			sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_INTEGER,
				vrambase, seedaddr, seedvall, error);
			sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_INTEGER,
				vrambase + 4, seedaddr, seedvalh, error);
#endif
		}
		
		/* Write narrow value. */
		else
			sjme_vmmwrite(jvm->vmem, sjme_vmmsizetotype(seedsize, error),
				vrambase, seedaddr, seedvalh, error);
			
#if defined(SJME_DEBUG)
		fprintf(stderr, "SEED op=%d sz=%d -> @%08x+%08x (R@%08x) = %d/%08x\n",
			(int)seedop, (int)seedsize, (int)vrambase, (int)seedaddr,
			(int)(vrambase + seedaddr), (int)seedvalh, (int)seedvalh);
#endif
		
		/* Error was reached? */
		if (error->code != SJME_ERROR_NONE)
			return 0;
	}
	
	/* Check end value. */
	if ((qq = sjme_vmmreadp(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &rp, error))
		!= (~SJME_JINT_C(0)))
	{
		sjme_seterror(error, SJME_ERROR_INVALIDBOOTRAMEND, qq);
		
		return 0;
	}
	
	/* Force failure to happen! */
	if (error->code != SJME_ERROR_NONE)
		return 0;
	
	/* Okay! */
	return 1;
}

/** Destroys the virtual machine instance. */
sjme_jint sjme_jvmdestroy(sjme_jvm* jvm, sjme_error* error)
{
	sjme_cpustate* cpu;
	sjme_cpustate* oldcpu;
	sjme_jint i;
	
	/* Missing this? */
	if (jvm == NULL)
	{
		sjme_seterror(error, SJME_ERROR_INVALIDARG, 0);
		
		return 0;
	}
	
	/* Reset error. */
	sjme_seterror(error, SJME_ERROR_NONE, 0);
	
	/* Go through and cleanup CPUs. */
	for (i = 0; i < SJME_THREAD_MAX; i++)
	{
		/* Get CPU here. */
		cpu = &jvm->threads[i].state;
		
		/* Recursively clear CPU stacks. */
		while (cpu->parent != NULL)
		{
			/* Keep for later free. */
			oldcpu = cpu->parent;
			
			/* Copy down. */
			*cpu = *oldcpu;
			
			/* Free CPU state. */
			if (oldcpu != &jvm->threads[i].state)
				sjme_free(oldcpu);
		}
	}
	
	/* Delete major JVM data areas. */
	sjme_free(jvm->ram);
	sjme_free(jvm->config);
	if (jvm->presetrom == NULL)
		sjme_free(jvm->rom);
	
	/* Destroyed okay. */
	return 1;
}

/** Initializes the configuration space. */
void sjme_configinit(sjme_jvm* jvm, sjme_jvmoptions* options,
	sjme_nativefuncs* nativefuncs, sjme_error* error)
{
#define SJME_CONFIG_FORMAT_INTEGER SJME_JINT_C(1)
#define SJME_CONFIG_FORMAT_KEYVALUE SJME_JINT_C(2)
#define SJME_CONFIG_FORMAT_STRING SJME_JINT_C(3)
#define SJME_CONFIG_FORMAT_STRINGS SJME_JINT_C(4)
	sjme_vmemptr wp;
	sjme_vmemptr basep;
	sjme_vmemptr sizep;
	sjme_jint opt, format, iv, it, wlen;
	sjme_vmemptr* setpointer;
	char* sa;
	
	(void)options;
	(void)nativefuncs;
	
	/* Write pointer starts at the base area. */
	wp = jvm->config->fakeptr;
	
	/* Go through all possible options to make a value. */
	for (opt = SJME_JINT_C(1); opt < SJME_CONFIG_NUM_OPTIONS; opt++)
	{
		/* Used to specify the format. */
		format = 0;
		
		/* Reset. */
		sa = NULL;
		iv = 0;
		setpointer = NULL;
		
		/* Depends on the option. */
		switch (opt)
		{
			/* Java VM Version. */
			case SJME_CONFIG_JAVA_VM_VERSION:
				format = SJME_CONFIG_FORMAT_STRING;
				sa = "0.3.0";
				break;
			
			/* Java VM Name. */
			case SJME_CONFIG_JAVA_VM_NAME:
				format = SJME_CONFIG_FORMAT_STRING;
				sa = "SquirrelJME RatufaCoat";
				break;
			
			/* Java VM Vendor. */
			case SJME_CONFIG_JAVA_VM_VENDOR:
				format = SJME_CONFIG_FORMAT_STRING;
				sa = "Stephanie Gawroriski";
				break;
			
			/* Java VM E-Mail. */
			case SJME_CONFIG_JAVA_VM_EMAIL:
				format = SJME_CONFIG_FORMAT_STRING;
				sa = "xerthesquirrel@gmail.com";
				break;
			
			/* Java VM URL. */
			case SJME_CONFIG_JAVA_VM_URL:
				format = SJME_CONFIG_FORMAT_STRING;
				sa = "https://squirreljme.cc/";
				break;
			
			/* The guest depth. */
			case SJME_CONFIG_GUEST_DEPTH:
				break;
			
			/* Main class. */
			case SJME_CONFIG_MAIN_CLASS:
				break;
			
			/* Main program arguments. */
			case SJME_CONFIG_MAIN_ARGUMENTS:
				break;
			
			/* Is this a MIDlet? */
			case SJME_CONFIG_IS_MIDLET:
				break;
			
			/* Define system propertly. */
			case SJME_CONFIG_DEFINE_PROPERTY:
				break;
			
			/* Classpath to use. */
			case SJME_CONFIG_CLASS_PATH:
				break;
			
			/* System Call: Static field pointer. */
			case SJME_CONFIG_SYSCALL_STATIC_FIELD_POINTER:
				format = SJME_CONFIG_FORMAT_INTEGER;
				setpointer = &jvm->syscallsfp;
				break;
			
			/* System Call: Code Pointer. */
			case SJME_CONFIG_SYSCALL_CODE_POINTER:
				format = SJME_CONFIG_FORMAT_INTEGER;
				setpointer = &jvm->syscallcode;
				break;
			
			/* System Call: Pool Pointer. */
			case SJME_CONFIG_SYSCALL_POOL_POINTER:
				format = SJME_CONFIG_FORMAT_INTEGER;
				setpointer = &jvm->syscallpool;
				break;
			
				/* Unknown, ignore. */
			default:
				continue;
		}
		
		/* No known way to write this? */
		if (format == 0)
			continue;
		
		/* Write option key. */
		sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_JAVASHORT, &wp, opt, error);
		
		/* Store size location for later write. */
		sizep = wp;
		sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_JAVASHORT, &wp, 0, error);
		
		/* Base write pointer. */
		basep = wp;
		
		/* Set pointer as needed. */
		if (setpointer != NULL)
			*setpointer = basep;
		
		/* Depends on the format. */
		switch (format)
		{
				/* Integer. */
			case SJME_CONFIG_FORMAT_INTEGER:
				sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_JAVAINTEGER, &wp, iv,
					error);
				break;
			
				/* Key/value pair. */
			case SJME_CONFIG_FORMAT_KEYVALUE:
				break;
			
				/* String value. */
			case SJME_CONFIG_FORMAT_STRING:
				{
					// Record string length
					int iv = strlen(sa);
					sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_JAVASHORT, &wp, iv,
						error);
					
					// Record characters
					for (it = 0; it < iv; it++)
						sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_BYTE, &wp,
							(sjme_jint)sa[it], error);
				}
				break;
			
				/* Multiple strings. */
			case SJME_CONFIG_FORMAT_STRINGS:
				break;
		}
		
		/* Determine length and round it to 4 bytes. */
		wlen = wp - basep;
		while ((wlen & SJME_JINT_C(3)) != 0)
		{
			/* Write padding. */
			sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_BYTE, &wp, 0, error);
			wlen++;
		}
		
		/* Write to the actual size! */
		sjme_vmmwrite(jvm->vmem, SJME_VMMTYPE_JAVASHORT, sizep, 0, wlen,
			error);
	}
	
	/* Write end of config. */
	sjme_vmmwritep(jvm->vmem, SJME_VMMTYPE_BYTE, &wp, SJME_CONFIG_END, error);
	
#undef SJME_CONFIG_FORMAT_INTEGER
#undef SJME_CONFIG_FORMAT_KEYVALUE
#undef SJME_CONFIG_FORMAT_STRING
#undef SJME_CONFIG_FORMAT_STRINGS
}

/** Creates a new instance of the JVM. */
sjme_jvm* sjme_jvmnew(sjme_jvmoptions* options, sjme_nativefuncs* nativefuncs,
	sjme_error* error)
{
	sjme_jvmoptions nulloptions;
	void* ram;
	void* rom;
	void* conf;
	void* optionjar;
	sjme_jvm* rv;
	sjme_jint i, l, romsize;
	sjme_framebuffer* fbinfo;
	sjme_vmem* vmem;
	
	/* We need native functions. */
	if (nativefuncs == NULL)
		return NULL;
	
	/* Allocate virtual memory manager. */
	vmem = sjme_vmmnew(error);
	if (vmem == NULL)
	{
		sjme_seterror(error, SJME_ERROR_VMMNEWFAIL, error->code);
		
		return NULL;
	}
	
	/* Allocate VM state. */
	rv = sjme_malloc(sizeof(*rv));
	conf = sjme_malloc(SJME_DEFAULT_CONF_SIZE);
	if (rv == NULL || conf == NULL)
	{
		sjme_seterror(error, SJME_ERROR_NOMEMORY,
			sizeof(*rv) + SJME_DEFAULT_CONF_SIZE);
		
		sjme_free(rv);
		sjme_free(conf);
		
		return NULL;
	}
	
	/* Store virtual memory area. */
	rv->vmem = vmem;
	
	/* Virtual map config. */
	rv->config = sjme_vmmmap(vmem, 0, conf, SJME_DEFAULT_CONF_SIZE, error);
	if (rv->config == NULL)
		return NULL;
	
	/* If there were no options specified, just use a null set. */
	if (options == NULL)
	{
		memset(&nulloptions, 0, sizeof(nulloptions));
		options = &nulloptions;
	}
	
	/* If no RAM size was specified then use the default. */
	if (options->ramsize <= 0)
		options->ramsize = SJME_DEFAULT_RAM_SIZE;
	
	/* Allocate RAM, or at least keep trying to. */
	while (options->ramsize >= SJME_MINIMUM_RAM_SIZE)
	{
		/* Attempt to allocate the RAM. */
		ram = sjme_malloc(options->ramsize);
		
		/* Ram allocated! So stop. */
		if (ram != NULL)
			break;
		
		/* Cut RAM allocation size down in half. */
		options->ramsize /= 2;
	}
	
	/* Failed to allocate the RAM. */
	if (ram == NULL)
	{
		sjme_seterror(error, SJME_ERROR_NOMEMORY, options->ramsize);
			
		sjme_free(rv);
		sjme_free(conf);
		
		return NULL;
	}
	
	/* Virtual map RAM. */
	rv->ram = sjme_vmmmap(vmem, 0, ram, options->ramsize, error);
	if (rv->ram == NULL)
	{
		sjme_seterror(error, SJME_ERROR_VMMMAPFAIL, 0);
		
		sjme_free(rv);
		sjme_free(conf);
		
		return NULL;
	}
	
	/* Set native functions. */
	rv->nativefuncs = nativefuncs;
	
	/* Initialize the framebuffer info, if available. */
	fbinfo = NULL;
	if (nativefuncs->framebuffer != NULL)
		fbinfo = nativefuncs->framebuffer();
	
	/* Initialize framebuffer, done a bit early to show errors. */
	rv->fbinfo = fbinfo;
	if (fbinfo != NULL)
	{
		/* If scan-line is not specified, default to the display width. */
		if (fbinfo->scanlen == 0)
			fbinfo->scanlen = fbinfo->width;
		
		/* Number of available pixels. */
		if (fbinfo->numpixels == 0)
			fbinfo->numpixels = fbinfo->scanlen * fbinfo->height;
		
		/* Bytes per pixel must be specified. */
		if (fbinfo->bitsperpixel == 0)
			switch (fbinfo->format)
			{
				case SJME_PIXELFORMAT_PACKED_ONE:
					fbinfo->bitsperpixel = 1;
					break;
				
				case SJME_PIXELFORMAT_PACKED_TWO:
					fbinfo->bitsperpixel = 2;
					break;
				
				case SJME_PIXELFORMAT_PACKED_FOUR:
					fbinfo->bitsperpixel = 4;
					break;
				
				case SJME_PIXELFORMAT_BYTE_INDEXED:
					fbinfo->bitsperpixel = 8;
					break;
				
				case SJME_PIXELFORMAT_SHORT_RGB565:
					fbinfo->bitsperpixel = 16;
					break;
				
				default:
				case SJME_PIXELFORMAT_INTEGER_RGB888:
					fbinfo->bitsperpixel = 32;
					break;
			}
		
		/* Scan line in bytes is based on the bytes per pixel. */
		if (fbinfo->scanlenbytes == 0)
			fbinfo->scanlenbytes =
				(fbinfo->scanlen * fbinfo->bitsperpixel) / 8;
		
		/* Console positions and size. */
		rv->conx = 0;
		rv->cony = 0;
		rv->conw = fbinfo->width / sjme_font.charwidths[0];
		rv->conh = fbinfo->height / sjme_font.pixelheight;
	}
	
	/* Virtual map framebuffer, if available. */
	if (fbinfo != NULL)
	{
		rv->framebuffer = sjme_vmmmap(vmem, 0, fbinfo->pixels,
			(fbinfo->numpixels * fbinfo->bitsperpixel) / 8, error);
		if (rv->framebuffer == NULL)
		{
			sjme_seterror(error, SJME_ERROR_VMMMAPFAIL, 0);
			
			sjme_free(rv);
			sjme_free(ram);
			sjme_free(conf);
			
			return NULL;
		}
	}
	
	/* Needed by destruction later. */
	rv->presetrom = options->presetrom;
	
	/* Load the ROM? */
	rom = options->presetrom;
	if (rom == NULL)
	{
		/* Call sub-routine which can load the ROM. */
		rom = sjme_loadrom(nativefuncs, &romsize, error);
		
		/* Could not load the ROM? */
		if (rom == NULL)
		{
			/* Write the ROM failure message! */
			sjme_console_pipewrite(rv, (nativefuncs != NULL ?
				nativefuncs->stderr_write : NULL), sjme_romfailmessage, 0,
				sizeof(sjme_romfailmessage) / sizeof(sjme_jbyte), error);
			
			/* Clear resources */
			sjme_free(rv);
			sjme_free(ram);
			sjme_free(conf);
			
			/* Fail */
			return NULL;
		}
	}
	
	/* If we are copying from the preset ROM, duplicate it. */
	if (options->presetrom != NULL && options->copyrom != 0)
	{
		/* Use this ROM size. */
		romsize = options->romsize;
		
		/* Allocate space to fit ROM. */
		rom = sjme_malloc(options->romsize);
		if (rom == NULL)
		{
			sjme_seterror(error, SJME_ERROR_NOMEMORY, options->romsize);
			
			sjme_free(ram);
			sjme_free(conf);
			
			return NULL;
		}
		
		/* Copy large chunks at a time. */
		for (i = 0; i < options->romsize;)
		{
			/* Byte left to move? */
			l = options->romsize - i;
			
			/* Function uses a size_t which may be limited on this platform. */
			if (sizeof(sjme_jint) > sizeof(size_t) && l > (sjme_jint)SIZE_MAX)
				l = (sjme_jint)SIZE_MAX;
			
			/* Copy the data. */
			memmove(SJME_POINTER_OFFSET(rom, i),
				SJME_POINTER_OFFSET(options->presetrom, i), l);
			
			/* Offset up. */
			i += l;
		}
		
		/* We copied it, so never make a preset ROM. */
		rv->presetrom = NULL;
	}
	
	/* If we are using a preset ROM then just use the size. */
	if (rv->presetrom != NULL)
		romsize = options->romsize;
	
	/* Virtual map ROM. */
	rv->rom = sjme_vmmmap(vmem, 0, rom, romsize, error);
	if (rv->rom == NULL)
	{
		sjme_seterror(error, SJME_ERROR_VMMMAPFAIL, 0);
		
		sjme_free(rv);
		sjme_free(ram);
		sjme_free(conf);
		if (rv->presetrom == NULL)
			sjme_free(rom);
		
		return NULL;
	}
	
	/* Initialize configuration space. */
	sjme_configinit(rv, options, nativefuncs, error);
	
	/* Initialize the BootRAM and boot the CPU. */
	if (sjme_initboot(rv, error) == 0)
	{
		/* Write the Boot failure message! */
		sjme_console_pipewrite(rv, (nativefuncs != NULL ?
			nativefuncs->stderr_write : NULL), sjme_bootfailmessage, 0,
			sizeof(sjme_bootfailmessage) / sizeof(sjme_jbyte), error);
		
		/* Force error to be on-screen. */
		rv->supervisorokay = 0;
		sjme_printerror(rv, error);
		
		/* Cleanup. */
		sjme_free(rv);
		sjme_free(ram);
		sjme_free(conf);
		
		/* If a pre-set ROM is not being used, make sure it gets cleared. */
		if (rv->presetrom == NULL)
			sjme_free(rom);
		
		return NULL;
	}
	
	/* Memory map the option JAR, if available. */
	if (nativefuncs->optional_jar != NULL)
		if (nativefuncs->optional_jar(&optionjar, &i) != 0)
		{
			rv->optionjar = sjme_vmmmap(vmem, 0, optionjar, i, error);
			if (rv->rom == NULL)
			{
				sjme_seterror(error, SJME_ERROR_VMMMAPFAIL, 0);
				
				sjme_free(rv);
				sjme_free(ram);
				sjme_free(conf);
				if (rv->presetrom == NULL)
					sjme_free(rom);
				
				return NULL;
			}
		}
	
	/* The JVM is ready to use. */
	return rv;
}
