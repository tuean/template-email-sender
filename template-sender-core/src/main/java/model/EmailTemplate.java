package model;

import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplate {

    private String smtpConfigId;

    private String template;

    private String title;

    private LinkedList<Map<String, Object>> params;

    private List<String> toList;

    private List<String> ccList;

    private List<String> bccList;


}
