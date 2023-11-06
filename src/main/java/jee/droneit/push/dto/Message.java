package jee.droneit.push.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * WebSocket message representation.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Message
{

    /**
     * Message author.
     */
    @Setter
    private String from;

    /**
     * Message content.
     */
    @Setter
    private String content;

    @Setter
    private String target;

}
