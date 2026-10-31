# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 OF Boot Loader

# Everything is in the text section
.section .text

# Starting entry point
.globl _start
_start:
	# --- 2013 Notes ---
	# When the IEEE1275 firmware interface boots, the following state is
	# specified:
	#   %r1 = Initial stack of at least 32KiB
	#   %r2 = 0
	#   %r3 = Platform binding specific
	#   %r4 = Platform binding specific
	#   %r5 = Client program interface (calls into firmware code)
	#   %r6 = Address of boot arguments.
	#   %r7 = Length of boot arguments (in bytes)
	#   %r?? = 0
	#
	# The MSR contains the following state:
	#   ee = 0, Interrupts disabled
	#   pr = 0, supervisor state
	#   fp = 1, floating point enabled
	#   me = 1, machine checks enabled
	#   fe0,fe1 = 0, floating point exceptions disabled
	#   ip = ??, Interrupts
	#   ir,dr = ??, Real-mode
	#   sf = 0, 32-bit mode
	#   ile,le = ???, little endian

	# The code at least in QEMU appears at 0x01000050
	
	# QEMU has trouble unless the load address of the binary is specifically
	# zero. (see below)
	
	# Right now, the ELF is linked at a load-base of 0x200000, however, on any
	# hardware system, this probably is not the case.
	#  On my PowerMac/PowerBook G4: 0x00800000
	#  On the QEMU test by default: 0x04000000
	# So any absolute jumps will just go down the drain, which is a bad thing.
	# So all the addresses must either be coded relative, or maybe paging setup
	# or something. Or hack every instruction to the correct base.
	# r3-r7 must be preserved
	# From more testing, seems it does not matter.
	# you can boot with:
	#  load-base release-load-area " hd:\mpa" $boot
	# And that will work on any system. So perhaps read the load-area value
	# from memory and see if our area is different, and if it is, just move it
	# around.
	# ------------------

_die:
	b _die
	nop
