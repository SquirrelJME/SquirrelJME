// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import net.multiphasicapps.classfile.CFConstantPool;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This class is given a chunk of byte code and represents that program in SSA
 * form.
 *
 * @since 2016/03/29
 */
public class CPProgram
	extends AbstractList<CPOp>
{
	/** The maximum size method code may be. */
	public static final int MAX_CODE_SIZE =
		65535;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The program constant pool. */
	protected final CFConstantPool constantpool;
	
	/** The size of the stack. */
	protected final int maxstack;
	
	/** The number of local variables. */
	protected final int maxlocals;
	
	/** Total variable count. */
	protected final int maxvariables;
	
	/** Physical code block length. */
	protected final int physicalsize;
	
	/** Logical instruction count. */
	protected final int logicalsize;
	
	/** The position of each logical instruction to a physical one. */
	private final int[] _ipos;
	
	/** The logical instructions in this program. */
	private final CPOp[] _logops;
	
	/**
	 * Initializes the class program with the given input stream represents
	 * the {@code Code} attribute.
	 *
	 * @param __is The source bytes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/10
	 */
	public CPProgram(CFClass __cl, CFMethod __method, InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__cl == null || __method == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Setup data input
		DataInputStream das = (__is instanceof DataInputStream ?
			(DataInputStream)__is : new DataInputStream(__is));
		__is = null;
		
		// Set
		constantpool = __cl.constantPool();
		
		// Max stack and local entries
		maxstack = das.readUnsignedShort();
		maxlocals = das.readUnsignedShort();
		maxvariables = maxstack + maxlocals;
		
		// Read code length
		int codelen = das.readInt();
		physicalsize = codelen;
		
		// {@squirreljme.error CP0d The length of the method code is either
		// zero, negative, or greater than the maximum limit permitted by
		// the virtual machine (64K). (The length of this code)}
		if (codelen <= 0 || codelen > MAX_CODE_SIZE)
			throw new CPProgramException(String.format("CP0d %d", codelen));
		
		// Read in the code array so it can be parsed later after the
		// exceptions and the (optional) StackMap/StackMapTable are parsed.
		byte rawcode[] = new byte[codelen];
		for (int rx = 0; rx < codelen;)
		{
			// Read data
			int rc = das.read(rawcode, rx, codelen - rx);
			
			// {@squirreljme.error CP0e Reached end of file reading while
			// reading the array of byte code which a method contains. (The
			// actual read length; The length of the code area)}
			if (rc < 0)
				throw new CPProgramException(String.format("CP0e %d %d",
					rx, codelen));
			
			// Add into it
			rx += rc;
		}
		
		// Read the exception table
		// If there are no exceptions, then use shorter handlers
		List<CPRawException> excs = new ArrayList<>();
		int numex = das.readUnsignedShort();
		for (int i = 0; i < numex; i++)
		{
			// Read data
			int spc = das.readUnsignedShort();
			int epc = das.readUnsignedShort();
			int hpc = das.readUnsignedShort();
			int xht = das.readUnsignedShort();
			
			// Add it
			excs.add(new CPRawException(spc, epc, hpc, (xht == 0 ? null :
				constantpool.<CFConstantEntry.ClassName>getAs(xht,
					CFConstantEntry.ClassName.class).symbol().
						asBinaryName())));
		}
		
		// Determine the position of all operations so that they can be
		// condensed into single index points (they all consume a single
		// address rather than multiple ones).
		int pn = length;
		int[] bp = new int[pn];
		int bpa = 0;
		for (int i = 0; i < pn;)
		{
			// Set position where this instruction starts
			bp[bpa++] = i;
			
			// Get instruction size here
			int sz = __ByteCodeSizes__.__sizeOf(i, rawcode);
			
			// Negative or zero size?
			if (sz <= 0)
				throw new RuntimeException("WTFX");
			
			// Go to next instruction
			i += sz;
		}
		
		// The byte code for this method entirely uses single byte instructions
		// so no condensation is needed
		if (bpa == pn)
			_ipos = bp;
		
		// Otherwise, condense
		else
		{
			// Setup array
			int[] actbp = new int[bpa];
			
			// Copy into it
			for (int i = 0; i < bpa; i++)
				actbp[i] = bp[i];
			
			// Use this array instead
			_ipos = actbp;
		}
		
		// Set logical size
		logicalsize = _ipos.length;
		
		// Not needed
		bp = null;
		
		// Setup logical operations
		int ln = logicalsize;
		CPOp[] logs = new CPOp[ln];
		_logops = logs;
		for (int i = 0; i < ln; i++)
			logs[i] = new CPOp(this, excs);
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public CPOp get(int __i)
	{
		// Check
		int max = logicalsize;
		if (__i < 0 || __i >= max)
			throw new IndexOutOfBoundsArguments(String.format("IOOB %d %d",
				__i, max));
		
		// Get instruction
		return _logops[__i];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int size()
	{
		return logicalsize;
	}
}

