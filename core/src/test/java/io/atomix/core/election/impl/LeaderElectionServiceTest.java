/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.core.election.impl;

import io.atomix.core.election.LeaderElectionType;
import io.atomix.core.election.Leadership;
import io.atomix.core.election.impl.LeaderElectionOperations.Run;
import io.atomix.primitive.PrimitiveId;
import io.atomix.primitive.service.ServiceConfig;
import io.atomix.primitive.service.ServiceContext;
import io.atomix.primitive.service.impl.DefaultBackupInput;
import io.atomix.primitive.service.impl.DefaultBackupOutput;
import io.atomix.primitive.service.impl.DefaultCommit;
import io.atomix.primitive.session.PrimitiveSession;
import io.atomix.primitive.session.SessionId;
import io.atomix.storage.buffer.Buffer;
import io.atomix.storage.buffer.HeapBuffer;
import io.atomix.utils.time.WallClock;
import org.junit.Test;

import static io.atomix.core.election.impl.LeaderElectionOperations.RUN;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Leader elector service test.
 */
public class LeaderElectionServiceTest {
  @Test
  public void testSnapshot() throws Exception {
    ServiceContext context = mock(ServiceContext.class);
    when(context.serviceType()).thenReturn(LeaderElectionType.instance());
    when(context.serviceName()).thenReturn("test");
    when(context.serviceId()).thenReturn(PrimitiveId.from(1));
    when(context.wallClock()).thenReturn(new WallClock());

    PrimitiveSession session = mock(PrimitiveSession.class);
    when(session.sessionId()).thenReturn(SessionId.from(1));

    LeaderElectionService service = new LeaderElectionService(new ServiceConfig());
    service.init(context);

    byte[] id = "a".getBytes();
    service.run(new DefaultCommit<>(
        2,
        RUN,
        new Run(id),
        session,
        System.currentTimeMillis()));

    Buffer buffer = HeapBuffer.allocate();
    service.backup(new DefaultBackupOutput(buffer, service.serializer()));

    service = new LeaderElectionService(new ServiceConfig());
    service.init(context);
    service.restore(new DefaultBackupInput(buffer.flip(), service.serializer()));

    Leadership<byte[]> value = service.getLeadership();
    assertNotNull(value);
    assertArrayEquals(value.leader().id(), id);
  }
}
