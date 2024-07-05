package com.kidchang.lingopress.videoTranscriptions;

import com.kidchang.lingopress._base.entity.BaseTimeEntity;
import com.kidchang.lingopress.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class VideoTranscriptions extends BaseTimeEntity {
    @Id
    private Long id;
    private String language;
    private String videoUrl;
    private VideoProcessingEnum processingStatus;
    // 요청자
    @ManyToOne
    private User user;
}
