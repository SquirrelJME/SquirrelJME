// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.mips;

import net.multiphasicapps.squirreljme.jit.base.JITException;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.generic.GenericRegister;
import net.multiphasicapps.squirreljme.jit.generic.GenericRegisterType;

/**
 * This represents a MIPS CPU register.
 *
 * @since 2016/09/01
 */
public enum MIPSRegister
	implements GenericRegister
{
	/** r0. */
	R0(false, 0, GenericRegisterType.SAME),

	/** r1. */
	R1(false, 1, GenericRegisterType.SAME),

	/** r2. */
	R2(false, 2, GenericRegisterType.SAME),

	/** r3. */
	R3(false, 3, GenericRegisterType.SAME),

	/** r4. */
	R4(false, 4, GenericRegisterType.SAME),

	/** r5. */
	R5(false, 5, GenericRegisterType.SAME),

	/** r6. */
	R6(false, 6, GenericRegisterType.SAME),

	/** r7. */
	R7(false, 7, GenericRegisterType.SAME),

	/** r8. */
	R8(false, 8, GenericRegisterType.SAME),

	/** r9. */
	R9(false, 9, GenericRegisterType.SAME),

	/** r10. */
	R10(false, 10, GenericRegisterType.SAME),

	/** r11. */
	R11(false, 11, GenericRegisterType.SAME),

	/** r12. */
	R12(false, 12, GenericRegisterType.SAME),

	/** r13. */
	R13(false, 13, GenericRegisterType.SAME),

	/** r14. */
	R14(false, 14, GenericRegisterType.SAME),

	/** r15. */
	R15(false, 15, GenericRegisterType.SAME),

	/** r16. */
	R16(false, 16, GenericRegisterType.SAME),

	/** r17. */
	R17(false, 17, GenericRegisterType.SAME),

	/** r18. */
	R18(false, 18, GenericRegisterType.SAME),

	/** r19. */
	R19(false, 19, GenericRegisterType.SAME),

	/** r20. */
	R20(false, 20, GenericRegisterType.SAME),

	/** r21. */
	R21(false, 21, GenericRegisterType.SAME),

	/** r22. */
	R22(false, 22, GenericRegisterType.SAME),

	/** r23. */
	R23(false, 23, GenericRegisterType.SAME),

	/** r24. */
	R24(false, 24, GenericRegisterType.SAME),

	/** r25. */
	R25(false, 25, GenericRegisterType.SAME),

	/** r26. */
	R26(false, 26, GenericRegisterType.SAME),

	/** r27. */
	R27(false, 27, GenericRegisterType.SAME),

	/** r28. */
	R28(false, 28, GenericRegisterType.SAME),

	/** r29. */
	R29(false, 29, GenericRegisterType.SAME),

	/** r30. */
	R30(false, 30, GenericRegisterType.SAME),

	/** r31. */
	R31(false, 31, GenericRegisterType.SAME),
	
	/** f0. */
	F0(true, 0, GenericRegisterType.SAME),

	/** f1. */
	F1(true, 1, GenericRegisterType.SAME),

	/** f2. */
	F2(true, 2, GenericRegisterType.SAME),

	/** f3. */
	F3(true, 3, GenericRegisterType.SAME),

	/** f4. */
	F4(true, 4, GenericRegisterType.SAME),

	/** f5. */
	F5(true, 5, GenericRegisterType.SAME),

	/** f6. */
	F6(true, 6, GenericRegisterType.SAME),

	/** f7. */
	F7(true, 7, GenericRegisterType.SAME),

	/** f8. */
	F8(true, 8, GenericRegisterType.SAME),

	/** f9. */
	F9(true, 9, GenericRegisterType.SAME),

	/** f10. */
	F10(true, 10, GenericRegisterType.SAME),

	/** f11. */
	F11(true, 11, GenericRegisterType.SAME),

	/** f12. */
	F12(true, 12, GenericRegisterType.SAME),

	/** f13. */
	F13(true, 13, GenericRegisterType.SAME),

	/** f14. */
	F14(true, 14, GenericRegisterType.SAME),

	/** f15. */
	F15(true, 15, GenericRegisterType.SAME),

	/** f16. */
	F16(true, 16, GenericRegisterType.SAME),

	/** f17. */
	F17(true, 17, GenericRegisterType.SAME),

	/** f18. */
	F18(true, 18, GenericRegisterType.SAME),

	/** f19. */
	F19(true, 19, GenericRegisterType.SAME),

	/** f20. */
	F20(true, 20, GenericRegisterType.SAME),

	/** f21. */
	F21(true, 21, GenericRegisterType.SAME),

	/** f22. */
	F22(true, 22, GenericRegisterType.SAME),

	/** f23. */
	F23(true, 23, GenericRegisterType.SAME),

	/** f24. */
	F24(true, 24, GenericRegisterType.SAME),

	/** f25. */
	F25(true, 25, GenericRegisterType.SAME),

	/** f26. */
	F26(true, 26, GenericRegisterType.SAME),

	/** f27. */
	F27(true, 27, GenericRegisterType.SAME),

	/** f28. */
	F28(true, 28, GenericRegisterType.SAME),

	/** f29. */
	F29(true, 29, GenericRegisterType.SAME),

	/** f30. */
	F30(true, 30, GenericRegisterType.SAME),

	/** f31. */
	F31(true, 31, GenericRegisterType.SAME),

	/** End. */
	;
	
	/** Is this a floating point register? */
	protected final boolean isfloat;
	
	/** The register ID. */
	protected final int id;
	
	/** The register type. */
	protected final GenericRegisterType type;
	
	/**
	 * Initializes the register information.
	 *
	 * @param __float Is this a floating point register?
	 * @param __id The register ID.
	 * @param __t The type of register this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	private MIPSRegister(boolean __float, int __id, GenericRegisterType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.isfloat = __float;
		this.id = __id;
		this.type = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/01
	 */
	@Override
	public GenericRegisterType floatType()
	{
		if (this.isfloat)
			return this.type;
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/01
	 */
	@Override
	public GenericRegisterType intType()
	{
		if (this.isfloat)
			return null;
		return this.type;
	}
}

