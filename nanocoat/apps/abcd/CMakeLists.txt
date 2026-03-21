# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# Description: Ancient Build Contraption -- Used to build SquirrelJME and
# legacy Java ME software using more modern systems.
#
# References:
# https://docs.oracle.com/javame/dev-tools/jme-sdk-3.4/nb/html/commandline.htm
# https://www.oracle.com/technical-resources/articles/javame/midp-programming-packaging.html
# https://www.dribin.org/dave/j2me_ant/users_guide.html

# Setup base executable
add_executable(AppAncientBuildContraptionDevice
	ant.c
	cat.c
	device-address.c
	device-manager.c
	dispatch.c
	doja.c
	doja_g.c
	eclipse.c
	emulator.c
	jadtool.c
	jar.c
	jar2prc.c
	javac.c
	javadoc.c
	javap.c
	jasmin.c
	jdb.c
	iconv.c
	mekeytool.c
	midp.c
	netbeans.c
	preverify.c
	star.c
	touch.c
	wscompile.c
	zip.c)

# Include main headers
target_include_directories(AppAncientBuildContraptionDevice PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include")

# Link against the base libraries
squirreljme_link_libraries_required(AppAncientBuildContraptionDevice PUBLIC
	BaseStatic)

# Name it something easier to use
squirreljme_target_binary_name(AppAncientBuildContraptionDevice
	"sjmeabcd")

# Output to the base directory
squirreljme_target_binary_output(AppAncientBuildContraptionDevice
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")
