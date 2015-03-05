/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.kuujo.copycat.log.io;

import net.kuujo.copycat.log.io.util.ReferenceCounted;
import net.kuujo.copycat.log.io.util.ReferenceManager;

/**
 * Buffer reader factory.
 *
 * @author <a href="http://github.com/kuujo">Jordan Halterman</a>
 */
@FunctionalInterface
public interface BufferReaderFactory<T extends BufferReader<?, U>, U extends Buffer & ReferenceCounted<? extends Buffer>> {

  /**
   * Creates a new buffer reader.
   *
   * @param buffer The underlying buffer.
   * @param offset The starting offset for the reader.
   * @param limit The limit for the reader.
   * @param referenceManager The reader reference manager.
   * @return The new reader instance.
   */
  T createReader(U buffer, long offset, long limit, ReferenceManager<T> referenceManager);

}
