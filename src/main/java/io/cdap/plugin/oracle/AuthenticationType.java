/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.oracle;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

/**
 * Specifies the mode of authentication to use.
 */
public enum AuthenticationType {
  BASIC("basic"),
  SESSION("session"),
  OAUTH("oauth");

  private static final Map<String, AuthenticationType> byDisplayName = Arrays.stream(values())
    .collect(Collectors.toMap(AuthenticationType::getDisplayName, Function.identity()));

  private final String displayName;

  AuthenticationType(String displayName) {
    this.displayName = displayName;
  }

  @Nullable
  public static AuthenticationType fromDisplayName(String displayName) {
    return byDisplayName.get(displayName);
  }

  public String getDisplayName() {
    return displayName;
  }
}
