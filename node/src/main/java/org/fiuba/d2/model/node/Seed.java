package org.fiuba.d2.model.node;

import org.fiuba.d2.connector.Connector;
import org.fiuba.d2.model.membership.MembershipEvent;

import java.util.List;

public class Seed {

    private Connector connector;

    public Seed(Connector connector) {
        this.connector = connector;
    }

    public List<MembershipEvent> getEventsSince(Long timestamp) {
        return connector.getEventsSince(timestamp);
    }
}
