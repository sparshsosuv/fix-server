package com.flowlinx.fix.server.resource.representation;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class DeleteSessionRepresentation {

    private String beginString;
    private String connectionType;

    private String senderCompID;
    private String targetCompID;
}
