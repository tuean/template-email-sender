package model;

import entity.PhotoTair;
import entity.ReceiverContainer;
import entity.ServerSetting;
import lombok.*;

import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplate {

    private ServerSetting serverSetting;

    private boolean useTemplate;

    private String template;

    private String title;

    private LinkedList<Map<String, Object>> params;

    private LinkedList<Map<String, PhotoTair>> photos;

    private LinkedList<ReceiverContainer> receiverList;


}
