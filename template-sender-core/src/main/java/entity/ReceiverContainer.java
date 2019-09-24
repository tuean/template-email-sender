package entity;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiverContainer {

    private List<String> toList;

    private List<String> ccList;

    private List<String> bccList;


}
