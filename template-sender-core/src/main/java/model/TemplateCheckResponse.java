package model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TemplateCheckResponse {

    private Set<String> paramList;

    private Set<String> photoList;

}
