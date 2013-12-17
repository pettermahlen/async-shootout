package com.spotify.asynctest;

import java.util.Objects;

/**
 * TODO: document!
 */
public class DecorationResult {
  public static final DecorationResult DEFAULT = new DecorationResult("Nice decoration!");

  private final String decorationResult;

  public DecorationResult(String decorationResult) {
    this.decorationResult = decorationResult;
  }

  public String getDecorationResult() {
    return decorationResult;
  }

  @Override
  public int hashCode() {
    return Objects.hash(decorationResult);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final DecorationResult other = (DecorationResult) obj;
    return Objects.equals(this.decorationResult, other.decorationResult);
  }
}
