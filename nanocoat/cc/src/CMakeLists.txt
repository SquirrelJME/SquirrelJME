# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Compiler sources

# Compiler sources
add_executable(cc
	asm.c
	asm_ia16.c
	asm_ia32.c
	asm_m68k.c
	asm_ppc32.c
	cc.c
	cc_ia16.c
	cc_ia32.c
	cc_m68k.c
	cc_ppc32.c
	link.c
	link_coff.c
	link_elf.c
	link_macho.c
	link_mz.c
	link_ne.c
	link_palmos.c
	link_pe.c
	link_pef.c
	link_xcoff.c
	main.c
	preproc.c)

# Includes for the compiler
target_include_directories(cc
	"${CMAKE_SOURCE_DIR}/include")
