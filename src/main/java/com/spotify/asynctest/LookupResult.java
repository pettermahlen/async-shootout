package com.spotify.asynctest;

import java.util.Objects;

/**
 * TODO: document!
 */
public class LookupResult {
  private final Version version;
  private final long value;

  public LookupResult(Version version, long value) {
    this.version = version;
    this.value = value;
  }

  public Version getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(version);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final LookupResult other = (LookupResult) obj;
    return Objects.equals(this.version, other.version);
  }
}
