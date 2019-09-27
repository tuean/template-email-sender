package model;

import entity.Annex;
import entity.PhotoTair;
import entity.ReceiverContainer;
import entity.ServerSetting;
import lombok.*;

import javax.sound.midi.Receiver;
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

    private ServerSetting smtpConfig;

    private String template;

    private String title;

    private LinkedList<Map<String, Object>> params;

    private LinkedList<Map<String, PhotoTair>> photos;

    private LinkedList<ReceiverContainer> receiverList;


}
