// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode.mips;

import net.multiphasicapps.squirreljme.nativecode.NativeRegister;
import net.multiphasicapps.squirreljme.nativecode.NativeRegisterType;

/**
 * This represents a MIPS CPU register.
 *
 * @since 2016/09/01
 */
public enum MIPSRegister
	implements NativeRegister
{
	/** r0. */
	R0(false, 0),

	/** r1. */
	R1(false, 1),

	/** r2. */
	R2(false, 2),

	/** r3. */
	R3(false, 3),

	/** r4. */
	R4(false, 4),

	/** r5. */
	R5(false, 5),

	/** r6. */
	R6(false, 6),

	/** r7. */
	R7(false, 7),

	/** r8. */
	R8(false, 8),

	/** r9. */
	R9(false, 9),

	/** r10. */
	R10(false, 10),

	/** r11. */
	R11(false, 11),

	/** r12. */
	R12(false, 12),

	/** r13. */
	R13(false, 13),

	/** r14. */
	R14(false, 14),

	/** r15. */
	R15(false, 15),

	/** r16. */
	R16(false, 16),

	/** r17. */
	R17(false, 17),

	/** r18. */
	R18(false, 18),

	/** r19. */
	R19(false, 19),

	/** r20. */
	R20(false, 20),

	/** r21. */
	R21(false, 21),

	/** r22. */
	R22(false, 22),

	/** r23. */
	R23(false, 23),

	/** r24. */
	R24(false, 24),

	/** r25. */
	R25(false, 25),

	/** r26. */
	R26(false, 26),

	/** r27. */
	R27(false, 27),

	/** r28. */
	R28(false, 28),

	/** r29. */
	R29(false, 29),

	/** r30. */
	R30(false, 30),

	/** r31. */
	R31(false, 31),
	
	/** f0. */
	F0(true, 0),

	/** f1. */
	F1(true, 1),

	/** f2. */
	F2(true, 2),

	/** f3. */
	F3(true, 3),

	/** f4. */
	F4(true, 4),

	/** f5. */
	F5(true, 5),

	/** f6. */
	F6(true, 6),

	/** f7. */
	F7(true, 7),

	/** f8. */
	F8(true, 8),

	/** f9. */
	F9(true, 9),

	/** f10. */
	F10(true, 10),

	/** f11. */
	F11(true, 11),

	/** f12. */
	F12(true, 12),

	/** f13. */
	F13(true, 13),

	/** f14. */
	F14(true, 14),

	/** f15. */
	F15(true, 15),

	/** f16. */
	F16(true, 16),

	/** f17. */
	F17(true, 17),

	/** f18. */
	F18(true, 18),

	/** f19. */
	F19(true, 19),

	/** f20. */
	F20(true, 20),

	/** f21. */
	F21(true, 21),

	/** f22. */
	F22(true, 22),

	/** f23. */
	F23(true, 23),

	/** f24. */
	F24(true, 24),

	/** f25. */
	F25(true, 25),

	/** f26. */
	F26(true, 26),

	/** f27. */
	F27(true, 27),

	/** f28. */
	F28(true, 28),

	/** f29. */
	F29(true, 29),

	/** f30. */
	F30(true, 30),

	/** f31. */
	F31(true, 31),

	/** End. */
	;
	
	/** Is this a floating point register? */
	protected final boolean isfloat;
	
	/** The register ID. */
	protected final int id;
	
	/**
	 * Initializes the register information.
	 *
	 * @param __float Is this a floating point register?
	 * @param __id The register ID.
	 * @since 2016/09/01
	 */
	private MIPSRegister(boolean __float, int __id)
	{
		// Set
		this.isfloat = __float;
		this.id = __id;
	}
	
	/**
	 * Returns an existing MIPS register.
	 *
	 * @param __float If {@code true} then a floating point register is used.
	 * @param __i The register index to use.
	 * @return The associated register.
	 * @throws IllegalArgumentException If an illegal register was requested.
	 * @since 2016/09/01
	 */
	public static MIPSRegister of(boolean __float, int __i)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AW01 The register index is not within bounds
		// of the valid register set. (The register index)}
		if (__i < 0 || __i >= 32)
			throw new IllegalArgumentException(String.format("AW01 %d", __i));
		
		// Floating point?
		if (__float)
			switch (__i)
			{
				case 0: return F0;
				case 1: return F1;
				case 2: return F2;
				case 3: return F3;
				case 4: return F4;
				case 5: return F5;
				case 6: return F6;
				case 7: return F7;
				case 8: return F8;
				case 9: return F9;
				case 10: return F10;
				case 11: return F11;
				case 12: return F12;
				case 13: return F13;
				case 14: return F14;
				case 15: return F15;
				case 16: return F16;
				case 17: return F17;
				case 18: return F18;
				case 19: return F19;
				case 20: return F20;
				case 21: return F21;
				case 22: return F22;
				case 23: return F23;
				case 24: return F24;
				case 25: return F25;
				case 26: return F26;
				case 27: return F27;
				case 28: return F28;
				case 29: return F29;
				case 30: return F30;
				case 31: return F31;

					// Should not occur
				default:
					throw new RuntimeException("OOPS");
			}
		
		// Integer
		else
			switch (__i)
			{
				case 0: return R0;
				case 1: return R1;
				case 2: return R2;
				case 3: return R3;
				case 4: return R4;
				case 5: return R5;
				case 6: return R6;
				case 7: return R7;
				case 8: return R8;
				case 9: return R9;
				case 10: return R10;
				case 11: return R11;
				case 12: return R12;
				case 13: return R13;
				case 14: return R14;
				case 15: return R15;
				case 16: return R16;
				case 17: return R17;
				case 18: return R18;
				case 19: return R19;
				case 20: return R20;
				case 21: return R21;
				case 22: return R22;
				case 23: return R23;
				case 24: return R24;
				case 25: return R25;
				case 26: return R26;
				case 27: return R27;
				case 28: return R28;
				case 29: return R29;
				case 30: return R30;
				case 31: return R31;
				
					// Should not occur
				default:
					throw new RuntimeException("OOPS");
			}
	}
}

