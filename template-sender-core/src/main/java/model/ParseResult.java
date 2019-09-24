package model;

import entity.WholeMessageInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParseResult {


    private List<WholeMessageInfo> messageInfoList;

    private List<String> error;

}
