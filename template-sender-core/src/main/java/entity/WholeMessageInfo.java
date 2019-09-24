package entity;

import lombok.*;

import javax.mail.internet.InternetAddress;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WholeMessageInfo {

    private ServerSetting serverSetting;

    private String title;

    private String content;

    private String encoding;

    private boolean htmlFlag;

    private boolean iCalFlag;

    private String iCalText;

    private List<InternetAddress> toList;

    private List<InternetAddress> ccList;

    private List<InternetAddress> bccList;

    private List<Annex> annexList;

    private List<Annex> photoList;

}
