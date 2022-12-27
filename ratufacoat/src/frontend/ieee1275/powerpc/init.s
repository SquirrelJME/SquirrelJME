# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# -----------------------------------------------------------------------------
# Start location for OFW on PowerPC systems

.section .text.start
.globl _start
_start:

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
	# And that will work on any system. So perhaps read the load-area value from
	# memory and see if our area is different, and if it is, just move it around.

		# maybe later

	# Copy OFW initial register pieces to variables in memory somewhere
	stw 2, -4(1)								# Put old r2 on stack

	#lis 27, 0xCAFE
	#ori 27, 27, 0xBABE
	#lis 28, 0xCAFE
	#ori 28, 28, 0xBABE

	lis 2, sjme_ieee1275EntryFunc@ha				# Entry point
	ori 2, 2, sjme_ieee1275EntryFunc@l
	stw 5, 0(2)

	lis 2, sjme_ieee1275EntryArgs@ha					# Arguments
	ori 2, 2, sjme_ieee1275EntryArgs@l
	stw 6, 0(2)

	lis 2, sjme_ieee1275EntryNumArgs@ha					# Length of arguments
	ori 2, 2, sjme_ieee1275EntryNumArgs@l
	stw 7, 0(2)

	lwz 2, -4(1)								# Restore old r2 from stack

	# Jump to main platform code
	#b sjme_ieee1275BootArch
	bl sjme_ieee1275BootArch

	# If this is reached =(
.globl _die
_die:
	b _die
