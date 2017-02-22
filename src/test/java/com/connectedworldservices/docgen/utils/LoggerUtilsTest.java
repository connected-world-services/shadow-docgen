package com.connectedworldservices.docgen.utils;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;
@Slf4j
public class LoggerUtilsTest {

    private Appender mockAppender;

    @Test
    public void testAuditInfo()  {
        LoggerUtils.auditInfo(log, "request", "123", ()->"logstatement");
        String expectedLog = "logstatement";
        verifyLoggedArguments(ImmutableList.of(expectedLog));
    }

    @Test
    public void testAuditError() {
        LoggerUtils.auditError(log, "request", "123", ()->"logstatement");
        String expectedLog = "logstatement";
        verifyLoggedArguments(ImmutableList.of(expectedLog));
    }

    @Before
    public void setupLogger(){
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        mockAppender = mock(Appender.class);
        when(mockAppender.getName()).thenReturn("MOCK");
        root.addAppender(mockAppender);

    }


    private void verifyLoggedArguments(List<String> expectedInput) {
        Iterator<String> expected = FluentIterable.from(expectedInput).cycle().iterator();
        verify(mockAppender,times(expectedInput.size())).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {

                final String next = expected.next();
                final String message = ((LoggingEvent) argument).getMessage().replace("\r","");
                boolean starts= message.startsWith(next);
                return starts;
            }
        }));
    }
}