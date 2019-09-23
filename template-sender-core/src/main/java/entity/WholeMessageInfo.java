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

    private InternetAddress[] toList;

    private InternetAddress[] ccList;

    private InternetAddress[] bccList;

    private List<Annex> annexList;

    private List<Annex> photoList;

}
