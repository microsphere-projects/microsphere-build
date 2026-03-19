/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.microsphere.example;

/**
 * Example service used to exercise the build profiles and Maven plugins
 * configured in the {@code microsphere-build} parent POM.
 *
 * <p>The following profiles are exercised when building this module:
 * <ul>
 *   <li>{@code publish} - artifact signing and publishing to Maven Central</li>
 *   <li>{@code ci} - signing via sign-maven-plugin in CI environments</li>
 *   <li>{@code test} - surefire, failsafe and checkstyle execution</li>
 *   <li>{@code coverage} - JaCoCo code-coverage instrumentation and reporting</li>
 *   <li>{@code docs} - AsciiDoc documentation generation</li>
 *   <li>{@code java8+} - activated automatically on JDK 8 and later</li>
 *   <li>{@code java9+} - activated automatically on JDK 9 and later</li>
 *   <li>{@code java11+} - activated automatically on JDK 11 and later</li>
 *   <li>{@code java9-15} - activated automatically on JDK 9 through 15</li>
 *   <li>{@code java16+} - activated automatically on JDK 16 and later</li>
 *   <li>{@code java17+} - activated automatically on JDK 17 and later</li>
 * </ul>
 */
public class ExampleService {

    /**
     * Returns a greeting message for the given name.
     *
     * @param name the name to greet; must not be {@code null}
     * @return a greeting string
     */
    public String greet(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must not be null or empty");
        }
        return "Hello, " + name + "!";
    }

    /**
     * Returns the current Java specification version as reported by the JVM.
     *
     * @return Java specification version string (e.g. {@code "17"})
     */
    public String getJavaVersion() {
        return System.getProperty("java.specification.version");
    }
}
