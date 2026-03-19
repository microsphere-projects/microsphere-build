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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link ExampleService}.
 * Exercises the {@code test} and {@code coverage} build profiles.
 */
public class ExampleServiceTest {

    private final ExampleService service = new ExampleService();

    @Test
    public void testGreet() {
        assertEquals("Hello, World!", service.greet("World"));
    }

    @Test
    public void testGreetWithCustomName() {
        assertEquals("Hello, Microsphere!", service.greet("Microsphere"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreetWithNullName() {
        service.greet(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGreetWithEmptyName() {
        service.greet("");
    }

    @Test
    public void testGetJavaVersion() {
        assertNotNull(service.getJavaVersion());
    }
}
