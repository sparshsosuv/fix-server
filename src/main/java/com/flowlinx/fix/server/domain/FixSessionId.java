package com.flowlinx.fix.server.domain;

import java.io.Serializable;
import java.util.Objects;

public class FixSessionId implements Serializable {
    private String sessionId;
    private String fixVersion;

    // Default constructor
    public FixSessionId() {}

    // Parameterized constructor
    public FixSessionId(String sessionId, String fixVersion) {
        this.sessionId = sessionId;
        this.fixVersion = fixVersion;
    }

    // Getters and setters
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(String fixVersion) {
        this.fixVersion = fixVersion;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FixSessionId that = (FixSessionId) o;
        return sessionId.equals(that.sessionId) && fixVersion.equals(that.fixVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, fixVersion);
    }
}
