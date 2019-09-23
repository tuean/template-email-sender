package model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParseResult {


    private List<EmailModel> modelList;

    private List<String> error;

}
