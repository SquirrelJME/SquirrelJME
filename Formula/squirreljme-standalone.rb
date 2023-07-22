# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
#     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# For more information see license.txt.
# ---------------------------------------------------------------------------

# Cookbook: https://docs.brew.sh/Formula-Cookbook
# Formula Documentation: https://rubydoc.brew.sh/Formula
# Can be installed via: brew install --HEAD Formula/squirreljme-standalone.rb
class SquirreljmeStandalone < Formula
  desc 'Standalone Test Virtual Machine Jar, written purely in Java'
  homepage 'https://squirreljme.cc/'
  url 'https://squirreljme.cc/tarball/squirreljme-src.tar.gz?name=squirreljme-src&uuid=trunk'
  version '0.3.0'
  license 'MPL-2.0'
  head 'https://squirreljme.cc/', using: :fossil

  depends_on 'openjdk@11'

  def install
    system './gradlew', ':emulators:standalone:shadowJar'
    mkdir "#{bin}/"
    system './Formula/brew-install.sh', "#{bin}/", 'emulators/standalone/build/libs/'
  end

  test do
    system 'true'
  end
end
