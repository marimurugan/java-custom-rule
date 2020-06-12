package org.sonar.sample.model;

import org.sonar.plugins.java.api.tree.Modifier;
import org.sonar.plugins.java.api.tree.ModifierKeywordTree;
import org.sonar.plugins.java.api.tree.ModifiersTree;

import javax.annotation.CheckForNull;
import java.util.Optional;

public final class ModifiersUtils {

  private ModifiersUtils() {
    // This class only contains static methods
  }

  public static boolean hasModifier(ModifiersTree modifiers, Modifier expectedModifier) {
    return findModifier(modifiers, expectedModifier).isPresent();
  }

  @CheckForNull
  public static ModifierKeywordTree getModifier(ModifiersTree modifiers, Modifier expectedModifier) {
    return findModifier(modifiers, expectedModifier).orElse(null);
  }

  public static Optional<ModifierKeywordTree> findModifier(ModifiersTree modifiersTree,  Modifier expectedModifier) {
    return modifiersTree.modifiers().stream()
      .filter(modifierKeywordTree -> modifierKeywordTree.modifier() == expectedModifier)
      .findAny();
  }
}